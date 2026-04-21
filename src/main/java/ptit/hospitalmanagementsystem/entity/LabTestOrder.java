package ptit.hospitalmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chi_dinh_xet_nghiem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabTestOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_chi_dinh_xet_nghiem")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_ho_so_kham", nullable = false)
    private MedicalRecord medicalRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_xet_nghiem", nullable = false)
    private TestCategory testCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_bac_si_chi_dinh", nullable = false)
    private Doctor prescribingDoctor;

    @Column(name = "ngay_chi_dinh", nullable = false)
    private LocalDateTime orderDate;

    @OneToOne(mappedBy = "labTestOrder", cascade = CascadeType.ALL)
    private TestResult testResult;

    @PrePersist
    public void prePersist() {
        if (orderDate == null) orderDate = LocalDateTime.now();
    }
}