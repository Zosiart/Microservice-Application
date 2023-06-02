package nl.tudelft.sem.template.planning.models;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import nl.tudelft.sem.template.planning.domain.entity.Appointment;

@Data
public class PendingAppointmentsResponseModel {
    private long activityId;
    private long appointmentId;

    /**
     * Provides a list of appointments that are pending.
     *
     * @param pendingAppointments - the list of pending appointments
     * @return a list of pending appointments
     */
    public static List<PendingAppointmentsResponseModel> fromAppointments(List<Appointment> pendingAppointments) {
        return pendingAppointments.stream()
                .map(PendingAppointmentsResponseModel::fromPendingAppointment)
                .collect(Collectors.toList());
    }

    /**
     * Creates a PendingAppointmentsResponseModel from an Appointment.
     *
     * @param pendingAppointment -  the appointment to create the response model from
     * @return the response model
     */
    public static PendingAppointmentsResponseModel fromPendingAppointment(Appointment pendingAppointment) {
        PendingAppointmentsResponseModel pendingAppointmentsResponseModel = new PendingAppointmentsResponseModel();
        pendingAppointmentsResponseModel.setActivityId(pendingAppointment.getActivityId());
        pendingAppointmentsResponseModel.setAppointmentId(pendingAppointment.getId());
        return pendingAppointmentsResponseModel;
    }
}
