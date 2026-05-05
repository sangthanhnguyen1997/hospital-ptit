package ptit.hospitalmanagementsystem.dto.respond;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorResponse {
    Long id;
    String healthcareCenterName; // Trả về tên cơ sở cho thân thiện
    String fullName;
    String gender;
    String specialty;
    String practicingCertificate;
    String phoneNumber;
    String email;
    Boolean isActive;
}
