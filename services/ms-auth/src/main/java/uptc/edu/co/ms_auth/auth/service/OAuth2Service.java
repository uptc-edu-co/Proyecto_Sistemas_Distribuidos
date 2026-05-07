package uptc.edu.co.ms_auth.auth.service;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import co.edu.uptc.shared.exceptions.AuthenticationException;
import co.edu.uptc.shared.exceptions.ResourceNotFoundException;
import co.edu.uptc.shared.exceptions.ValidationException;
import co.edu.uptc.shared.security.RoleScopeCatalog;
import uptc.edu.co.ms_auth.auth.config.OAuth2Properties;
import uptc.edu.co.ms_auth.auth.dto.AuthResponse;
import uptc.edu.co.ms_auth.auth.dto.oauth.GoogleTokenResponse;
import uptc.edu.co.ms_auth.auth.dto.oauth.GoogleUserInfo;
import uptc.edu.co.ms_auth.auth.model.ExternalIdentity;
import uptc.edu.co.ms_auth.auth.model.Role;
import uptc.edu.co.ms_auth.auth.model.User;
import uptc.edu.co.ms_auth.auth.repository.ExternalIdentityRepository;
import uptc.edu.co.ms_auth.auth.repository.RoleRepository;
import uptc.edu.co.ms_auth.auth.repository.UserRepository;
import uptc.edu.co.ms_auth.auth.security.JwtService;
import uptc.edu.co.ms_auth.auth.security.Sha256Hasher;

@Service
public class OAuth2Service {

    private static final String PROVIDER_GOOGLE = "google";

    private final OAuth2Properties properties;
    private final OAuth2StateStore stateStore;
    private final ExternalIdentityRepository externalIdentityRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final Sha256Hasher hasher;
    private final RestClient restClient;

    public OAuth2Service(OAuth2Properties properties,
            OAuth2StateStore stateStore,
            ExternalIdentityRepository externalIdentityRepository,
            UserRepository userRepository,
            RoleRepository roleRepository,
            JwtService jwtService,
            Sha256Hasher hasher) {
        this.properties = properties;
        this.stateStore = stateStore;
        this.externalIdentityRepository = externalIdentityRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
        this.hasher = hasher;
        this.restClient = RestClient.builder().build();
    }

    public URI buildAuthorizationUrl(String provider) {
        if (!PROVIDER_GOOGLE.equalsIgnoreCase(provider)) {
            throw new ResourceNotFoundException("Unsupported OAuth provider: " + provider);
        }

        OAuth2Properties.Google google = properties.getGoogle();
        String state = stateStore.createState();

        return UriComponentsBuilder.fromUriString(google.getAuthUri())
                .queryParam("client_id", google.getClientId())
                .queryParam("redirect_uri", google.getRedirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope", google.getScopes())
                .queryParam("state", state)
                .build()
                .toUri();
    }

    @Transactional
    public AuthResponse handleCallback(String provider, String code, String state) {
        if (!PROVIDER_GOOGLE.equalsIgnoreCase(provider)) {
            throw new ResourceNotFoundException("Unsupported OAuth provider: " + provider);
        }

        if (code == null || code.isBlank()) {
            throw new ValidationException("Missing OAuth code");
        }

        if (!stateStore.consumeState(state)) {
            throw new AuthenticationException("Invalid OAuth state");
        }

        OAuth2Properties.Google google = properties.getGoogle();
        GoogleTokenResponse tokenResponse = exchangeCodeForToken(google, code);
        GoogleUserInfo userInfo = fetchUserInfo(google, tokenResponse);

        User user = resolveUserForGoogle(userInfo);
        Set<String> scopes = userRepository.findScopesByUsername(user.getUsername());
        List<String> sortedScopes = scopes.stream().sorted().toList();

        String token = jwtService.generateToken(user.getUsername(), sortedScopes);
        return new AuthResponse(token, "Bearer", jwtService.getExpirationSeconds(), sortedScopes);
    }

    private GoogleTokenResponse exchangeCodeForToken(OAuth2Properties.Google google, String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", google.getClientId());
        formData.add("client_secret", google.getClientSecret());
        formData.add("redirect_uri", google.getRedirectUri());
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");

        GoogleTokenResponse response = restClient.post()
                .uri(google.getTokenUri())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(GoogleTokenResponse.class);

        if (response == null || response.getAccessToken() == null) {
            throw new AuthenticationException("Invalid token response from Google");
        }

        return response;
    }

    private GoogleUserInfo fetchUserInfo(OAuth2Properties.Google google, GoogleTokenResponse tokenResponse) {
        GoogleUserInfo userInfo = restClient.get()
                .uri(google.getUserInfoUri())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getAccessToken())
                .retrieve()
                .body(GoogleUserInfo.class);

        if (userInfo == null || userInfo.getEmail() == null || userInfo.getEmail().isBlank()) {
            throw new AuthenticationException("Missing email from Google profile");
        }

        return userInfo;
    }

    private User resolveUserForGoogle(GoogleUserInfo userInfo) {
        String providerUserId = userInfo.getSub();
        String email = normalizeEmail(userInfo.getEmail());

        Optional<ExternalIdentity> existingIdentity = externalIdentityRepository
                .findByProviderAndProviderUserId(PROVIDER_GOOGLE, providerUserId);

        if (existingIdentity.isPresent()) {
            return existingIdentity.get().getUser();
        }

        User user = userRepository.findByEmailIgnoreCase(email)
                .or(() -> userRepository.findByUsername(email))
                .orElseGet(() -> createUserFromOAuth(email));

        ExternalIdentity identity = new ExternalIdentity();
        identity.setProvider(PROVIDER_GOOGLE);
        identity.setProviderUserId(providerUserId);
        identity.setEmail(email);
        identity.setUser(user);
        externalIdentityRepository.save(identity);

        return user;
    }

    private User createUserFromOAuth(String email) {
        User user = new User();
        user.setUsername(email);
        user.setEmail(email);
        user.setPasswordHash(hasher.hash(email + System.nanoTime()));
        user.setActive(true);

        Role defaultRole = roleRepository.findByName(RoleScopeCatalog.ROLE_USER)
                .orElseGet(() -> roleRepository.save(new Role(RoleScopeCatalog.ROLE_USER)));
        user.getRoles().add(defaultRole);

        return userRepository.save(user);
    }

    private String normalizeEmail(String value) {
        if (value == null) {
            return null;
        }

        return value.trim().toLowerCase();
    }
}
