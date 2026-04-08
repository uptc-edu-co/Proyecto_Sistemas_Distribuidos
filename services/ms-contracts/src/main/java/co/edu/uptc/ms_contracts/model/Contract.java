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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "contract_number", nullable = false, unique = true)
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

    @PrePersist
    private void prePersist() {
        this.version = 1;
        this.status = "In preparation";
    }
}