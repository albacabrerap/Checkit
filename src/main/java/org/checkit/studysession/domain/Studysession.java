package org.checkit.studysession.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkit.user.domain.User;

import java.time.ZonedDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Studysession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private ZonedDateTime startTime;
    @Column(nullable = false)
    private ZonedDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
