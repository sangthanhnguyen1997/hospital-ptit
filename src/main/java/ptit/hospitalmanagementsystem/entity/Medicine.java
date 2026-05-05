package ptit.hospitalmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "thuoc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicine {
    @Id
    @Column(name = "ma_thuoc", length = 50) // Kiểu String để bảo mật hơn
    private String id;

    @Column(name = "ten_thuoc", nullable = false, length = 200)
    private String name;

    @Column(name = "don_vi_tinh", length = 50)
    private String unit;

    @Column(name = "ham_luong", length = 100)
    private String dosage;

    @Column(name = "gia_ban", nullable = false, precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "trang_thai", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}