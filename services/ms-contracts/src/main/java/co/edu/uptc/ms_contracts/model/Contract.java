package co.edu.uptc.ms_contracts.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "contracts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @Column(name = "contract_number", nullable = false)
    private Long contractNumber;

    @Column(name = "supplier_id", nullable = false, columnDefinition = "UUID")
    private UUID supplierId;

    @Column(nullable = false, length = 200)
    private String subject;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal budget;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(nullable = false)
    private Integer version;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @PrePersist
    private void prePersist() {
        this.uuid = UUID.randomUUID();
        this.version = 1;
        this.status = "In preparation";
        this.isActive = true;
    }
}