package org.checkit.user.domain;

import lombok.RequiredArgsConstructor;
import org.checkit.exception.BusinessException;
import org.checkit.user.dto.UserConfigUpdateDto;
import org.checkit.user.dto.MobileWidgetResponseDto;
import org.checkit.task.domain.Task;
import org.checkit.task.domain.State;
import org.checkit.user.infrastructure.UserRepository;
import org.checkit.user.infrastructure.FriendsRepository;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FriendsRepository friendshipRepository; // Added repository

    @Transactional
    public User updateUserPreferences(User user, UserConfigUpdateDto dto) {
        user.setSessionsCompleted(dto.getWeeklyTaskGoal());
        return userRepository.save(user);
    }

    public MobileWidgetResponseDto getWidgetStatusData(User user, int userDefinedGoal) {
        long completedThisWeek = user.getTasks().stream()
                .filter(task -> task.getState() == State.COMPLETADA)
                .count();

        int percentage = (userDefinedGoal > 0) ? (int) ((completedThisWeek * 100) / userDefinedGoal) : 0;
        if (percentage > 100) percentage = 100;

        MobileWidgetResponseDto response = new MobileWidgetResponseDto();
        response.setWeeklyJarPercentage(percentage);

        if (percentage >= 100) {
            response.setMotivationalNote("Completaste tu Jar de la semana!! Felicitaciones");
        } else if (percentage > 50) {
            response.setMotivationalNote("Vas más de la mitad!!! Sigue así y no olvides tomar descansos");
        } else {
            response.setMotivationalNote("Tú puedes! Yo te apoyo!");
        }

        return response;
    }

    @Transactional(readOnly = true)
    public List<Long> getFriendIds(Long userId) {
        List<Friendship> friendships = friendshipRepository.findByUserIdAndAcceptedTrue(userId);
        return friendships.stream()
                .map(friendship -> friendship.getFriend().getId())
                .toList();
    }

    @Transactional
    public User findOrCreateFromGoogle(OAuth2AuthenticationToken authToken) {
        OAuth2User oauthUser = authToken.getPrincipal();

        String email = Objects.requireNonNull(oauthUser).getAttribute("email");
        String name = oauthUser.getAttribute("name");
        String googleId = oauthUser.getAttribute("sub"); // Google's unique user ID

        return userRepository.findByGoogleId(googleId)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setGoogleId(googleId);
                    return userRepository.save(newUser);
                });
    }
}