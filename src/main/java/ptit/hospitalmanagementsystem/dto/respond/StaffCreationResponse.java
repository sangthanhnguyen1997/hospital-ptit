package ptit.hospitalmanagementsystem.dto.respond;


import lombok.*;
import lombok.experimental.FieldDefaults;
import ptit.hospitalmanagementsystem.entity.HealthcareCenter;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffCreationResponse {
     Long id;
     HealthcareCenter healthcareCenter;
     String fullName;
     String position;
     String phoneNumber;
     String email;
     Boolean isActive = true;
}
