package nl.tudelft.sem.template.planning.models;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import nl.tudelft.sem.template.planning.domain.entity.Appointment;

@Data
public class AppointmentStatusResponseModel {
    private long appointmentId;
    private String status;

    /**
     * Converts a list of appointments to a list of AppointmentStatusResponseModels.
     *
     * @param appointments - the list of appointments
     * @return the list of AppointmentStatusResponseModels
     */
    public static List<AppointmentStatusResponseModel> fromAppointments(List<Appointment> appointments) {
        if (appointments == null) {
            return null;
        }

        return appointments.stream()
                .map(AppointmentStatusResponseModel::fromAppointment)
                .collect(Collectors.toList());
    }

    /**
     * Converts an appointment to an appointment status response model.
     *
     * @param appointment the appointment to convert
     * @return the appointment status response model
     */
    public static AppointmentStatusResponseModel fromAppointment(Appointment appointment) {
        if (appointment == null) {
            return null;
        }

        AppointmentStatusResponseModel appointmentStatusResponseModel = new AppointmentStatusResponseModel();
        appointmentStatusResponseModel.setAppointmentId(appointment.getId());
        appointmentStatusResponseModel.setStatus(appointment.getStatus().toString());
        return appointmentStatusResponseModel;
    }
}
