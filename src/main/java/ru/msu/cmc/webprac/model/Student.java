package ru.msu.cmc.webprac.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    @Column(name = "surname", nullable = false, length = 50)
    private String surname;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "patronymic", length = 50)
    private String patronymic;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private StudyGroup group;
}