package ptit.hospitalmanagementsystem.dto.respond;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExaminationRoomResponse {
    Long id;
    Long healthcareCenterId;
    String centerName;
    String roomName;
    String roomType;
    String floor;
    Boolean isActive;
}