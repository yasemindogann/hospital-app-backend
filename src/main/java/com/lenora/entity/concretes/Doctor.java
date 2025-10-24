package com.lenora.entity.concretes;

import com.lenora.entity.enums.Specialization;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doctors")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //primary key

    private String fullName;

    @Enumerated(EnumType.STRING)
    private Specialization specialization;

    private String phone;

    //DB'de Long
    //referencedColumnName = "id"  => User'daki id'yi referans al
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")  //foreign key
    private User user;
}
