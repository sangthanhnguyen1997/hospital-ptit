package ptit.hospitalmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import ptit.hospitalmanagementsystem.enums.PaymentMethod;
import ptit.hospitalmanagementsystem.enums.PaymentStatus;
import java.time.LocalDateTime;

@Entity
@Table(name = "thanh_toan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_thanh_toan")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_hoa_don", nullable = false)
    private Invoice invoice;

    @Column(name = "ngay_thanh_toan", nullable = false)
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "phuong_thuc_thanh_toan", nullable = false, length = 50)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai_thanh_toan", nullable = false, length = 50)
    private PaymentStatus paymentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_nhan_vien_thu_ngan")
    private Staff cashierStaff;

    @PrePersist
    public void prePersist() {
        if (paymentDate == null) paymentDate = LocalDateTime.now();
    }
}