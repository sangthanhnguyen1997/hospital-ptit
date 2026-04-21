package ptit.hospitalmanagementsystem.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;
import ptit.hospitalmanagementsystem.enums.Gender;

import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientCreationRequest {
    String hoTen;
    LocalDate ngaySinh;
    Gender gender;
    String soDienThoai;
    String soCanCuoc;
    String diaChi;
    String soBaoHiemYTe;
}
