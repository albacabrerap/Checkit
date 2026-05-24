package org.checkit.user.application;

import lombok.RequiredArgsConstructor;
import org.checkit.user.domain.User;
import org.checkit.user.domain.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //lo añadi para el oauth
    @GetMapping("/me")
    public ResponseEntity<User> me(OAuth2AuthenticationToken authToken) {
        User user = userService.findOrCreateFromGoogle(authToken);
        return ResponseEntity.ok(user);
    }
}