package ptit.hospitalmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "phong_kham")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExaminationRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_phong_kham")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_co_so", nullable = false)
    private HealthcareCenter healthcareCenter;

    @Column(name = "ten_phong_kham", nullable = false, length = 200)
    private String roomName;

    @Column(name = "loai_phong", length = 100)
    private String roomType;

    @Column(name = "tang", length = 20)
    private String floor;

    @Column(name = "trang_thai", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "examinationRoom")
    @Builder.Default
    private List<ExaminationTicket> examinationTickets = new ArrayList<>();
}