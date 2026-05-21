package org.checkit.user.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "friendships")
@Getter @Setter
@NoArgsConstructor
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    private User friend;

    private boolean accepted = false;
}