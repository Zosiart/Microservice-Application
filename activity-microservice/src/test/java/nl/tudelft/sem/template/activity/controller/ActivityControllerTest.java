package nl.tudelft.sem.template.activity.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import nl.tudelft.sem.template.activity.authentication.AuthManager;
import nl.tudelft.sem.template.activity.authentication.JwtTokenVerifier;
import nl.tudelft.sem.template.activity.domain.activity.Activity;
import nl.tudelft.sem.template.activity.domain.activity.Availability;
import nl.tudelft.sem.template.activity.domain.activity.Certificate;
import nl.tudelft.sem.template.activity.domain.activity.Competition;
import nl.tudelft.sem.template.activity.domain.activity.Gender;
import nl.tudelft.sem.template.activity.domain.activity.Level;
import nl.tudelft.sem.template.activity.domain.activity.Role;
import nl.tudelft.sem.template.activity.domain.activity.RoleAvailability;
import nl.tudelft.sem.template.activity.domain.activity.RoleInfo;
import nl.tudelft.sem.template.activity.domain.activity.Training;
import nl.tudelft.sem.template.activity.dto.ActivityRequest;
import nl.tudelft.sem.template.activity.dto.ActivityResponse;
import nl.tudelft.sem.template.activity.dto.UserResponse;
import nl.tudelft.sem.template.activity.exception.ActivityNotFoundException;
import nl.tudelft.sem.template.activity.exception.ActivityTypeMismatchException;
import nl.tudelft.sem.template.activity.exception.InvalidActivityException;
import nl.tudelft.sem.template.activity.exception.InvalidUserException;
import nl.tudelft.sem.template.activity.exception.WrongDateException;
import nl.tudelft.sem.template.activity.service.ActivityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test", "mockTokenVerifier", "mockAuthenticationManager"})
@AutoConfigureMockMvc
public class ActivityControllerTest {

    @Autowired
    private transient AuthManager mockAuthManager;

    @Mock
    private transient ActivityService service;

    @Test
    public void createTraining() throws WrongDateException, InvalidActivityException {

        ActivityController controller = new ActivityController(service, mockAuthManager);
        ActivityRequest model = new ActivityRequest(1, new Date(2022, 12, 17, 16, 30),
            new Date(2021, 12, 18, 16, 30), "des", Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 1)), null, null, null,
            false);
        Training training = new Training(1, new Date(2022, 12, 17, 16, 30),
            new Date(2021, 12, 18, 16, 30), "des", Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 1)));
        when(service.createActivity(model)).thenReturn(training);
        ResponseEntity<Activity> response = controller.createActivity(model);
        verify(service).createActivity(model);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(training, response.getBody());
    }

    @Test
    public void createCompetition() throws WrongDateException, InvalidActivityException {

        ActivityController controller = new ActivityController(service, mockAuthManager);
        ActivityRequest model = new ActivityRequest(1, new Date(2022, 12, 17, 16, 30),
            new Date(2021, 12, 18, 16, 30), "description", Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 1)), Gender.FEMALE, "TUD", Level.Amateur,
            true);
        Competition competition = new Competition(1, new Date(2022, 12, 17, 16, 30),
            new Date(2021, 12, 18, 16, 30), "description", Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 1)), Gender.FEMALE, "TUD", Level.Amateur);
        when(service.createActivity(model)).thenReturn(competition);
        ResponseEntity<Activity> response = controller.createActivity(model);
        verify(service).createActivity(model);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(competition, response.getBody());
    }

    @Test
    public void createTrainingWithWrongDate() throws WrongDateException, InvalidActivityException {
        ActivityController controller = new ActivityController(service, mockAuthManager);
        ActivityRequest model = new ActivityRequest(1, new Date(2022, 12, 17, 16, 30),
            new Date(2021, 12, 18, 16, 30), "sth", Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 1)), null, null, null,
            false);
        when(service.createActivity(model)).thenThrow(new WrongDateException("Starting date is after ending date"));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> controller.createActivity(model));
        verify(service).createActivity(model);
        assertEquals("400 BAD_REQUEST \"WRONG_DATE_INPUT\"", exception.getMessage());
    }

    @Test
    public void createInvalidActivity() throws WrongDateException, InvalidActivityException {
        ActivityController controller = new ActivityController(service, mockAuthManager);
        ActivityRequest model = new ActivityRequest(1, new Date(2022, 12, 17, 16, 30),
            new Date(2021, 12, 18, 16, 30), "sth", Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 0)), null, null, null,
            false);
        when(service.createActivity(model)).thenThrow(new InvalidActivityException("Invalid activity"));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> controller.createActivity(model));
        verify(service).createActivity(model);
        assertEquals("400 BAD_REQUEST \"INVALID_ACTIVITY\"", exception.getMessage());
    }

    @Test
    public void getAllEmpty() {
        ActivityController controller = new ActivityController(service, mockAuthManager);
        when(service.getAllActivities()).thenReturn(Arrays.asList());
        List<ActivityResponse> response = controller.getAll();
        verify(service).getAllActivities();
        assertEquals(Arrays.asList(), response);
    }

    @Test
    public void getAllActivities() {
        ActivityController controller = new ActivityController(service, mockAuthManager);
        ActivityResponse training = new ActivityResponse(1, 1,
            new Date(2022, 12, 17, 16, 30),
            new Date(2021, 12, 18, 16, 30), "description", Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 1)), null, null, null,
            false);
        ActivityResponse competition = new ActivityResponse(1, 1,
            new Date(2022, 12, 17, 16, 30),
            new Date(2021, 12, 18, 16, 30), "description", Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Coach, 2)), Gender.FEMALE, "TUD", Level.Amateur, true);
        when(service.getAllActivities()).thenReturn(Arrays.asList(training, competition));
        List<ActivityResponse> response = controller.getAll();
        verify(service).getAllActivities();
        assertEquals(Arrays.asList(training, competition), response);
    }

    @Test
    public void getActivityById() throws ActivityNotFoundException {
        ActivityController controller = new ActivityController(service, mockAuthManager);
        ActivityResponse training = new ActivityResponse(1, 1,
            new Date(2022, 12, 17, 16, 30),
            new Date(2021, 12, 18, 16, 30), "description", Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 1)), null, null, null,
            false);
        when(service.getActivity(1)).thenReturn(training);
        ActivityResponse response = controller.getActivity(1);
        verify(service).getActivity(1);
        assertEquals(training, response);
    }

    @Test
    public void getActivityByIdNotFound() throws ActivityNotFoundException {
        ActivityController controller = new ActivityController(service, mockAuthManager);
        when(service.getActivity(1)).thenThrow(new ActivityNotFoundException());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> controller.getActivity(1));
        verify(service).getActivity(1);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void deleteActivityInvalidUser() throws ActivityNotFoundException, InvalidUserException {
        ActivityController controller = new ActivityController(service, mockAuthManager);
        doThrow(new InvalidUserException("Invalid user")).when(service).deleteActivity(1, 1);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> controller.deleteActivity(1, 1));
        verify(service).deleteActivity(1, 1);
        assertEquals("400 BAD_REQUEST \"INVALID_USER\"", exception.getMessage());
    }

    @Test
    public void deleteActivityNotFound() throws ActivityNotFoundException, InvalidUserException {
        ActivityController controller = new ActivityController(service, mockAuthManager);
        doThrow(new ActivityNotFoundException()).when(service).deleteActivity(1, 1);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> controller.deleteActivity(1, 1));
        verify(service).deleteActivity(1, 1);
        assertEquals("400 BAD_REQUEST \"ACTIVITY_NOT_FOUND\"", exception.getMessage());
    }

    @Test
    public void updateActivity() throws ActivityNotFoundException, WrongDateException, ActivityTypeMismatchException,
        InvalidUserException {
        ActivityController controller = new ActivityController(service, mockAuthManager);
        ActivityRequest model = new ActivityRequest(1, new Date(2022, 12, 17, 16, 30),
            new Date(2021, 12, 18, 16, 30), "sth else", Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 1)), null, null, null,
            false);
        Training updatedTraining = new Training(1, new Date(2022, 12, 17, 16, 30),
            new Date(2021, 12, 18, 16, 30), "sth else", Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 1)));
        when(service.updateActivity(model, 1)).thenReturn(updatedTraining);
        ResponseEntity<Activity> response = controller.updateActivity(model, 1);
        verify(service).updateActivity(model, 1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedTraining, response.getBody());
    }

    @Test
    public void updateActivityNotFound() throws ActivityNotFoundException, WrongDateException,
        ActivityTypeMismatchException, InvalidUserException {
        ActivityController controller = new ActivityController(service, mockAuthManager);
        ActivityRequest model = new ActivityRequest(1, new Date(2022, 12, 17, 16, 30),
            new Date(2021, 12, 18, 16, 30), "hello", Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 1)), null, null, null,
            false);
        when(service.updateActivity(model, 1)).thenThrow(new ActivityNotFoundException());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> controller.updateActivity(model, 1));
        verify(service).updateActivity(model, 1);
        assertEquals("400 BAD_REQUEST \"ACTIVITY_NOT_FOUND\"", exception.getMessage());

    }

    @Test
    public void updateActivityWithWrongDate() throws ActivityNotFoundException, WrongDateException,
        ActivityTypeMismatchException, InvalidUserException {
        ActivityController controller = new ActivityController(service, mockAuthManager);
        ActivityRequest model = new ActivityRequest(1, new Date(2022, 12, 17, 16, 30),
            new Date(2021, 12, 18, 16, 30), "hi there", Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 1)), null, null, null,
            false);
        when(service.updateActivity(model, 1)).thenThrow(
            new WrongDateException("Starting date is after ending date"));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> controller.updateActivity(model, 1));
        verify(service).updateActivity(model, 1);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("400 BAD_REQUEST \"WRONG_DATE_INPUT\"", exception.getMessage());
    }

    @Test
    public void updateActivityTypeMismatch() throws WrongDateException, ActivityNotFoundException,
        ActivityTypeMismatchException, InvalidUserException {
        ActivityController controller = new ActivityController(service, mockAuthManager);
        ActivityRequest model = new ActivityRequest(1, new Date(2022, 12, 17, 16, 30),
            new Date(2021, 12, 18, 16, 30), "Welcome", Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 1)), null, null, null,
            false);
        when(service.updateActivity(model, 1)).thenThrow(
            new ActivityTypeMismatchException("Activity type mismatch"));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> controller.updateActivity(model, 1));
        verify(service).updateActivity(model, 1);
        assertEquals("400 BAD_REQUEST \"ACTIVITY_TYPE_MISMATCH\"", exception.getMessage());
    }

    @Test
    public void updateActivityInvalidUser() throws WrongDateException, ActivityNotFoundException,
        ActivityTypeMismatchException, InvalidUserException {
        ActivityController controller = new ActivityController(service, mockAuthManager);
        ActivityRequest model = new ActivityRequest(1, new Date(2022, 12, 17, 16, 30),
            new Date(2021, 12, 18, 16, 30), "Welcome again", Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 1)), null, null, null,
            false);
        when(service.updateActivity(model, 1)).thenThrow(new InvalidUserException("Invalid user"));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> controller.updateActivity(model, 1));
        verify(service).updateActivity(model, 1);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("400 BAD_REQUEST \"INVALID_USER\"", exception.getMessage());

    }




}