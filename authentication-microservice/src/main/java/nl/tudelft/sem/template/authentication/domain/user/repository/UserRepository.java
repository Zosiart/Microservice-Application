package nl.tudelft.sem.template.authentication.domain.user.repository;

import java.util.Optional;
import nl.tudelft.sem.template.authentication.domain.user.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A DDD repository for querying and persisting user aggregate roots.
 */
@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    /**
     * Find user by NetID.
     */
    Optional<AppUser> findByUsername(String username);

    /**
     * Check if an existing user already uses a NetID.
     */
    boolean existsByUsername(String username);
}
