package uptc.edu.co.ms_auth.auth.service;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import uptc.edu.co.ms_auth.auth.dto.AuthResponse;
import uptc.edu.co.ms_auth.auth.dto.LoginRequest;
import uptc.edu.co.ms_auth.auth.dto.RegisterRequest;
import uptc.edu.co.ms_auth.auth.dto.RegisterResponse;
import uptc.edu.co.ms_auth.auth.model.User;
import uptc.edu.co.ms_auth.auth.repository.UserRepository;
import uptc.edu.co.ms_auth.auth.security.JwtService;
import uptc.edu.co.ms_auth.auth.security.Sha256Hasher;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final Sha256Hasher hasher;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       Sha256Hasher hasher,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.hasher = hasher;
        this.jwtService = jwtService;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        String normalizedEmail = normalizeEmail(request.getUsername());
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        if (normalizedEmail != null && userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(normalizedEmail);
        user.setPasswordHash(hasher.hash(request.getPassword()));
        user.setActive(true);

        User saved = userRepository.save(user);

        return new RegisterResponse(saved.getId(), saved.getUsername(), saved.isActive());
    }

    private String normalizeEmail(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        if (!trimmed.contains("@")) {
            return null;
        }

        return trimmed.toLowerCase();
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Inactive user");
        }

        String incomingHash = hasher.hash(request.getPassword());
        if (!incomingHash.equals(user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        Set<String> scopes = userRepository.findScopesByUsername(user.getUsername());
        List<String> sortedScopes = scopes.stream().sorted().toList();

        String token = jwtService.generateToken(user.getUsername(), sortedScopes);
        return new AuthResponse(token, "Bearer", jwtService.getExpirationSeconds(), sortedScopes);
    }
}
