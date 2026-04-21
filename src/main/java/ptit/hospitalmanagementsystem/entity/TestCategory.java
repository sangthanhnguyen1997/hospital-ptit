package ptit.hospitalmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "danh_muc_xet_nghiem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_xet_nghiem")
    private Long id;

    @Column(name = "ten_xet_nghiem", nullable = false, length = 200)
    private String testName;

    @Column(name = "don_vi_tinh", length = 50)
    private String unit;

    @Column(name = "gia_tien", nullable = false, precision = 18, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "mo_ta", length = 500)
    private String description;

    @Column(name = "trang_thai", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "testCategory")
    @Builder.Default
    private List<LabTestOrder> labTestOrders = new ArrayList<>();
}