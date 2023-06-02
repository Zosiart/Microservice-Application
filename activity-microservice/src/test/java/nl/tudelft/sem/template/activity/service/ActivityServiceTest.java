package nl.tudelft.sem.template.activity.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import nl.tudelft.sem.template.activity.database.ActivityRepository;
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
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class ActivityServiceTest {

    private String description = "This is a description";
    private String organization = "TUD";

    @Test
    public void createValidTraining() throws WrongDateException, InvalidActivityException {
        ActivityRequest model = new ActivityRequest(1, new Date(2022, 12, 17, 16, 30),
            new Date(2022, 12, 18, 16, 30), description, Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 1)), null, null, null,
            false);
        ArgumentCaptor<Training> captor = ArgumentCaptor.forClass(Training.class);
        ActivityRepository activityRepo = mock(ActivityRepository.class);
        ActivityService service = new ActivityService(activityRepo);

        service.createActivity(model);
        verify(activityRepo).save(captor.capture());
        Training training = captor.getValue();
        assertEquals(1, training.getUserId());
        assertEquals(new Date(2022, 12, 17, 16, 30), training.getStartingDate());
        assertEquals(new Date(2022, 12, 18, 16, 30), training.getEndingDate());
        assertEquals(description, training.getDescription());
        assertEquals(Certificate.C_FOUR, training.getCertificate());
        assertEquals(Arrays.asList(new RoleAvailability(Role.Cox, 1)), training.getRoles());
    }

    @Test
    public void createValidCompetition() throws WrongDateException, InvalidActivityException {
        ActivityRequest model = new ActivityRequest(1, new Date(2022, 12, 17, 16, 30),
            new Date(2022, 12, 18, 16, 30), description, Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 1)), Gender.FEMALE, organization, Level.Amateur,
            true);
        ArgumentCaptor<Competition> captor = ArgumentCaptor.forClass(Competition.class);
        ActivityRepository activityRepo = mock(ActivityRepository.class);
        ActivityService service = new ActivityService(activityRepo);

        service.createActivity(model);
        verify(activityRepo).save(captor.capture());
        Competition competition = captor.getValue();
        assertEquals(1, competition.getUserId());
        assertEquals(new Date(2022, 12, 17, 16, 30), competition.getStartingDate());
        assertEquals(new Date(2022, 12, 18, 16, 30), competition.getEndingDate());
        assertEquals(description, competition.getDescription());
        assertEquals(Certificate.C_FOUR, competition.getCertificate());
        assertEquals(Arrays.asList(new RoleAvailability(Role.Cox, 1)), competition.getRoles());
        assertEquals(Gender.FEMALE, competition.getGender());
        assertEquals(organization, competition.getOrganization());
        assertEquals(Level.Amateur, competition.getLevel());
    }

    @Test
    public void createActivityWithInvalidDate() throws WrongDateException, InvalidActivityException {
        ActivityRequest model = new ActivityRequest(1, new Date(2022, 12, 17, 16, 30),
            new Date(2021, 12, 18, 16, 30), description, Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 1)), Gender.FEMALE, organization, Level.Amateur,
            true);
        ArgumentCaptor<Competition> captor = ArgumentCaptor.forClass(Competition.class);
        ActivityRepository activityRepo = mock(ActivityRepository.class);
        ActivityService service = new ActivityService(activityRepo);

        assertThrows(WrongDateException.class, () -> service.createActivity(model));
    }

    @Test
    public void createInvalidActivity() {
        ActivityRequest model = new ActivityRequest(1, new Date(2022, 12, 17, 16, 30),
            new Date(2022, 12, 18, 16, 30), description, Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 0)), Gender.FEMALE, organization, Level.Amateur,
            true);
        ArgumentCaptor<Competition> captor = ArgumentCaptor.forClass(Competition.class);
        ActivityRepository activityRepo = mock(ActivityRepository.class);
        ActivityService service = new ActivityService(activityRepo);
        assertThrows(InvalidActivityException.class, () -> service.createActivity(model));
    }

    @Test
    public void deleteActivitySuccessful() throws ActivityNotFoundException, InvalidUserException {
        ActivityRepository activityRepo = mock(ActivityRepository.class);
        when(activityRepo.existsById(1)).thenReturn(true);
        Training training = new Training(1, new Date(2022, 12, 17, 16, 30),
            new Date(2022, 12, 18, 16, 30), description, Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 1)));
        when(activityRepo.findById(1)).thenReturn(java.util.Optional.of(training));
        ActivityService service = new ActivityService(activityRepo);
        service.deleteActivity(1, 1);
        verify(activityRepo).deleteById(1);
    }

    @Test
    public void deleteActivityNotFound() throws ActivityNotFoundException {
        ActivityRepository activityRepo = mock(ActivityRepository.class);
        when(activityRepo.existsById(1)).thenReturn(false);
        assertThrows(ActivityNotFoundException.class, () -> new ActivityService(activityRepo)
            .deleteActivity(1, 1));
    }

    @Test
    public void deleteActivityInvalidUser() throws ActivityNotFoundException, InvalidUserException {
        ActivityRepository activityRepo = mock(ActivityRepository.class);
        when(activityRepo.existsById(1)).thenReturn(true);
        Training training = new Training(1, new Date(2022, 12, 17, 16, 30),
            new Date(2022, 12, 18, 16, 30), description, Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 1)));
        when(activityRepo.findById(1)).thenReturn(java.util.Optional.of(training));
        ActivityService service = new ActivityService(activityRepo);
        assertThrows(InvalidUserException.class, () -> service.deleteActivity(1, 2));
    }

    @Test
    public void updateCompetitionSuccessful() throws ActivityNotFoundException, InvalidUserException,
        WrongDateException, ActivityTypeMismatchException {
        ActivityRequest model = new ActivityRequest(1, new Date(2022, 12, 16, 16, 30),
            new Date(2022, 12, 20, 16, 30), "Longer Description",
            Certificate.FOUR_PLUS, Arrays.asList(new RoleAvailability(Role.Coach, 2)), Gender.FEMALE,
            organization, Level.Competitive, true);
        Competition competition = new Competition(1, new Date(2022, 12, 17, 16, 30),
            new Date(2022, 12, 18, 16, 30), description, Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 1)), Gender.MALE, "Eindhoven",
            Level.Amateur);

        ActivityRepository activityRepo = mock(ActivityRepository.class);
        when(activityRepo.existsById(1)).thenReturn(true);
        when(activityRepo.findById(1)).thenReturn(java.util.Optional.of(competition));
        ActivityService service = new ActivityService(activityRepo);
        service.updateActivity(model, 1);
        ArgumentCaptor<Competition> captor = ArgumentCaptor.forClass(Competition.class);
        verify(activityRepo).save(captor.capture());
        Competition updatedCompetition = captor.getValue();
        assertEquals(1, updatedCompetition.getUserId());
        assertEquals(new Date(2022, 12, 16, 16, 30), updatedCompetition.getStartingDate());
        assertEquals(new Date(2022, 12, 20, 16, 30), updatedCompetition.getEndingDate());
        assertEquals("Longer Description", updatedCompetition.getDescription());
        assertEquals(Certificate.FOUR_PLUS, updatedCompetition.getCertificate());
        assertEquals(Arrays.asList(new RoleAvailability(Role.Coach, 2)), updatedCompetition.getRoles());
        assertEquals(Gender.FEMALE, updatedCompetition.getGender());
        assertEquals(organization, updatedCompetition.getOrganization());
        assertEquals(Level.Competitive, updatedCompetition.getLevel());
    }

    @Test
    public void updateTrainingSuccessful() throws ActivityNotFoundException, WrongDateException,
        ActivityTypeMismatchException, InvalidUserException {
        ActivityRequest model = new ActivityRequest(1, new Date(2022, 12, 16, 16, 30),
            new Date(2022, 12, 20, 16, 30), "Longer Description",
            Certificate.FOUR_PLUS, Arrays.asList(new RoleAvailability(Role.Coach, 2)), null,
            null, null,
            false);
        ActivityRepository activityRepo = mock(ActivityRepository.class);
        when(activityRepo.existsById(1)).thenReturn(true);
        Training training = new Training(1, new Date(2022, 12, 17, 16, 30),
            new Date(2022, 12, 18, 16, 30), description, Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 1)));
        when(activityRepo.findById(1)).thenReturn(java.util.Optional.of(training));
        ActivityService service = new ActivityService(activityRepo);
        service.updateActivity(model, 1);
        ArgumentCaptor<Training> captor = ArgumentCaptor.forClass(Training.class);
        verify(activityRepo).save(captor.capture());
        Training updatedTraining = captor.getValue();
        assertEquals(1, updatedTraining.getUserId());
        assertEquals(new Date(2022, 12, 16, 16, 30), updatedTraining.getStartingDate());
        assertEquals(new Date(2022, 12, 20, 16, 30), updatedTraining.getEndingDate());
        assertEquals("Longer Description", updatedTraining.getDescription());
        assertEquals(Certificate.FOUR_PLUS, updatedTraining.getCertificate());
        assertEquals(Arrays.asList(new RoleAvailability(Role.Coach, 2)), updatedTraining.getRoles());
    }

    @Test
    public void updateTrainingSuccessfulWithMoreFields() throws ActivityNotFoundException, WrongDateException,
        ActivityTypeMismatchException, InvalidUserException {
        ActivityRequest model = new ActivityRequest(1, new Date(2022, 12, 16, 16, 30),
            new Date(2022, 12, 20, 16, 30), "Longer Description",
            Certificate.FOUR_PLUS, Arrays.asList(new RoleAvailability(Role.Coach, 2)), Gender.FEMALE,
            organization, Level.Amateur, false);
        ActivityRepository activityRepo = mock(ActivityRepository.class);
        when(activityRepo.existsById(1)).thenReturn(true);
        Training training = new Training(1, new Date(2022, 12, 17, 16, 30),
            new Date(2022, 12, 18, 16, 30), description, Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 1)));
        when(activityRepo.findById(1)).thenReturn(java.util.Optional.of(training));
        ActivityService service = new ActivityService(activityRepo);
        service.updateActivity(model, 1);
        ArgumentCaptor<Training> captor = ArgumentCaptor.forClass(Training.class);
        verify(activityRepo).save(captor.capture());
        Training updatedTraining = captor.getValue();
        assertEquals(1, updatedTraining.getUserId());
        assertEquals(new Date(2022, 12, 16, 16, 30), updatedTraining.getStartingDate());
        assertEquals(new Date(2022, 12, 20, 16, 30), updatedTraining.getEndingDate());
        assertEquals("Longer Description", updatedTraining.getDescription());
        assertEquals(Certificate.FOUR_PLUS, updatedTraining.getCertificate());
        assertEquals(Arrays.asList(new RoleAvailability(Role.Coach, 2)), updatedTraining.getRoles());
    }

    @Test
    public void updateTrainingNotFound() throws ActivityNotFoundException, WrongDateException,
        ActivityTypeMismatchException, InvalidUserException {
        ActivityRequest model = new ActivityRequest(1, new Date(2022, 12, 16, 16, 30),
            new Date(2022, 12, 20, 16, 30), "Longer Description",
            Certificate.FOUR_PLUS, Arrays.asList(new RoleAvailability(Role.Coach, 2)), null,
            null, null,
            false);
        ActivityRepository activityRepo = mock(ActivityRepository.class);
        when(activityRepo.existsById(1)).thenReturn(false);
        assertThrows(ActivityNotFoundException.class, () -> new ActivityService(activityRepo)
            .updateActivity(model, 1));
    }

    @Test
    public void updateTrainingInvalidUser() throws ActivityNotFoundException, WrongDateException,
        ActivityTypeMismatchException, InvalidUserException {
        ActivityRequest model = new ActivityRequest(2, new Date(2022, 12, 16, 16, 30),
            new Date(2022, 12, 20, 16, 30), "Longer Description",
            Certificate.FOUR_PLUS,
            Arrays.asList(new RoleAvailability(Role.Coach, 2)), null, null, null,
            false);
        ActivityRepository activityRepo = mock(ActivityRepository.class);
        when(activityRepo.existsById(1)).thenReturn(true);
        Training training = new Training(1, new Date(2022, 12, 17, 16, 30),
            new Date(2022, 12, 18, 16, 30), description, Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 1)));
        when(activityRepo.findById(1)).thenReturn(java.util.Optional.of(training));
        ActivityService service = new ActivityService(activityRepo);
        assertThrows(InvalidUserException.class, () -> service.updateActivity(model, 1));
    }

    @Test
    public void updateTrainingWrongDate() throws ActivityNotFoundException, WrongDateException,
        ActivityTypeMismatchException {
        ActivityRequest model = new ActivityRequest(1, new Date(2022, 12, 16, 16, 30),
            new Date(2022, 12, 15, 16, 30), "Longer Description",
            Certificate.FOUR_PLUS, Arrays.asList(new RoleAvailability(Role.Coach, 2)), null,
            null, null,
            false);
        ActivityRepository activityRepo = mock(ActivityRepository.class);
        when(activityRepo.existsById(1)).thenReturn(true);
        Training training = new Training(1, new Date(2022, 12, 17, 16, 30),
            new Date(2022, 12, 18, 16, 30), description, Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 1)));
        when(activityRepo.findById(1)).thenReturn(java.util.Optional.of(training));
        ActivityService service = new ActivityService(activityRepo);
        assertThrows(WrongDateException.class, () -> service.updateActivity(model, 1));
    }

    @Test
    public void updateTrainingTypeMismatch() throws ActivityNotFoundException,
        WrongDateException, ActivityTypeMismatchException {
        ActivityRequest model = new ActivityRequest(1, new Date(2022, 12, 16, 16, 30),
            new Date(2022, 12, 15, 16, 30), "Longer Description",
            Certificate.FOUR_PLUS, Arrays.asList(new RoleAvailability(Role.Coach, 2)), Gender.FEMALE,
            organization, Level.Amateur,
            true);
        ActivityRepository activityRepo = mock(ActivityRepository.class);
        when(activityRepo.existsById(1)).thenReturn(true);
        Training training = new Training(1, new Date(2022, 12, 17, 16, 30),
            new Date(2022, 12, 18, 16, 30), description, Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 1)));
        when(activityRepo.findById(1)).thenReturn(java.util.Optional.of(training));
        ActivityService service = new ActivityService(activityRepo);
        assertThrows(ActivityTypeMismatchException.class, () -> service.updateActivity(model, 1));
    }


}