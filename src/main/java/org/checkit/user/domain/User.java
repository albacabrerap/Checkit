package org.checkit.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkit.achivement.domain.Achivement;
import org.checkit.distractor.domain.Distractor;
import org.checkit.pet.domain.Pet;
import org.checkit.studysession.domain.Studysession;
import org.checkit.task.domain.Task;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private Integer tasksCompleted;
    private Integer sessionsCompleted;

    @OneToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @OneToMany(mappedBy = "user")
    private List<Studysession> studysessions;

    @OneToMany(mappedBy = "user")
    private List<Distractor> distractors;

    @OneToMany(mappedBy = "user")
    private List<Task> tasks;

    @ManyToMany
    @JoinTable(name = "user_achivements",  joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "achivement_id"))
    private List<Achivement> achivements;
}
