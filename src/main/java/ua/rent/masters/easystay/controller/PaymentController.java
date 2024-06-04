package ua.rent.masters.easystay.controller;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.rent.masters.easystay.dto.PaymentCancelResponseDto;
import ua.rent.masters.easystay.dto.PaymentResponseDto;
import ua.rent.masters.easystay.model.User;
import ua.rent.masters.easystay.service.PaymentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_USER')")
    public PaymentResponseDto getPaymentById(@PathVariable Long id,
                                             @AuthenticationPrincipal User user) {
        return paymentService.getPaymentById(id, user);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public List<PaymentResponseDto> getAllPayments(@AuthenticationPrincipal User user) {
        return paymentService.getAllPayments(user);
    }

    @ResponseStatus(HttpStatus.SEE_OTHER)
    @GetMapping("/create-session/{bookingId}")
    public void createPaymentSession(@PathVariable Long bookingId,
                                     HttpServletResponse response) throws Exception {
        String sessionUrl = paymentService.createPaymentSession(bookingId);
        response.setHeader(HttpHeaders.LOCATION, sessionUrl);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/success")
    public PaymentResponseDto handlePaymentSuccess(@RequestParam("session_id") String sessionId) {
        paymentService.handlePaymentSuccess(sessionId);
        return paymentService.getPaymentBySessionId(sessionId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/cancel")
    public PaymentCancelResponseDto handlePaymentCancel(
            @RequestParam("session_id") String sessionId) {
        return paymentService.handlePaymentCanceling(sessionId);
    }
}
