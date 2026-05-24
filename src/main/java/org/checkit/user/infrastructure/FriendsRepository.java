package org.checkit.user.infrastructure;

import org.checkit.user.domain.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendsRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findByUserIdAndAcceptedTrue(Long userId);

}