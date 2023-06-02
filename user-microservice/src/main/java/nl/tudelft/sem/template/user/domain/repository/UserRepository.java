package nl.tudelft.sem.template.user.domain.repository;

import java.util.Optional;
import nl.tudelft.sem.template.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserById(Long id);

    boolean existsUserById(Long id);
}
