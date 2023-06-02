package nl.tudelft.sem.template.user.domain;


import java.util.List;
import nl.tudelft.sem.template.user.domain.entity.Availability;
import nl.tudelft.sem.template.user.domain.entity.Email;
import nl.tudelft.sem.template.user.domain.entity.RoleInfo;
import nl.tudelft.sem.template.user.domain.entity.User;
import nl.tudelft.sem.template.user.domain.enums.Certificate;
import nl.tudelft.sem.template.user.domain.enums.Gender;

public interface Builder {
    Builder setEmail(Email email);

    Builder setName(String name);

    Builder setGender(Gender gender);

    Builder setRoles(List<RoleInfo> roles);

    Builder setSchedule(List<Availability> schedule);

    Builder setCertificate(Certificate certificate);

    Builder setOrganization(String organization);

    User build();
}
