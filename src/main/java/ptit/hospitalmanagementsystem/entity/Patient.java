package ptit.hospitalmanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import ptit.hospitalmanagementsystem.enums.Gender;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "benh_nhan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_benh_nhan")
    private Long id;

    @Column(name = "ho_ten", nullable = false, length = 200)
    private String fullName;

    @Column(name = "ngay_sinh")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gioi_tinh", length = 20)
    private Gender gender;

    @Column(name = "so_dien_thoai", length = 20)
    private String phoneNumber;

    @Column(name = "so_can_cuoc", length = 20)
    private String identityNumber;

    @Column(name = "dia_chi", length = 300)
    private String address;

    @Column(name = "so_bao_hiem_y_te", length = 50)
    private String healthInsuranceNumber;

    @Column(name = "ngay_tao", nullable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "patient")
    @Builder.Default
    private List<ExaminationTicket> examinationTickets = new ArrayList<>();



    @PrePersist
    public void prePersist() {
        if (createdDate == null) createdDate = LocalDateTime.now();
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private User user;
}