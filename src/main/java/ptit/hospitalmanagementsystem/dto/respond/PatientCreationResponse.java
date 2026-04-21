package ptit.hospitalmanagementsystem.dto.respond;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ptit.hospitalmanagementsystem.enums.Gender;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientCreationResponse {
    String UserName; // mac dinh == so dien thoai.
   // String Password; // mac dinh == so dien thoai
    String hoTen;
    LocalDate ngaySinh;
    Gender gender;
    String soDienThoai;
    String soCanCuoc;
    String diaChi;
    String soBaoHiemYTe;
    LocalDate Ngaytao;
}
