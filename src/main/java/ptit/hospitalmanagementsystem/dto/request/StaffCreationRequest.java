package ptit.hospitalmanagementsystem.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffCreationRequest {
    String healthcareCenter;
    String fullName;
    String position;
    String phoneNumber;
    String email;

}
