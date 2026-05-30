package co.edu.uptc.ms_auth.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.uptc.ms_auth.auth.model.ExternalIdentity;

public interface ExternalIdentityRepository extends JpaRepository<ExternalIdentity, Long> {

    Optional<ExternalIdentity> findByProviderAndProviderUserId(String provider, String providerUserId);
}
