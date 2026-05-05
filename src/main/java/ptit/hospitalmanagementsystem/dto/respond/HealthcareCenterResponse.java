package ptit.hospitalmanagementsystem.dto.respond;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthcareCenterResponse {
    private Long id;//ma_co_so
    private String centerName;
    private String address;
    private String phoneNumber;
    private Boolean isActive = true;
}
