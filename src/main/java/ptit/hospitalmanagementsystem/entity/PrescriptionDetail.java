package ptit.hospitalmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chi_tiet_don_thuoc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_chi_tiet_don_thuoc")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_don_thuoc", nullable = false)
    private PrescriptionHeader prescriptionHeader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_thuoc", nullable = false)
    private Medicine medicine;

    @Column(name = "so_luong", nullable = false)
    private Integer quantity;

    @Column(name = "lieu_dung", length = 200)
    private String dosage;

    @Column(name = "cach_dung", length = 500)
    private String usageInstructions;

    @Column(name = "so_ngay_dung")
    private Integer daysOfUse;
}