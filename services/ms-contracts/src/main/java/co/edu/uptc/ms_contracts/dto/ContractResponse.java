package co.edu.uptc.ms_contracts.dto;

import co.edu.uptc.ms_contracts.model.Contract;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class ContractResponse {

    private UUID uuid;
    private Long contractNumber;
    private UUID supplierId;
    private String subject;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal budget;
    private String status;
    private Integer version;

    public static ContractResponse fromModel(Contract c) {
        ContractResponse r = new ContractResponse();
        r.uuid = c.getUuid();
        r.contractNumber = c.getContractNumber();
        r.supplierId = c.getSupplierId();
        r.subject = c.getSubject();
        r.startDate = c.getStartDate();
        r.endDate = c.getEndDate();
        r.budget = c.getBudget();
        r.status = c.getStatus();
        r.version = c.getVersion();
        return r;
    }

    public UUID getUuid() { return uuid; }
    public Long getContractNumber() { return contractNumber; }
    public UUID getSupplierId() { return supplierId; }
    public String getSubject() { return subject; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public BigDecimal getBudget() { return budget; }
    public String getStatus() { return status; }
    public Integer getVersion() { return version; }
}