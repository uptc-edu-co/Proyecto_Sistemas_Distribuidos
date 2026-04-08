package uptc.edu.co.ms_auth.auth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uptc.edu.co.ms_auth.auth.model.Permission;
import uptc.edu.co.ms_auth.auth.model.Role;
import uptc.edu.co.ms_auth.auth.repository.PermissionRepository;
import uptc.edu.co.ms_auth.auth.repository.RoleRepository;

import java.util.Set;

/**
 * Seeds the database with base roles and permissions on startup if they don't exist.
 *
 * Roles created:
 *   - USER      (no extra permissions)
 *   - AUDITOR   (audit:read)
 *   - ADMIN     (audit:read)
 *
 * Permissions created:
 *   - audit:read  -> allows reading audit events
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    public DataInitializer(PermissionRepository permissionRepository, RoleRepository roleRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // --- Permissions ---
        Permission auditRead = permissionRepository.findByCode("view:audit")
                .orElseGet(() -> {
                    log.info("Creating permission: view:audit");
                    return permissionRepository.save(new Permission("view:audit"));
                });

        // --- Roles ---
        roleRepository.findByName("USER").orElseGet(() -> {
            log.info("Creating role: USER");
            return roleRepository.save(new Role("USER"));
        });

        roleRepository.findByName("AUDITOR").orElseGet(() -> {
            log.info("Creating role: AUDITOR");
            Role auditor = new Role("AUDITOR");
            auditor.setPermissions(Set.of(auditRead));
            return roleRepository.save(auditor);
        });

        roleRepository.findByName("ADMIN").orElseGet(() -> {
            log.info("Creating role: ADMIN");
            Role admin = new Role("ADMIN");
            admin.setPermissions(Set.of(auditRead));
            return roleRepository.save(admin);
        });

        log.info("DataInitializer: roles and permissions are ready.");
    }
}
