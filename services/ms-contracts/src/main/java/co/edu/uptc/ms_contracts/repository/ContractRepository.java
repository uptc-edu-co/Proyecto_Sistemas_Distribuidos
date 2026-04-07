package co.edu.uptc.ms_contracts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.uptc.ms_contracts.model.Contract;

public interface ContractRepository extends JpaRepository<Contract, Long> {}