package uptc.edu.co.ms_auth.auth.dto;

import jakarta.validation.constraints.NotNull;

public class UserStatusRequest {

    @NotNull
    private Boolean active;

    public UserStatusRequest() {
    }

    public UserStatusRequest(Boolean active) {
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
