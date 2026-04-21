package ptit.hospitalmanagementsystem.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder//tranh nham lanh builder cua lombok va mapstruct nhe
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LogoutRequest {
    String token;
}
