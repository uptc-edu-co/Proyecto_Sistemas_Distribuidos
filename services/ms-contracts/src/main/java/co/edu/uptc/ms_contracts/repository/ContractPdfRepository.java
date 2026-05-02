package co.edu.uptc.ms_contracts.repository;

import co.edu.uptc.ms_contracts.model.ContractPdf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContractPdfRepository extends JpaRepository<ContractPdf, UUID> {
}
