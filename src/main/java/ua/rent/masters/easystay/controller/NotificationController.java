package ua.rent.masters.easystay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.rent.masters.easystay.dto.NotificationResponse;
import ua.rent.masters.easystay.model.User;
import ua.rent.masters.easystay.service.NotificationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/subscribe")
    @ResponseStatus(HttpStatus.OK)
    public NotificationResponse subscribe(@AuthenticationPrincipal User user) {
        return notificationService.subscribe(user);
    }

    @GetMapping("/unsubscribe")
    @ResponseStatus(HttpStatus.OK)
    public NotificationResponse unsubscribe(@AuthenticationPrincipal User user) {
        return notificationService.unsubscribe(user);
    }
}
