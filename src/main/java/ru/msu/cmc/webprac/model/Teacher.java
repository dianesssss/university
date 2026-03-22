package ru.msu.cmc.webprac.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private Long id;

    @Column(name = "surname", nullable = false, length = 50)
    private String surname;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "patronymic", length = 50)
    private String patronymic;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "phone", length = 30)
    private String phone;
}