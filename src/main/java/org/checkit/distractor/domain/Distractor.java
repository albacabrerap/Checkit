package org.checkit.distractor.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkit.user.domain.User;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Distractor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String url;

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;
}
