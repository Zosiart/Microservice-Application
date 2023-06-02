package nl.tudelft.sem.template.planning.controllers;

import java.util.List;
import java.util.NoSuchElementException;
import nl.tudelft.sem.template.planning.domain.enums.Role;
import nl.tudelft.sem.template.planning.domain.enums.Status;
import nl.tudelft.sem.template.planning.domain.service.PlanningService;
import nl.tudelft.sem.template.planning.exception.InsufficientPermissions;
import nl.tudelft.sem.template.planning.exception.ServiceNotAvailable;
import nl.tudelft.sem.template.planning.models.ActivityResponseModel;
import nl.tudelft.sem.template.planning.models.AppointmentResponseModel;
import nl.tudelft.sem.template.planning.models.AppointmentStatusResponseModel;
import nl.tudelft.sem.template.planning.models.PendingAppointmentsResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for the planning microservice.
 */
@RestController
@RequestMapping("/planning")
public class PlanningController {

    private final transient PlanningService planningService;

    /**
     * Instantiates a new controller.
     *
     * @param planningService - the service that handles the planning
     */
    @Autowired
    public PlanningController(PlanningService planningService) {
        this.planningService = planningService;
    }

    /**
     * Endpoint for changing the status of an appointment.
     *
     * @param id     - the id of the appointment
     * @param status - the new status of the appointment
     */
    @PostMapping("/appointment/resolve/{id}")
    public ResponseEntity resolveAppointment(@PathVariable("id") long id, @RequestBody Status status) {
        try {
            if (planningService.resolveAppointment(id, status)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (InsufficientPermissions e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NoSuchFieldError | NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (ServiceNotAvailable e) {
            return ResponseEntity.ok().build();
        }
    }

    /**
     * Endpoint for getting all the user's appointments.
     *
     * @return a list of all the appointments ids together with their status
     */
    @GetMapping("/appointment/all")
    public ResponseEntity<List<AppointmentStatusResponseModel>> getAllAppointments() {
        return new ResponseEntity<>(planningService.getAllAppointments(), HttpStatus.OK);
    }


    /**
     * Endpoint for creating a new appointment.
     *
     * @param id   - activity id
     * @param role - the role you're applying for
     */
    @PostMapping("/appointment/{id}")
    public ResponseEntity newAppointment(@PathVariable("id") int id, @RequestBody Role role) {
        try {
            planningService.createAppointment(id, role);
            return ResponseEntity.ok().build();
        } catch (InsufficientPermissions e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ServiceNotAvailable e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint for getting the specific appointment.
     *
     * @param id - the id of the appointment
     * @return the appointment's details
     */
    @GetMapping("/appointment/{id}")
    public ResponseEntity<AppointmentResponseModel> getAppointment(@PathVariable("id") int id) {
        try {
            return new ResponseEntity<>(planningService.getAppointment(id), HttpStatus.OK);
        } catch (InsufficientPermissions e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (NoSuchFieldError | ServiceNotAvailable e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint for getting all the available activities.
     *
     * @return a list of all the available activities
     */
    @GetMapping("/activity/available")
    public ResponseEntity<List<ActivityResponseModel>> getAvailableActivities() {
        return new ResponseEntity<>(planningService.getAvailableActivities(), HttpStatus.OK);
    }

    /**
     * Endpoint for getting all the pending appointments of the activities owned by user.
     *
     * @return a list of all the pending appointments
     */
    @GetMapping("/appointment/pending")
    public ResponseEntity<List<PendingAppointmentsResponseModel>> getPendingAppointments() {
        try {
            return new ResponseEntity<>(planningService.getPendingAppointments(), HttpStatus.OK);
        } catch (ServiceNotAvailable | NoSuchFieldError e) {
            return ResponseEntity.notFound().build();
        }
    }
}
