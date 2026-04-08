package uptc.edu.co.ms_auth.auth.service;

import java.util.HashSet;
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
import uptc.edu.co.ms_auth.auth.model.Role;
import uptc.edu.co.ms_auth.auth.model.User;
import uptc.edu.co.ms_auth.auth.repository.RoleRepository;
import uptc.edu.co.ms_auth.auth.repository.UserRepository;
import uptc.edu.co.ms_auth.auth.security.JwtService;
import uptc.edu.co.ms_auth.auth.security.Sha256Hasher;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Sha256Hasher hasher;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       Sha256Hasher hasher,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.hasher = hasher;
        this.jwtService = jwtService;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(hasher.hash(request.getPassword()));
        user.setActive(true);

        // Assign requested roles, default to USER if none provided
        List<String> requestedRoles = (request.getRoles() != null && !request.getRoles().isEmpty())
                ? request.getRoles()
                : List.of("USER");

        Set<Role> assignedRoles = new HashSet<>();
        for (String roleName : requestedRoles) {
            roleRepository.findByName(roleName.toUpperCase())
                    .ifPresent(assignedRoles::add);
        }
        user.setRoles(assignedRoles);

        User saved = userRepository.save(user);

        List<String> roleNames = saved.getRoles().stream()
                .map(Role::getName)
                .sorted()
                .toList();

        return new RegisterResponse(saved.getId(), saved.getUsername(), saved.isActive(), roleNames);
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
