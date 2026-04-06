package co.edu.uptc.ms_suppliers.dto;

import co.edu.uptc.ms_suppliers.model.Supplier;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;

public class UpdateSupplierRequest {
    @Nullable
    private String name;

    @Email
    @Nullable
    private String email;

    @Nullable
    private String phone;

    @Nullable
    private Boolean is_active;

    public UpdateSupplierRequest() {
    }

    public UpdateSupplierRequest(String name, String email, String phone, Boolean is_active) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.is_active = is_active;
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
        supplier.setName(this.name);
        supplier.setEmail(this.email);
        supplier.setPhone(this.phone);
        supplier.setIsActive(this.is_active);
        return supplier;
    }
}
