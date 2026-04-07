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

    @Column(name = "num_contrato", nullable = false, unique = true)
    private Long contractNumber;

    @Column(name = "id_proveedor", nullable = false, columnDefinition = "UUID")
    private UUID supplierId;

    @Column(nullable = false, length = 200)
    private String subject;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate startDate;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal budget;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(nullable = false)
    private Integer version;

    @PrePersist
    private void prePersist() {
        this.uuid = UUID.randomUUID();
        this.version = 1;
        this.status = "In preparation";
    }
}