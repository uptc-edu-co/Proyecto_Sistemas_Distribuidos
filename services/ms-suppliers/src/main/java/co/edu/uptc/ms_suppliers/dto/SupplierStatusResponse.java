package co.edu.uptc.ms_suppliers.dto;

import java.util.UUID;

public class SupplierStatusResponse {

    private UUID id;
    private String nit;
    private boolean active;
    private String message;

    public SupplierStatusResponse() {
    }

    public SupplierStatusResponse(UUID id, String nit, boolean active, String message) {
        this.id = id;
        this.nit = nit;
        this.active = active;
        this.message = message;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
