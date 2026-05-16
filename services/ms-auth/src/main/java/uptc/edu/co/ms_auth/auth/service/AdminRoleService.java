package uptc.edu.co.ms_auth.auth.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import uptc.edu.co.ms_auth.auth.dto.RoleAssignmentRequest;
import uptc.edu.co.ms_auth.auth.dto.RolePermissionsRequest;
import uptc.edu.co.ms_auth.auth.dto.RoleResponse;
import uptc.edu.co.ms_auth.auth.dto.UserRolesResponse;
import uptc.edu.co.ms_auth.auth.model.Permission;
import uptc.edu.co.ms_auth.auth.model.Role;
import uptc.edu.co.ms_auth.auth.model.User;
import uptc.edu.co.ms_auth.auth.repository.PermissionRepository;
import uptc.edu.co.ms_auth.auth.repository.RoleRepository;
import uptc.edu.co.ms_auth.auth.repository.UserRepository;

@Service
public class AdminRoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public AdminRoleService(UserRepository userRepository,
                            RoleRepository roleRepository,
                            PermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Transactional(readOnly = true)
    public List<RoleResponse> listRoles() {
        return roleRepository.findAll().stream()
                .map(role -> new RoleResponse(role.getName(), toSortedPermissionCodes(role)))
                .sorted((left, right) -> left.getName().compareToIgnoreCase(right.getName()))
                .toList();
    }

    @Transactional
    public UserRolesResponse updateUserRoles(Long userId, RoleAssignmentRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Set<String> addNames = normalizeNames(request == null ? null : request.getAdd());
        Set<String> removeNames = normalizeNames(request == null ? null : request.getRemove());
        Set<String> requestedNames = new HashSet<>();
        requestedNames.addAll(addNames);
        requestedNames.addAll(removeNames);

        Map<String, Role> resolvedRoles = resolveRolesByName(requestedNames);
        removeMissingRoles(requestedNames, resolvedRoles);

        Set<Role> updated = new HashSet<>(user.getRoles());
        if (!removeNames.isEmpty()) {
            updated.removeIf(role -> removeNames.contains(normalizeName(role.getName())));
        }
        if (!addNames.isEmpty()) {
            for (String roleName : addNames) {
                updated.add(resolvedRoles.get(roleName));
            }
        }

        user.setRoles(updated);
        User saved = userRepository.save(user);

        return new UserRolesResponse(saved.getId(), saved.getUsername(), toSortedRoleNames(saved));
    }

    @Transactional
    public RoleResponse replaceRolePermissions(String roleName, RolePermissionsRequest request) {
        String normalizedRoleName = normalizeName(roleName);
        Role role = roleRepository.findByName(normalizedRoleName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

        Set<String> codes = normalizePermissionCodes(request == null ? null : request.getPermissions());
        List<Permission> permissions = codes.isEmpty()
                ? List.of()
                : permissionRepository.findByCodeIn(codes);

        if (permissions.size() != codes.size()) {
            Set<String> foundCodes = permissions.stream()
                    .map(Permission::getCode)
                    .collect(Collectors.toSet());
            Set<String> missing = new HashSet<>(codes);
            missing.removeAll(foundCodes);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Permissions not found: " + String.join(", ", missing));
        }

        role.setPermissions(new HashSet<>(permissions));
        Role saved = roleRepository.save(role);
        return new RoleResponse(saved.getName(), toSortedPermissionCodes(saved));
    }

    private Map<String, Role> resolveRolesByName(Set<String> roleNames) {
        if (roleNames.isEmpty()) {
            return Map.of();
        }

        List<Role> roles = roleRepository.findByNameIn(roleNames);
        Map<String, Role> resolved = new HashMap<>();
        for (Role role : roles) {
            resolved.put(normalizeName(role.getName()), role);
        }
        return resolved;
    }

    private void removeMissingRoles(Set<String> requestedNames, Map<String, Role> resolved) {
        if (requestedNames.isEmpty()) {
            return;
        }

        Set<String> missing = new HashSet<>(requestedNames);
        missing.removeAll(resolved.keySet());
        if (!missing.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Roles not found: " + String.join(", ", missing));
        }
    }

    private Set<String> normalizeNames(List<String> names) {
        if (names == null) {
            return Set.of();
        }

        return names.stream()
                .filter(name -> name != null && !name.isBlank())
                .map(this::normalizeName)
                .collect(Collectors.toSet());
    }

    private Set<String> normalizePermissionCodes(List<String> codes) {
        if (codes == null) {
            return Set.of();
        }

        return codes.stream()
                .filter(code -> code != null && !code.isBlank())
                .map(String::trim)
                .collect(Collectors.toSet());
    }

    private String normalizeName(String value) {
        return value == null ? "" : value.trim().toUpperCase(Locale.ROOT);
    }

    private List<String> toSortedPermissionCodes(Role role) {
        return role.getPermissions().stream()
                .map(Permission::getCode)
                .sorted()
                .toList();
    }

    private List<String> toSortedRoleNames(User user) {
        return user.getRoles().stream()
                .map(Role::getName)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }
}
