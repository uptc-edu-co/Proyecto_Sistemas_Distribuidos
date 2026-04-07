package co.edu.uptc.ms_contracts.client.dto;

import java.util.UUID;

public class SupplierClientResponse {

    private UUID id;
    private String nit;
    private String name;
    private String email;
    private String phone;
    private Boolean is_active;

    public UUID getId() { return id; }
    public String getNit() { return nit; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public Boolean getIsActive() { return is_active; }

    public void setId(UUID id) { this.id = id; }
    public void setNit(String nit) { this.nit = nit; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setIsActive(Boolean is_active) { this.is_active = is_active; }
}