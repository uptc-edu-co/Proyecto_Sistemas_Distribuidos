package co.edu.uptc.ms_suppliers.dto;

import co.edu.uptc.ms_suppliers.model.Supplier;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class SupplierRequest {

    @NotBlank
    private String nit;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone;

    private Boolean is_active = true;

    public SupplierRequest() {
    }

    public SupplierRequest(String nit, String name, String email, String phone, Boolean is_active) {
        this.nit = nit;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.is_active = is_active;
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

    public Boolean isActive() {
        return is_active;
    }

    public void setIsActive(Boolean isActive) {
        this.is_active = isActive;
    }

    public Supplier toModel() {
        Supplier supplier = new Supplier();
        supplier.setNit(this.nit);
        supplier.setName(this.name);
        supplier.setEmail(this.email);
        supplier.setPhone(this.phone);
        supplier.setIsActive(this.is_active != null ? this.is_active : true);
        return supplier;
    }
}
