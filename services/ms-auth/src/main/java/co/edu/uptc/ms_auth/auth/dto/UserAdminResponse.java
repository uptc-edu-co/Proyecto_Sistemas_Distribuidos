package co.edu.uptc.ms_auth.auth.dto;

import java.util.List;

public class UserAdminResponse {

    private Long id;
    private String username;
    private String email;
    private boolean active;
    private List<String> roles;

    public UserAdminResponse() {
    }

    public UserAdminResponse(Long id, String username, String email, boolean active, List<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.active = active;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
