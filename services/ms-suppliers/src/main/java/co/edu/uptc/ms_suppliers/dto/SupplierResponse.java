package co.edu.uptc.ms_suppliers.dto;

import java.util.UUID;

import co.edu.uptc.ms_suppliers.model.Supplier;

public class SupplierResponse {

    private UUID id;
    private String nit;
    private String nombre;
    private String email;
    private String telefono;
    private Boolean estado;

    public SupplierResponse() {
    }

    public SupplierResponse(UUID id, String nit, String nombre, String email, String telefono, Boolean estado) {
        this.id = id;
        this.nit = nit;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.estado = estado;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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
