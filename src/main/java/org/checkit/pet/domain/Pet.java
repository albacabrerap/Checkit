package org.checkit.pet.domain;

import jakarta.persistence.*;
import lombok.*;
import org.checkit.user.domain.User;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer level = 1;
    private Integer exp = 0;
    private String currentSkin = "DEFAULT";

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}