package ptit.hospitalmanagementsystem.dto.respond;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestCategoryResponse {
    String id; // Kiểu String (Ví dụ: LAB-BLOOD-01)
    String name;
    String unit;
    BigDecimal price;
    String description;
    Boolean isActive;
}