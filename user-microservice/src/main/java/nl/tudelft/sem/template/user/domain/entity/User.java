package nl.tudelft.sem.template.user.domain.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import nl.tudelft.sem.template.user.domain.attribute.converters.*;
import nl.tudelft.sem.template.user.domain.enums.Certificate;
import nl.tudelft.sem.template.user.domain.enums.Gender;
import nl.tudelft.sem.template.user.domain.exceptions.UsernameFormatIncorrectException;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "Name", nullable = false)
    private String name;

    @NonNull
    @Column(name = "Gender", nullable = false)
    @Convert(converter = GenderAttributeConverter.class)
    private Gender gender;

    @Column(name = "Email")
    @Convert(converter = EmailAttributeConverter.class)
    private Email email;

    @Column(name = "Roles")
    @Convert(converter = RoleInfoAttributeConverter.class)
    private List<RoleInfo> roles;

    @Column(name = "Dates")
    @Convert(converter = AvailabilitiesAttributeConverter.class)
    private List<Availability> availabilities;

    @Column(name = "Certificate")
    @Convert(converter = CertificateAttributeConverter.class)
    private Certificate certificate;

    @Column(name = "Organization")
    private String organization;

    /**
     * Constructs a basic User object with the provided name and gender. The email, roles, and
     * availabilities are set to default values.
     *
     * @param name   the name of the user
     * @param gender the gender of the user
     */
    public User(String name, Gender gender) {
        this.name = name;
        this.gender = gender;
        this.email = new Email();
        this.roles = new ArrayList<>();
        this.availabilities = new ArrayList<>();
    }

    /**
     * Constructs a complete User object with the provided name, email, gender, roles, availabilities,
     * and certificate.
     *
     * @param name           the name of the user
     * @param email          the email of the user
     * @param gender         the gender of the user
     * @param roles          the list of roles for the user
     * @param availabilities the list of availabilities for the user
     * @param certificate    the certificate of the user
     */
    public User(String name, Email email, Gender gender, List<RoleInfo> roles, List<Availability> availabilities,
                Certificate certificate, String organization) throws UsernameFormatIncorrectException {
        if (!validUsername(name)) {
            throw new UsernameFormatIncorrectException("Username must be between 8 and 20 characters, only contain "
                    + "alphanumeric characters, underscores and dots, and only start and end in "
                    + "alphanumeric characters.");
        }
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.roles = roles;
        this.availabilities = availabilities;
        this.certificate = certificate;
        this.organization = organization;
    }

    /**
     * Username must be between 8 and 20 characters, only contain alphanumeric characters, underscores and dots,
     * and only start and end in alphanumeric characters.
     *
     * @param userName the username to be checked against these rules
     * @return true iff the username is valid
     */
    public static boolean validUsername(String userName) {
        if (userName == null || userName.length() < 8 || userName.length() > 20) {
            return false;
        }

        return userName.matches("^[a-zA-Z0-9]+([._]?[a-zA-Z0-9]+)*$");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("User{");
        if (name != null) {
            sb.append("name='").append(name).append('\'');
        }
        if (email != null && email.getEmailText() != null) {
            sb.append(", email=").append(email.getEmailText());
        }
        if (gender != null) {
            sb.append(", gender=").append(gender.name());
        }
        int size;
        size = this.roles.size();
        if (this.roles != null && this.roles.size() > 0) {
            sb.append(", roles=");
            size--;
            for (RoleInfo role : this.roles) {
                sb.append(role.toString());
                if (size > 0) {
                    sb.append(", ");
                }
            }
        }
        size = this.availabilities.size();
        if (availabilities != null && availabilities.size() > 0) {
            sb.append(" available on:");
            for (Availability a : this.availabilities) {
                size--;
                sb.append(a.toString());
                if (size > 0) {
                    sb.append(", ");
                }
            }
        }
        if (organization != null) {
            sb.append(", organization=").append(organization);
        }
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(gender, user.gender)
            && Objects.equals(email, user.email) && Objects.equals(roles, user.roles)
            && Objects.equals(availabilities, user.availabilities) && certificate == user.certificate
            && Objects.equals(organization, user.organization);
    }

}
