package uptc.edu.co.ms_auth.auth.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import uptc.edu.co.ms_auth.auth.dto.AuthResponse;
import uptc.edu.co.ms_auth.auth.dto.LoginRequest;
import uptc.edu.co.ms_auth.auth.dto.RegisterRequest;
import uptc.edu.co.ms_auth.auth.dto.RegisterResponse;
import uptc.edu.co.ms_auth.auth.config.OAuth2Properties;
import uptc.edu.co.ms_auth.auth.service.OAuth2Service;
import uptc.edu.co.ms_auth.auth.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final OAuth2Service oauth2Service;
    private final OAuth2Properties oauth2Properties;

    public AuthController(AuthService authService,
                          OAuth2Service oauth2Service,
                          OAuth2Properties oauth2Properties) {
        this.authService = authService;
        this.oauth2Service = oauth2Service;
        this.oauth2Properties = oauth2Properties;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/oauth2/authorize/google")
    public ResponseEntity<Void> authorizeGoogle() {
        URI redirect = oauth2Service.buildAuthorizationUrl("google");
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(redirect)
                .build();
    }

    @GetMapping("/oauth2/callback/google")
    public ResponseEntity<Void> callbackGoogle(@RequestParam(required = false) String code,
                                               @RequestParam(required = false) String state,
                                               @RequestParam(required = false) String error) {
        if (error != null && !error.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "OAuth login failed: " + error);
        }

        AuthResponse response = oauth2Service.handleCallback("google", code, state);
        URI redirect = buildPostLoginRedirect(response);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(redirect)
                .build();
    }

    private URI buildPostLoginRedirect(AuthResponse response) {
        String baseRedirect = oauth2Properties.getPostLoginRedirect();
        if (baseRedirect == null || baseRedirect.isBlank()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Missing post-login redirect URL");
        }

        String scopes = response.getScopes() == null ? "" : String.join(",", response.getScopes());
        String fragment = buildFragment(response.getToken(), response.getTokenType(), response.getExpiresIn(), scopes);

        return UriComponentsBuilder.fromUriString(baseRedirect)
                .fragment(fragment)
                .build()
                .toUri();
    }

    private String buildFragment(String token, String tokenType, long expiresIn, String scopes) {
        return "token=" + urlEncode(token)
                + "&token_type=" + urlEncode(tokenType)
                + "&expires_in=" + expiresIn
                + "&scopes=" + urlEncode(scopes);
    }

    private String urlEncode(String value) {
        return UriUtils.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }
}
