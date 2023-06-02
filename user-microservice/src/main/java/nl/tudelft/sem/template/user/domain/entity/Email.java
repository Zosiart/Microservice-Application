package nl.tudelft.sem.template.user.domain.entity;

import java.util.Objects;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.user.domain.exceptions.EmailFormatIncorrectException;

@NoArgsConstructor
public class Email {
    private String emailValue;

    /**
     * Constructs an Email object with the provided email address. If the email address is not in a
     * valid format, an {@link EmailFormatIncorrectException} is thrown.
     *
     * @param email the email address
     * @throws EmailFormatIncorrectException if the email address is not in a valid format
     */
    public Email(String email) throws EmailFormatIncorrectException {
        if (validateEmail(email)) {
            this.emailValue = email;
        } else {
            throw new EmailFormatIncorrectException("Email format unsupported");
        }
    }

    /**
     * using the RegEx code found here:
     * https://regexr.com/3e48o
     *
     * @param email - the value we are trying to set for the email
     * @return true iff the format of the email is valid
     */
    private boolean validateEmail(String email) {
        if (email == null) {
            return false;
        }
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    public String getEmailText() {
        return this.emailValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Email email1 = (Email) o;
        return Objects.equals(emailValue, email1.emailValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailValue);
    }
}
