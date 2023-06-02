package nl.tudelft.sem.template.authentication.domain.user.entity;

import java.util.Objects;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.tudelft.sem.template.authentication.domain.HasEvents;
import nl.tudelft.sem.template.authentication.domain.user.converter.HashedPasswordAttributeConverter;

/**
 * A DDD entity representing an application user in our domain.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class AppUser extends HasEvents {
    /**
     * Identifier for the application user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    @Convert(converter = HashedPasswordAttributeConverter.class)
    private HashedPassword password;

    /**
     * Create a new application user.
     *
     * @param username - the username of the user
     * @param password - the password of the user
     */
    public AppUser(String username, HashedPassword password) {
        this.username = username;
        this.password = password;
    }


    /**
     * Equality is only based on the identifier.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AppUser appUser = (AppUser) o;
        return Objects.equals(id, appUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
