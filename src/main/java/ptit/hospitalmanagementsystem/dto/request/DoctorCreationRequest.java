package ptit.hospitalmanagementsystem.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorCreationRequest {
     String healthcareCenter;
     String fullName;
     String specialty;
     String practicingCertificate;
     String phoneNumber;
     String email;
}
