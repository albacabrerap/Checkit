package org.checkit.notif.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkit.user.domain.User;

import java.time.ZonedDateTime;

@Entity
@Getter@Setter
@NoArgsConstructor
public class Notif {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;
    @Enumerated(EnumType.STRING)
    private Type type;
    private ZonedDateTime sentAt;
    private Boolean read=false;

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;
}
