package co.edu.uptc.ms_suppliers.dto;

import java.util.UUID;

import co.edu.uptc.ms_suppliers.model.Supplier;

public class SupplierResponse {

    private UUID id;
    private String nit;
    private String name;
    private String email;
    private String phone;
    private Boolean is_active;

    public SupplierResponse() {
    }

    public SupplierResponse(UUID id, String nit, String name, String email, String phone, Boolean is_active) {
        this.id = id;
        this.nit = nit;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.is_active = is_active;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getIsActive() {
        return is_active;
    }

    public void setIsActive(Boolean is_active) {
        this.is_active = is_active;
    }

    public static SupplierResponse fromModel(Supplier supplier) {
        if (supplier == null) {
            return null;
        }
        return new SupplierResponse(
                supplier.getId(),
                supplier.getNit(),
                supplier.getName(),
                supplier.getEmail(),
                supplier.getPhone(),
                supplier.getIsActive()
        );
    }
}
