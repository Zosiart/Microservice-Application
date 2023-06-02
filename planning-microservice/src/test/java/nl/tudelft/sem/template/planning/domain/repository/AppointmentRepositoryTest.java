package nl.tudelft.sem.template.planning.domain.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Optional;
import nl.tudelft.sem.template.planning.domain.entity.Appointment;
import nl.tudelft.sem.template.planning.domain.enums.Role;
import nl.tudelft.sem.template.planning.domain.enums.Status;
import nl.tudelft.sem.template.planning.domain.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AppointmentRepositoryTest {

    @Autowired
    AppointmentRepository appointmentRepository;

    /**
     * Set up the test environment.
     */
    @BeforeEach
    public void init() {
        // Create some appointments and save them to the database
        Appointment appointment1 = new Appointment(1, 2, Role.Cox);
        appointment1.setStatus(Status.ACCEPTED);

        Appointment appointment2 = new Appointment(1, 3, Role.Cox);
        appointment2.setStatus(Status.PENDING);

        Appointment appointment3 = new Appointment(2, 4, Role.Coach);
        appointment3.setStatus(Status.DECLINED);

        Appointment appointment4 = new Appointment(2, 2, Role.ScullingRower);
        appointment4.setStatus(Status.PENDING);

        Appointment appointment5 = new Appointment(3, 4, Role.StarboardSideRower);
        appointment5.setStatus(Status.ACCEPTED);

        Appointment appointment6 = new Appointment(3, 5, Role.StarboardSideRower);
        appointment6.setStatus(Status.DECLINED);

        Appointment appointment7 = new Appointment(3, 2, Role.Coach);
        appointment7.setStatus(Status.PENDING);

        appointmentRepository.saveAll(Arrays.asList(appointment1, appointment2, appointment3, appointment4,
                appointment5, appointment6, appointment7));
    }


    @Test
    public void testFindById() {
        // Create a new appointment and save it to the database
        Appointment appointment = new Appointment(1, 2, Role.Coach);
        appointmentRepository.save(appointment);

        // Retrieve the appointment by its id
        Optional<Appointment> foundAppointment = appointmentRepository.findById(appointment.getId());

        // Ensure that the appointment was correctly retrieved
        assertTrue(foundAppointment.isPresent());
        assertEquals(appointment, foundAppointment.get());
    }

    @Test
    public void testCountStatusForActivityRole_ValidActivityAndRole() {
        // Count the number of appointments with activity id 1 and role COX
        int count = appointmentRepository.countStatusForActivityRole(1, Role.Cox, Status.ACCEPTED);

        // Ensure that the correct number of appointments was returned
        assertEquals(1, count);
    }

    @Test
    public void testCountStatusForActivityRole_InvalidActivityId() {
        // Count the number of appointments with activity id 99 and role COX
        int count = appointmentRepository.countStatusForActivityRole(99, Role.Cox, Status.PENDING);

        // Ensure that the correct number of appointments was returned
        assertEquals(0, count);
    }

    @Test
    public void testCountStatusForActivityRole_InvalidRole() {
        // Count the number of appointments with activity id 1 and role INVALID_ROLE
        int count = appointmentRepository.countStatusForActivityRole(2, Role.ScullingRower, Status.DECLINED);

        // Ensure that the correct number of appointments was returned
        assertEquals(0, count);
    }

    @Test
    public void testUserHasStatusForActivity_ValidActivityUserAndRole() {
        // Check whether user 2 has an appointment with status ACCEPTED for activity id 1
        boolean hasAppointment = appointmentRepository.userHasStatusForActivity(2, 1, Status.ACCEPTED);

        // Ensure that the correct result was returned
        assertTrue(hasAppointment);
    }

    @Test
    public void testUserHasStatusForActivity_InvalidUserId() {
        // Check whether user 99 has an appointment with status ACCEPTED for activity id 1
        boolean hasAppointment = appointmentRepository.userHasStatusForActivity(99, 1, Status.ACCEPTED);

        // Ensure that the correct result was returned
        assertFalse(hasAppointment);
    }

    @Test
    public void testUserHasStatusForActivity_InvalidActivityId() {
        // Check whether user 2 has an appointment with status ACCEPTED for activity id 99
        boolean hasAppointment = appointmentRepository.userHasStatusForActivity(2, 99, Status.ACCEPTED);

        // Ensure that the correct result was returned
        assertFalse(hasAppointment);
    }

    @Test
    public void testUserHasStatusForActivity_InvalidStatus() {
        // Check whether user 2 has an appointment with status INVALID_STATUS for activity id 1
        boolean hasAppointment = appointmentRepository.userHasStatusForActivity(2, 1, Status.DECLINED);

        // Ensure that the correct result was returned
        assertFalse(hasAppointment);
    }

    @Test
    public void testUserHasAppointmentForActivityRole_ValidActivityUserAndRole() {
        // Check whether user 2 has an appointment for activity id 1 and role COX
        boolean hasAppointment = appointmentRepository.userHasAppointmentForActivityRole(2, 1, Role.Cox);

        // Ensure that the correct result was returned
        assertTrue(hasAppointment);
    }

    @Test
    public void testUserHasAppointmentForActivityRole_InvalidUserId() {
        // Check whether user 99 has an appointment for activity id 1 and role COX
        boolean hasAppointment = appointmentRepository.userHasAppointmentForActivityRole(99, 1, Role.Cox);

        // Ensure that the correct result was returned
        assertFalse(hasAppointment);
    }

    @Test
    public void testUserHasAppointmentForActivityRole_InvalidActivityId() {
        // Check whether user 2 has an appointment for activity id 99 and role COX
        boolean hasAppointment = appointmentRepository.userHasAppointmentForActivityRole(2, 99, Role.Cox);

        // Ensure that the correct result was returned
        assertFalse(hasAppointment);
    }

    @Test
    public void testUserHasAppointmentForActivityRole_InvalidRole() {
        // Check whether user 2 has an appointment for activity id 1 and role INVALID_ROLE
        boolean hasAppointment = appointmentRepository.userHasAppointmentForActivityRole(2, 1, Role.ScullingRower);

        // Ensure that the correct result was returned
        assertFalse(hasAppointment);
    }
}
