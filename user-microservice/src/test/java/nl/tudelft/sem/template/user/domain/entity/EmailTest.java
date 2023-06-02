package nl.tudelft.sem.template.user.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import nl.tudelft.sem.template.user.domain.exceptions.EmailFormatIncorrectException;
import org.junit.jupiter.api.Test;

class EmailTest {
    Email email;

    @Test
    public void noExtension() {
        assertThrows(EmailFormatIncorrectException.class, () -> new Email("abc@yahoo"));
    }

    @Test
    public void noDomain() {
        assertThrows(EmailFormatIncorrectException.class, () -> new Email("abc.com"));
    }

    @Test
    public void noName() {
        assertThrows(EmailFormatIncorrectException.class, () -> new Email("@gmail.com"));
    }

    @Test
    public void validEmail() throws EmailFormatIncorrectException {
        email = new Email("email@gmail.com");
        assertEquals("email@gmail.com", email.getEmailText());
    }

    @Test
    public void nullEmailIsInvalid() {
        String s = null;
        assertThrows(EmailFormatIncorrectException.class, () ->  new Email(s));
    }

    @Test
    public void equalEmails() throws EmailFormatIncorrectException {
        Email e1 = new Email("abcd@yahoo.com");
        Email e2 = new Email("abcd@yahoo.com");
        assertTrue(e1.equals(e1));
        assertTrue(e1.equals(e2));
    }

    @Test
    public void differentEmail() throws EmailFormatIncorrectException {
        Email e1 = new Email("abcd@yahoo.com");
        Email e2 = new Email("abcd@gmail.com");
        assertFalse(e1.equals(null));
        assertFalse(e1.equals(e2));
        assertFalse(e1.equals(new User()));
    }
}