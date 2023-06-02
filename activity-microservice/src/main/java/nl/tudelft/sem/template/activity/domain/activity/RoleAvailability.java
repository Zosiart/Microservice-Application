package nl.tudelft.sem.template.activity.domain.activity;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleAvailability {
    private Role role;
    private int requiredCount;

    public RoleAvailability(Role role, int requiredCount) {
        this.role = role;
        this.requiredCount = requiredCount;
    }

    @Override
    public String toString() {
        return role + ", " + requiredCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleAvailability roleAvailability = (RoleAvailability) o;
        return requiredCount == roleAvailability.requiredCount && role == roleAvailability.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(role, requiredCount);
    }
}
