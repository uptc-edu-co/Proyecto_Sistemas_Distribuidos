package uptc.edu.co.ms_auth.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uptc.edu.co.ms_auth.auth.model.User;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByUsername(String username);

    boolean existsByEmailIgnoreCase(String email);

    @Query("""
           select distinct p.code
           from User u
           join u.roles r
           join r.permissions p
           where u.username = :username
           """)
    Set<String> findScopesByUsername(String username);
}
