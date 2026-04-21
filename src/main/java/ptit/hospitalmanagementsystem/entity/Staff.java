package ptit.hospitalmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "nhan_vien")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_nhan_vien")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_co_so", nullable = false)
    private HealthcareCenter healthcareCenter;

    @Column(name = "ho_ten", nullable = false, length = 200)
    private String fullName;

    @Column(name = "chuc_vu", nullable = false, length = 100)
    private String position;

    @Column(name = "so_dien_thoai", length = 20)
    private String phoneNumber;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "dang_lam_viec", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "receptionStaff")
    @Builder.Default
    private List<ExaminationTicket> receptionTickets = new ArrayList<>();

    @OneToMany(mappedBy = "cashierStaff")
    @Builder.Default
    private List<PaymentTransaction> paymentTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "performerStaff")
    @Builder.Default
    private List<TestResult> testResults = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}