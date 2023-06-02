package nl.tudelft.sem.template.user.domain;


import java.util.List;
import nl.tudelft.sem.template.user.domain.entity.Availability;
import nl.tudelft.sem.template.user.domain.entity.Email;
import nl.tudelft.sem.template.user.domain.entity.RoleInfo;
import nl.tudelft.sem.template.user.domain.entity.User;
import nl.tudelft.sem.template.user.domain.enums.Certificate;
import nl.tudelft.sem.template.user.domain.enums.Gender;

public class BuilderDirector {

    /**
     * Constructs a basic {@link User} object using the provided builder and name and gender.
     *
     * @param builder    the builder to use for constructing the {@link User} object
     * @param name       the name of the user
     * @param genderEnum the gender of the user
     * @return a basic {@link User} object
     */
    public User constructBasicUser(Builder builder, String name, Gender genderEnum) {
        return builder.setName(name)
            .setGender(genderEnum)
            .build();
    }

    /**
     * Constructs a complete {@link User} object using the provided builder and name, gender, email,
     * role information, schedule, and certificate.
     *
     * @param builder          the builder to use for constructing the {@link User} object
     * @param name             the name of the user
     * @param genderEnum       the gender of the user
     * @param email            the email of the user
     * @param roleInfoList     the list of roles for the user
     * @param availabilityList the list of availabilities for the user's schedule
     * @param certificateEnum  the certificate of the user
     * @return a complete {@link User} object
     */
    public User constructCompleteUser(Builder builder, String name,
                                      Gender genderEnum, Email email, List<RoleInfo> roleInfoList,
                                      List<Availability> availabilityList, Certificate certificateEnum,
                                      String organization) {
        return builder
            .setName(name)
            .setGender(genderEnum)
            .setEmail(email)
            .setRoles(roleInfoList)
            .setSchedule(availabilityList)
            .setCertificate(certificateEnum)
            .setOrganization(organization)
            .build();
    }
}
