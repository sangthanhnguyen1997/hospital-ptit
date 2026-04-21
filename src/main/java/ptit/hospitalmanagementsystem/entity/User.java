package ptit.hospitalmanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ptit.hospitalmanagementsystem.enums.Role;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    String id;

    @Column(name = "username", unique = true, nullable = false, length = 200)
    String username;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "last_name", columnDefinition = "TEXT")
    String fullName;

    @Column(name = "dob")
    LocalDate dob;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Builder.Default
    Role roles = Role.USER;

    // Mapping tới Bệnh nhân
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Patient patient;

    // Mapping tới Bác sĩ
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Doctor doctor;

    // Mapping tới Nhân viên
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Staff staff;
}