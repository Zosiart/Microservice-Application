package nl.tudelft.sem.template.user.domain;


import java.util.List;
import nl.tudelft.sem.template.user.domain.entity.Availability;
import nl.tudelft.sem.template.user.domain.entity.Email;
import nl.tudelft.sem.template.user.domain.entity.RoleInfo;
import nl.tudelft.sem.template.user.domain.entity.User;
import nl.tudelft.sem.template.user.domain.enums.Certificate;
import nl.tudelft.sem.template.user.domain.enums.Gender;
import nl.tudelft.sem.template.user.domain.exceptions.UsernameFormatIncorrectException;

public class UserBuilder implements Builder {
    private transient String name;
    private transient Email email;
    private transient Gender gender;
    private transient List<RoleInfo> roles;
    private transient List<Availability> availabilities;
    private transient Certificate certificateEnum;
    private transient String organization;

    public Builder setEmail(Email email) {
        this.email = email;
        return this;
    }

    public Builder setName(String name) {
        this.name = name;
        return this;
    }

    public Builder setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public Builder setRoles(List<RoleInfo> roles) {
        this.roles = roles;
        return this;
    }

    public Builder setSchedule(List<Availability> schedule) {
        this.availabilities = schedule;
        return this;
    }

    public Builder setCertificate(Certificate certificate) {
        this.certificateEnum = certificate;
        return this;
    }

    @Override
    public Builder setOrganization(String organization) {
        this.organization = organization;
        return this;
    }

    /**
     * Builds a user.
     *
     * @return the user
     */
    public User build()  {
        try {
            return new User(name, email, gender, roles, availabilities, certificateEnum, organization);
        } catch (UsernameFormatIncorrectException e) {
            System.out.println("Username did not pass the validity checks.");
            return null;
        }
    }

}
