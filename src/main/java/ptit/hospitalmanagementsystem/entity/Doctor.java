package ptit.hospitalmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bac_si")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_bac_si")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_co_so", nullable = false)
    private HealthcareCenter healthcareCenter;

    @Column(name = "ho_ten", nullable = false, length = 200)
    private String fullName;

    @Column(name = "chuyen_khoa", length = 100)
    private String specialty;

    @Column(name = "so_chung_chi_hanh_nghe", length = 50)
    private String practicingCertificate;

    @Column(name = "so_dien_thoai", length = 20)
    private String phoneNumber;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "dang_lam_viec", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "doctor")
    @Builder.Default
    private List<ExaminationTicket> examinationTickets = new ArrayList<>();

    @OneToMany(mappedBy = "doctor")
    @Builder.Default
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    @OneToMany(mappedBy = "prescribingDoctor")
    @Builder.Default
    private List<LabTestOrder> labTestOrders = new ArrayList<>();

    @OneToMany(mappedBy = "doctor")
    @Builder.Default
    private List<PrescriptionHeader> prescriptionHeaders = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL) // Thêm cascade ở đây
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    // --- HELPER METHOD ---
    public void setUser(User user) {
        this.user = user;
        // Nếu user chưa biết về bác sĩ này, hãy báo cho user biết
        if (user != null && user.getDoctor() != this) {
            user.setDoctor(this);
        }
    }
}