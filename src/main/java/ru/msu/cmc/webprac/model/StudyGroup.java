package ru.msu.cmc.webprac.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "groups_table")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 30)
    private String name;

    @Column(name = "study_year", nullable = false)
    private Integer studyYear;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;
}