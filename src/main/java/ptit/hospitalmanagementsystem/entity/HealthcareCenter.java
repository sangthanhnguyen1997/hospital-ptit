package ptit.hospitalmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "co_so")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthcareCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_co_so")
    private Long id;

    @Column(name = "ten_co_so", nullable = false, length = 200)
    private String centerName;

    @Column(name = "dia_chi", length = 300)
    private String address;

    @Column(name = "so_dien_thoai", length = 20)
    private String phoneNumber;

    @Column(name = "trang_thai", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "healthcareCenter")
    @Builder.Default
    private List<ExaminationRoom> examinationRooms = new ArrayList<>();

    @OneToMany(mappedBy = "healthcareCenter")
    @Builder.Default
    private List<Staff> staffList = new ArrayList<>();

    @OneToMany(mappedBy = "healthcareCenter")
    @Builder.Default
    private List<Doctor> doctors = new ArrayList<>();

    @OneToMany(mappedBy = "healthcareCenter")
    @Builder.Default
    private List<ExaminationTicket> examinationTickets = new ArrayList<>();

    @OneToMany(mappedBy = "healthcareCenter")
    @Builder.Default
    private List<Invoice> invoices = new ArrayList<>();
}