package uptc.edu.co.ms_contracts.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class CreateContractRequest {

    @NotNull
    private UUID idProveedor;

    @NotNull
    private String objeto;

    @NotNull
    private LocalDate fechaInicio;

    @NotNull
    private LocalDate fechaFin;

    @NotNull
    @PositiveOrZero
    private BigDecimal presupuesto;

    public UUID getIdProveedor() { return idProveedor; }
    public String getObjeto() { return objeto; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public BigDecimal getPresupuesto() { return presupuesto; }

    public void setIdProveedor(UUID idProveedor) { this.idProveedor = idProveedor; }
    public void setObjeto(String objeto) { this.objeto = objeto; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public void setPresupuesto(BigDecimal presupuesto) { this.presupuesto = presupuesto; }
}