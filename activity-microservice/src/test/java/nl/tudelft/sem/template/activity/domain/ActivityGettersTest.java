package nl.tudelft.sem.template.activity.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import nl.tudelft.sem.template.activity.database.ActivityRepository;
import nl.tudelft.sem.template.activity.domain.activity.*;
import nl.tudelft.sem.template.activity.dto.ActivityResponse;
import nl.tudelft.sem.template.activity.dto.UserResponse;
import nl.tudelft.sem.template.activity.exception.ActivityNotFoundException;
import org.junit.jupiter.api.Test;

public class ActivityGettersTest {

    private String description = "description";
    private String organization = "organization";

    @Test
    public void getAllActivitiesOneEntry() {
        ActivityRepository activityRepo = mock(ActivityRepository.class);
        Training training = new Training(1, new Date(2022, 12, 17, 16, 30),
            new Date(2022, 12, 18, 16, 30), description, Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 1)));
        training.setId(1);
        when(activityRepo.findAll()).thenReturn(Arrays.asList(training));
        List<ActivityResponse> activities = ActivityGetters.getAllActivities(activityRepo);
        assertEquals(1, activities.size());
        assertEquals(1, activities.get(0).getUserId());
        assertEquals(new Date(2022, 12, 17, 16, 30), activities.get(0).getStartingDate());
        assertEquals(new Date(2022, 12, 18, 16, 30), activities.get(0).getEndingDate());
        assertEquals(description, activities.get(0).getDescription());
        assertEquals(Certificate.C_FOUR, activities.get(0).getCertificate());
        assertEquals(Arrays.asList(new RoleAvailability(Role.Cox, 1)), activities.get(0).getRoles());
    }


    @Test
    public void getAllActivitiesMultipleEntries() {
        ActivityRepository activityRepo = mock(ActivityRepository.class);
        Training training = new Training(1,
            new Date(2022, 12, 17, 16, 30),
            new Date(2022, 12, 18, 16, 30), description,
            Certificate.C_FOUR, Arrays.asList(new RoleAvailability(Role.Cox, 1)));
        training.setId(1);
        Competition competition = new Competition(1, new Date(2022, 12, 17, 16, 30),
            new Date(2022, 12, 18, 16, 30),
            description, Certificate.C_FOUR, List.of(new RoleAvailability(Role.Cox, 1)),
            Gender.FEMALE, organization, Level.Amateur);
        competition.setId(2);
        when(activityRepo.findAll()).thenReturn(Arrays.asList(training, competition));

        List<ActivityResponse> activities = ActivityGetters.getAllActivities(activityRepo);
        assertEquals(2, activities.size());
        assertEquals(1, activities.get(0).getUserId());
        assertEquals(new Date(2022, 12, 17, 16, 30), activities.get(0).getStartingDate());
        assertEquals(new Date(2022, 12, 18, 16, 30), activities.get(0).getEndingDate());
        assertEquals(description, activities.get(0).getDescription());
        assertEquals(Certificate.C_FOUR, activities.get(0).getCertificate());
        assertEquals(List.of(new RoleAvailability(Role.Cox, 1)), activities.get(0).getRoles());
        assertEquals(1, activities.get(1).getUserId());
        assertEquals(new Date(2022, 12, 17, 16, 30), activities.get(1).getStartingDate());
        assertEquals(new Date(2022, 12, 18, 16, 30), activities.get(1).getEndingDate());
        assertEquals(description, activities.get(1).getDescription());
        assertEquals(Certificate.C_FOUR, activities.get(1).getCertificate());
        assertEquals(Gender.FEMALE, activities.get(1).getGender());
        assertEquals(organization, activities.get(1).getOrganization());
        assertEquals(Level.Amateur, activities.get(1).getLevel());
    }

    @Test
    public void getActivitiesForOwnerTest() {
        ActivityRepository activityRepository = mock(ActivityRepository.class);
        Training training = new Training(1,
            new Date(2022, 12, 17, 16, 30),
            new Date(2022, 12, 18, 16, 30), description,
            Certificate.C_FOUR, List.of(new RoleAvailability(Role.Cox, 1)));
        training.setId(1);
        Competition competition = new Competition(1, new Date(2022, 12, 17, 16, 30),
            new Date(2022, 12, 18, 16, 30),
            description, Certificate.C_FOUR, List.of(new RoleAvailability(Role.Cox, 1)),
            Gender.FEMALE, organization, Level.Amateur);
        competition.setId(2);
        List<Activity> retrievedActivities = List.of(training, competition);
        when(activityRepository.findActivitiesByUserId(1)).thenReturn(retrievedActivities);
        List<ActivityResponse> expectedResponse =
            retrievedActivities.stream().map(ActivityGetters::mapToActivityResponse).collect(Collectors.toList());
        assertEquals(expectedResponse, ActivityGetters.getActivities(new UserResponse(), 1L, activityRepository));
    }

    @Test
    public void getActivitiesWithFiltersTest() {

        // first batch of matching activities
        Training training1 = new Training(1,
            new Date(2023, 6, 23, 12, 0, 0),
            new Date(2023, 6, 23, 15, 0, 0), description,
            Certificate.C_FOUR, List.of(new RoleAvailability(Role.PortSideRower, 1)));
        training1.setId(1);

        Training training2 = new Training(1,
            new Date(2023, 6, 23, 13, 0, 0),
            new Date(2023, 6, 23, 14, 0, 0), description,
            Certificate.C_FOUR, List.of(new RoleAvailability(Role.PortSideRower, 2)));
        training2.setId(2);

        Competition competition1 = new Competition(1,
            new Date(2023, 6, 23, 12, 0, 0),
            new Date(2023, 6, 23, 15, 0, 0),
            description, Certificate.C_FOUR,
            List.of(new RoleAvailability(Role.Cox, 1), new RoleAvailability(Role.PortSideRower, 2)),
            Gender.MALE, organization, Level.Amateur);
        competition1.setId(3);

        Availability availability1 = new Availability(new Date(2023, 6, 23, 12, 0, 0), new Date(2023, 6, 23, 15, 0, 0));
        Availability availability2 = new Availability(new Date(2023, 6, 24, 13, 0, 0), new Date(2023, 6, 24, 22, 0, 0));
        UserResponse userResponse = new UserResponse(
            List.of(new RoleInfo(Role.PortSideRower, Level.Amateur), new RoleInfo(Role.Cox, Level.Competitive)),
            List.of(availability1, availability2),
            Certificate.EIGHT_PLUS, Gender.MALE, organization);

        ActivityRepository activityRepo = mock(ActivityRepository.class);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        when(activityRepo.findByFilters(null, dateFormat.format(availability1.getStartTime()),
            dateFormat.format(availability1.getEndTime()), userResponse.getGender().ordinal(),
            userResponse.getOrganization())).thenReturn(List.of(training1, training2, competition1));

        // second batch of matching activities
        Training training3 = new Training(1,
            new Date(2023, 6, 24, 14, 0, 0),
            new Date(2023, 6, 24, 19, 0, 0), description,
            Certificate.C_FOUR, List.of(new RoleAvailability(Role.PortSideRower, 2)));
        training3.setId(4);

        when(activityRepo.findByFilters(null, dateFormat.format(availability2.getStartTime()),
            dateFormat.format(availability2.getEndTime()), userResponse.getGender().ordinal(),
            userResponse.getOrganization())).thenReturn(List.of(training3));

        List<ActivityResponse> expectedResponse =
            Stream.of(training1, training2, competition1, training3).map(ActivityGetters::mapToActivityResponse).collect(
                Collectors.toList());

        assertEquals(
            new HashSet<>(expectedResponse), new HashSet<>(ActivityGetters.getActivities(userResponse, null, activityRepo)));
    }

    @Test
    public void getActivitiesWithFiltersRolesNotMatching() {
        Availability availability1 = new Availability(new Date(2023, 6, 23, 12, 0, 0), new Date(2023, 6, 23, 15, 0, 0));
        Availability availability2 = new Availability(new Date(2023, 6, 24, 13, 0, 0), new Date(2023, 6, 24, 22, 0, 0));
        UserResponse userResponse = new UserResponse(
            List.of(new RoleInfo(Role.PortSideRower, Level.Amateur)),
            List.of(availability1, availability2),
            Certificate.FOUR_PLUS, Gender.MALE, organization);

        ActivityRepository activityRepo = mock(ActivityRepository.class);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // first batch of matching activities
        Training training1 = new Training(1,
            new Date(2023, 6, 23, 12, 0, 0),
            new Date(2023, 6, 23, 15, 0, 0), description,
            Certificate.C_FOUR, List.of(new RoleAvailability(Role.Cox, 1)));
        training1.setId(1);

        Competition competition1 = new Competition(1,
            new Date(2023, 6, 23, 12, 0, 0),
            new Date(2023, 6, 23, 15, 0, 0),
            description, Certificate.C_FOUR,
            List.of(new RoleAvailability(Role.Cox, 1), new RoleAvailability(Role.PortSideRower, 2)),
            Gender.MALE, organization, Level.Amateur);
        competition1.setId(3);

        when(activityRepo.findByFilters(null, dateFormat.format(availability1.getStartTime()),
            dateFormat.format(availability1.getEndTime()), userResponse.getGender().ordinal(),
            userResponse.getOrganization())).thenReturn(List.of(training1, competition1));

        // second batch of matching activities
        Training training3 = new Training(1,
            new Date(2023, 6, 24, 14, 0, 0),
            new Date(2023, 6, 24, 19, 0, 0), description,
            Certificate.C_FOUR, List.of(new RoleAvailability(Role.Coach, 2)));
        training3.setId(4);

        when(activityRepo.findByFilters(null, dateFormat.format(availability2.getStartTime()),
            dateFormat.format(availability2.getEndTime()), userResponse.getGender().ordinal(),
            userResponse.getOrganization())).thenReturn(List.of(training3));

        List<ActivityResponse> expectedResponse =
            Stream.of(competition1).map(ActivityGetters::mapToActivityResponse).collect(
                Collectors.toList());

        assertEquals(
            new HashSet<>(expectedResponse), new HashSet<>(ActivityGetters.getActivities(userResponse, null, activityRepo)));
    }

    @Test
    public void getActivitiesWithFiltersCompetitionLevelNotMatching() {
        Availability availability1 = new Availability(new Date(2023, 6, 23, 12, 0, 0), new Date(2023, 6, 23, 15, 0, 0));
        Availability availability2 = new Availability(new Date(2023, 6, 24, 13, 0, 0), new Date(2023, 6, 24, 22, 0, 0));
        UserResponse userResponse = new UserResponse(
            List.of(new RoleInfo(Role.Cox, Level.Amateur)),
            List.of(availability1, availability2),
            Certificate.C_FOUR, Gender.MALE, organization);

        ActivityRepository activityRepo = mock(ActivityRepository.class);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // first batch of matching activities
        Training training1 = new Training(1,
            new Date(2023, 6, 23, 12, 0, 0),
            new Date(2023, 6, 23, 15, 0, 0), description,
            Certificate.FOUR_PLUS, List.of(new RoleAvailability(Role.Cox, 1)));
        training1.setId(1);

        Competition competition1 = new Competition(1,
            new Date(2023, 6, 23, 12, 0, 0),
            new Date(2023, 6, 23, 15, 0, 0),
            description, Certificate.EIGHT_PLUS,
            List.of(new RoleAvailability(Role.Cox, 1), new RoleAvailability(Role.PortSideRower, 2)),
            Gender.MALE, organization, Level.Competitive);
        competition1.setId(3);

        when(activityRepo.findByFilters(null, dateFormat.format(availability1.getStartTime()),
            dateFormat.format(availability1.getEndTime()), userResponse.getGender().ordinal(),
            userResponse.getOrganization())).thenReturn(List.of(training1, competition1));

        // second batch of matching activities
        Training training3 = new Training(1,
            new Date(2023, 6, 24, 14, 0, 0),
            new Date(2023, 6, 24, 19, 0, 0), description,
            Certificate.C_FOUR, List.of(new RoleAvailability(Role.Coach, 2)));
        training3.setId(4);

        when(activityRepo.findByFilters(null, dateFormat.format(availability2.getStartTime()),
            dateFormat.format(availability2.getEndTime()), userResponse.getGender().ordinal(),
            userResponse.getOrganization())).thenReturn(List.of(training3));

        assertEquals(0, ActivityGetters.getActivities(userResponse, null, activityRepo).size());
    }

    @Test
    public void getActivitySuccessful() throws ActivityNotFoundException {
        ActivityRepository activityRepo = mock(ActivityRepository.class);
        when(activityRepo.existsById(1)).thenReturn(true);
        Training training = new Training(1, new Date(2022, 12, 17, 16, 30),
            new Date(2022, 12, 18, 16, 30), description, Certificate.C_FOUR,
            List.of(new RoleAvailability(Role.Cox, 1)));
        training.setId(1);
        when(activityRepo.findById(1)).thenReturn(java.util.Optional.of(training));
        ActivityResponse model = ActivityGetters.getActivity(1, activityRepo);

        assertEquals(1, model.getUserId());
        assertEquals(new Date(2022, 12, 17, 16, 30), model.getStartingDate());
        assertEquals(new Date(2022, 12, 18, 16, 30), model.getEndingDate());
        assertEquals(description, model.getDescription());
        assertEquals(Certificate.C_FOUR, model.getCertificate());
        assertEquals(List.of(new RoleAvailability(Role.Cox, 1)), model.getRoles());
    }

    @Test
    public void getActivityNotFound() throws ActivityNotFoundException {
        ActivityRepository activityRepo = mock(ActivityRepository.class);
        when(activityRepo.existsById(1)).thenReturn(false);
        assertThrows(ActivityNotFoundException.class, () -> ActivityGetters.getActivity(1, activityRepo));
    }

    @Test
    public void mapTrainingToActivityResponse() {
        Training training = new Training(1, new Date(2022, 12, 17, 16, 30),
            new Date(2022, 12, 18, 16, 30), description, Certificate.C_FOUR,
            List.of(new RoleAvailability(Role.Cox, 1)));
        training.setId(1);
        ActivityRepository activityRepo = mock(ActivityRepository.class);
        ActivityResponse activityResponse = ActivityGetters.mapToActivityResponse(training);

        assertEquals(1, activityResponse.getUserId());
        assertEquals(new Date(2022, 12, 17, 16, 30), activityResponse.getStartingDate());
        assertEquals(new Date(2022, 12, 18, 16, 30), activityResponse.getEndingDate());
        assertEquals(description, activityResponse.getDescription());
        assertEquals(Certificate.C_FOUR, activityResponse.getCertificate());
        assertEquals(List.of(new RoleAvailability(Role.Cox, 1)), activityResponse.getRoles());
        assertFalse(activityResponse.getIsCompetition());
    }

    @Test
    public void mapCompetitionToActivityResponse() {
        Competition competition = new Competition(1,
            new Date(2020, 12, 13, 23, 30, 10),
            new Date(2020, 12, 14, 10, 00, 00), description,
            Certificate.FOUR_PLUS, List.of(new RoleAvailability(Role.Cox, 1)), Gender.FEMALE,
            organization, Level.Amateur);
        competition.setId(1);
        ActivityRepository activityRepo = mock(ActivityRepository.class);
        ActivityResponse activityResponse = ActivityGetters.mapToActivityResponse(competition);

        assertEquals(1, activityResponse.getUserId());
        assertEquals(new Date(2020, 12, 13, 23, 30, 10), activityResponse
            .getStartingDate());
        assertEquals(new Date(2020, 12, 14, 10, 00, 00), activityResponse
            .getEndingDate());
        assertEquals(description, activityResponse.getDescription());
        assertEquals(Certificate.FOUR_PLUS, activityResponse.getCertificate());
        assertEquals(List.of(new RoleAvailability(Role.Cox, 1)), activityResponse.getRoles());
        assertTrue(activityResponse.getIsCompetition());
        assertEquals(Gender.FEMALE, activityResponse.getGender());
        assertEquals(organization, activityResponse.getOrganization());
        assertEquals(Level.Amateur, activityResponse.getLevel());

    }
}
