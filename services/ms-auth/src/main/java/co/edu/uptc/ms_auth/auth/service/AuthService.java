package co.edu.uptc.ms_auth.auth.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uptc.ms_auth.auth.dto.AuthResponse;
import co.edu.uptc.ms_auth.auth.dto.LoginRequest;
import co.edu.uptc.ms_auth.auth.dto.RegisterRequest;
import co.edu.uptc.ms_auth.auth.dto.RegisterResponse;
import co.edu.uptc.ms_auth.auth.model.User;
import co.edu.uptc.ms_auth.auth.repository.UserRepository;
import co.edu.uptc.ms_auth.auth.security.JwtService;
import co.edu.uptc.ms_auth.auth.security.Sha256Hasher;
import co.edu.uptc.shared.exceptions.AuthenticationException;
import co.edu.uptc.shared.exceptions.BusinessException;
import co.edu.uptc.shared.exceptions.ValidationException;
import org.springframework.http.HttpStatus;

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
        // Validate username format
        if (!isValidUsername(request.getUsername())) {
            throw new ValidationException("Invalid username format. Username must be 3-100 characters and contain only letters, numbers, dots, or underscores.");
        }

        // Validate email format
        String normalizedEmail = normalizeEmail(request.getEmail());
        if (normalizedEmail == null) {
            throw new ValidationException("A valid email address is required.");
        }

        // Check for duplicate username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Username already exists", HttpStatus.CONFLICT, "USER_ALREADY_EXISTS");
        }

        if (normalizedEmail != null && userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new BusinessException("Email already exists", HttpStatus.CONFLICT, "EMAIL_ALREADY_EXISTS");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(normalizedEmail);
        user.setPasswordHash(hasher.hash(request.getPassword()));
        user.setActive(true);

        User saved = userRepository.save(user);

        return new RegisterResponse(saved.getId(), saved.getUsername(), saved.isActive());
    }

    private boolean isValidUsername(String username) {
        if (username == null || username.length() < 3 || username.length() > 100) {
            return false;
        }
        return username.matches("^[a-zA-Z0-9._]+$");
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
                .orElseThrow(() -> new AuthenticationException("Invalid credentials"));

        if (!user.isActive()) {
            throw new AuthenticationException("Inactive user");
        }

        String incomingHash = hasher.hash(request.getPassword());
        if (!incomingHash.equals(user.getPasswordHash())) {
            throw new AuthenticationException("Invalid credentials");
        }

        Set<String> scopes = userRepository.findScopesByUsername(user.getUsername());
        List<String> sortedScopes = scopes.stream().sorted().toList();

        String token = jwtService.generateToken(user.getUsername(), sortedScopes);
        return new AuthResponse(token, "Bearer", jwtService.getExpirationSeconds(), sortedScopes);
    }
}
