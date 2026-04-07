package co.edu.uptc.ms_contracts.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class CreateContractRequest {

    @NotNull
    @JsonAlias("idProveedor")
    private UUID supplierId;

    @NotNull
    @JsonAlias("objeto")
    private String subject;

    @NotNull
    @JsonAlias("fechaInicio")
    private LocalDate startDate;

    @NotNull
    @JsonAlias("fechaFin")
    private LocalDate endDate;

    @NotNull
    @PositiveOrZero
    @JsonAlias("presupuesto")
    private BigDecimal budget;

    public UUID getSupplierId() { return supplierId; }
    public String getSubject() { return subject; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public BigDecimal getBudget() { return budget; }

    public void setSupplierId(UUID supplierId) { this.supplierId = supplierId; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }
}