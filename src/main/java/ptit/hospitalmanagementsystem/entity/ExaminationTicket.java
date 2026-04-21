package ptit.hospitalmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import ptit.hospitalmanagementsystem.enums.ServiceType;
import ptit.hospitalmanagementsystem.enums.ExaminationTicketStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "phieu_kham",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_phieu_kham_so_thu_tu",
                        columnNames = {"ma_co_so", "ngay_kham_he_thong", "so_thu_tu_kham"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExaminationTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_phieu_kham")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_benh_nhan", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_co_so", nullable = false)
    private HealthcareCenter healthcareCenter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_nhan_vien_tiep_nhan", nullable = false)
    private Staff receptionStaff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_phong_kham", nullable = false)
    private ExaminationRoom examinationRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_bac_si", nullable = false)
    private Doctor doctor;

    @Column(name = "so_thu_tu_kham", nullable = false)
    private Integer sequenceNumber;

    @Column(name = "ngay_kham", nullable = false)
    private LocalDateTime examinationDate;

    @Column(name = "ngay_kham_he_thong", nullable = false)
    private LocalDate systemExaminationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_kham", length = 50)
    private ServiceType serviceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai_phieu_kham", nullable = false, length = 50)
    private ExaminationTicketStatus status;

    @Column(name = "ghi_chu", length = 500)
    private String note;

    @OneToMany(mappedBy = "examinationTicket")
    @Builder.Default
    private List<Invoice> invoices = new ArrayList<>();

    @OneToOne(mappedBy = "examinationTicket", cascade = CascadeType.ALL)
    private MedicalRecord medicalRecord;

    @PrePersist
    public void prePersist() {
        if (examinationDate == null) examinationDate = LocalDateTime.now();
        if (systemExaminationDate == null) systemExaminationDate = LocalDate.now();
    }
}