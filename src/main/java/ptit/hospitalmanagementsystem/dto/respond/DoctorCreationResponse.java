package ptit.hospitalmanagementsystem.dto.respond;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorCreationResponse {
    Long id;
     String healthcareCenter;
     String fullName;
     String specialty;
     String practicingCertificate;
     String phoneNumber;//is a password
     String email;//is a username
     Boolean isActive = true;
}
