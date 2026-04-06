package co.edu.uptc.ms_suppliers.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uptc.ms_suppliers.model.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, UUID> {
    boolean existsByNit(String nit);
}
