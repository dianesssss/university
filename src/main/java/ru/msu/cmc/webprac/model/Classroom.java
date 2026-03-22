package ru.msu.cmc.webprac.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "classrooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "classroom_id")
    private Long id;

    @Column(name = "room_number", nullable = false, unique = true, length = 20)
    private String roomNumber;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;
}