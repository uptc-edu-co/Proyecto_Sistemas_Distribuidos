package uptc.edu.co.ms_contracts.dto;

import uptc.edu.co.ms_contracts.model.Contract;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class ContractResponse {

    private UUID uuid;
    private Long numContrato;
    private UUID idProveedor;
    private String objeto;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal presupuesto;
    private String estado;
    private Integer version;

    public static ContractResponse fromModel(Contract c) {
        ContractResponse r = new ContractResponse();
        r.uuid = c.getUuid();
        r.numContrato = c.getNumContrato();
        r.idProveedor = c.getIdProveedor();
        r.objeto = c.getObjeto();
        r.fechaInicio = c.getFechaInicio();
        r.fechaFin = c.getFechaFin();
        r.presupuesto = c.getPresupuesto();
        r.estado = c.getEstado();
        r.version = c.getVersion();
        return r;
    }

    public UUID getUuid() { return uuid; }
    public Long getNumContrato() { return numContrato; }
    public UUID getIdProveedor() { return idProveedor; }
    public String getObjeto() { return objeto; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public BigDecimal getPresupuesto() { return presupuesto; }
    public String getEstado() { return estado; }
    public Integer getVersion() { return version; }
}