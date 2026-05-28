package uptc.edu.co.ms_auth.auth.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uptc.shared.security.annotations.RequiresScope;
import uptc.edu.co.ms_auth.auth.dto.RoleAssignmentRequest;
import uptc.edu.co.ms_auth.auth.dto.RolePermissionsRequest;
import uptc.edu.co.ms_auth.auth.dto.RoleResponse;
import uptc.edu.co.ms_auth.auth.dto.UserRolesResponse;
import uptc.edu.co.ms_auth.auth.service.AdminRoleService;

@RestController
@RequestMapping("/auth")
public class AdminRoleController {

    private final AdminRoleService adminRoleService;

    public AdminRoleController(AdminRoleService adminRoleService) {
        this.adminRoleService = adminRoleService;
    }

    @GetMapping("/roles")
    @RequiresScope("admin:roles")
    public List<RoleResponse> listRoles() {
        return adminRoleService.listRoles();
    }

    @PutMapping("/users/{id}/roles")
    @RequiresScope("admin:roles")
    public UserRolesResponse updateUserRoles(@PathVariable("id") Long id,
                                             @RequestBody RoleAssignmentRequest request) {
        return adminRoleService.updateUserRoles(id, request);
    }

    @PutMapping("/roles/{role}/permissions")
    @RequiresScope("admin:roles")
    public RoleResponse replaceRolePermissions(@PathVariable("role") String role,
                                               @Valid @RequestBody RolePermissionsRequest request) {
        return adminRoleService.replaceRolePermissions(role, request);
    }
}
