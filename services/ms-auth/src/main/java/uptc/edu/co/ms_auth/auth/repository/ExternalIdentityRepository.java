package uptc.edu.co.ms_auth.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import uptc.edu.co.ms_auth.auth.model.ExternalIdentity;

public interface ExternalIdentityRepository extends JpaRepository<ExternalIdentity, Long> {

    Optional<ExternalIdentity> findByProviderAndProviderUserId(String provider, String providerUserId);
}
