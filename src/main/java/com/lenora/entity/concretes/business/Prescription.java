package com.lenora.entity.concretes.business;

import com.lenora.entity.concretes.business.Examination;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "prescriptions")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "examination_id", referencedColumnName = "id")
    private Examination examination;

    private String medicineName;

    private String dosage;

    private String description;

    @Column(nullable = false)
    private Boolean active = true;
}
