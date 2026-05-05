package ptit.hospitalmanagementsystem.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientRequest {
    Long healthcareCenterId;
    String fullName;
    LocalDate dateOfBirth;
    String gender;
    String phoneNumber;
    String identityNumber;
    String address;
    String healthInsuranceNumber;
}