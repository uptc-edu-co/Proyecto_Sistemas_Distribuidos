package co.edu.uptc.ms_auth.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.uptc.ms_auth.auth.model.Permission;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByCode(String code);

    List<Permission> findByCodeIn(Set<String> codes);
}
