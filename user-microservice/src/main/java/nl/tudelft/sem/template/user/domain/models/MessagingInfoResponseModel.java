package nl.tudelft.sem.template.user.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessagingInfoResponseModel {
    String name;
    String email;
}
