package co.edu.uptc.ms_contracts.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.uptc.ms_contracts.model.Contract;

public interface ContractRepository extends JpaRepository<Contract, UUID> {

    Optional<Contract> findTopByContractNumberOrderByVersionDesc(Long contractNumber);
}