package ptit.hospitalmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "don_thuoc",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_don_thuoc_ma_ho_so_kham", columnNames = "ma_ho_so_kham")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_don_thuoc")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_ho_so_kham", nullable = false, unique = true)
    private MedicalRecord medicalRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_bac_si", nullable = false)
    private Doctor doctor;

    @Column(name = "ngay_ke_don", nullable = false)
    private LocalDateTime prescriptionDate;

    @Column(name = "loi_dan", length = 1000)
    private String doctorAdvice;

    @OneToMany(mappedBy = "prescriptionHeader", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PrescriptionDetail> prescriptionDetails = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (prescriptionDate == null) prescriptionDate = LocalDateTime.now();
    }
}