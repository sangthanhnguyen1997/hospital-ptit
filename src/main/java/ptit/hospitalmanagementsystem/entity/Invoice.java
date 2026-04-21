package ptit.hospitalmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hoa_don")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_hoa_don")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_phieu_kham", nullable = false)
    private ExaminationTicket examinationTicket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_co_so", nullable = false)
    private HealthcareCenter healthcareCenter;

    @Column(name = "ngay_lap", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "tong_tien", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "trang_thai", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "invoice")
    @Builder.Default
    private List<PaymentTransaction> paymentTransactions = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (createdDate == null) createdDate = LocalDateTime.now();
        if (totalAmount == null) totalAmount = BigDecimal.ZERO;
    }
}