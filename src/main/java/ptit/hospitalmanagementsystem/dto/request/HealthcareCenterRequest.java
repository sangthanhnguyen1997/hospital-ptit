package ptit.hospitalmanagementsystem.dto.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthcareCenterRequest {
    private String centerName;
    private String address;
    private String phoneNumber;
}
