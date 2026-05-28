package uptc.edu.co.ms_auth.auth.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public class RolePermissionsRequest {

    @NotNull
    private List<String> permissions;

    public RolePermissionsRequest() {
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
