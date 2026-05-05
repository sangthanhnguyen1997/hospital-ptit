package ptit.hospitalmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "danh_muc_xet_nghiem") // Tên bảng tiếng Việt không dấu
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestCategory {
    @Id
    @Column(name = "ma_xet_nghiem", length = 50) // Kiểu String
    private String id;

    @Column(name = "ten_xet_nghiem", nullable = false, length = 200)
    private String name;

    @Column(name = "don_vi_tinh", length = 50)
    private String unit;

    @Column(name = "gia_tien", nullable = false, precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "mo_ta", length = 500)
    private String description;

    @Column(name = "trang_thai", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}