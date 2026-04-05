package uptc.edu.co.ms_suppliers.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uptc.edu.co.ms_suppliers.model.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, UUID> {
}
