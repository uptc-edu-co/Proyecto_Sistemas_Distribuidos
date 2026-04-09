package uptc.edu.co.ms_auth.auth.dto;

public class RegisterResponse {

    private Long id;
    private String username;
    private boolean active;

    public RegisterResponse() {
    }

    public RegisterResponse(Long id, String username, boolean active) {
        this.id = id;
        this.username = username;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean isActive() {
        return active;
    }
}
