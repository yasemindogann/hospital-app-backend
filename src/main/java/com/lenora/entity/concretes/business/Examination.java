package com.lenora.entity.concretes.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lenora.entity.concretes.user.Doctor;
import com.lenora.entity.concretes.user.Patient;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "examinations")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Examination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient patient;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime examinationDateTime; //muayene tarihi

    private String examinationDiagnosis; //muayene tanısı

    @Column(nullable = false)
    private Boolean active = true;
}
