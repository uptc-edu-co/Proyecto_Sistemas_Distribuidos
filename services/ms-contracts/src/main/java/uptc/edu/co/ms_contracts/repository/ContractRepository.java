package uptc.edu.co.ms_contracts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uptc.edu.co.ms_contracts.model.Contract;

public interface ContractRepository extends JpaRepository<Contract, Long> {}