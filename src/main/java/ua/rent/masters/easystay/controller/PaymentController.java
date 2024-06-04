package ua.rent.masters.easystay.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Payments", description = "Endpoints for payments processing")
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(
            summary = "Get Payment By Id",
            description = "USER can get payment for theirs bookings, MANAGER can get "
                    + "any payment")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_USER')")
    public PaymentResponseDto getPaymentById(@PathVariable Long id,
                                             @AuthenticationPrincipal User user) {
        return paymentService.getPaymentById(id, user);
    }

    @Operation(
            summary = "Get All Payments",
            description = "USER can get all payments for theirs bookings, MANAGER get all "
                    + "payments of all users")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public List<PaymentResponseDto> getAllPayments(@AuthenticationPrincipal User user) {
        return paymentService.getAllPayments(user);
    }

    @Operation(
            summary = "Create Payment Session",
            description = "Anyone can do payment for anyone. No roles restrictions")
    @ResponseStatus(HttpStatus.SEE_OTHER)
    @GetMapping("/create-session/{bookingId}")
    public void createPaymentSession(@PathVariable Long bookingId,
                                     HttpServletResponse response) throws Exception {
        String sessionUrl = paymentService.createPaymentSession(bookingId);
        response.setHeader(HttpHeaders.LOCATION, sessionUrl);
    }

    @Operation(
            summary = "Success URL",
            description = "Payment system can access here to inform that payment was done.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/success")
    public PaymentResponseDto handlePaymentSuccess(@RequestParam("session_id") String sessionId) {
        paymentService.handlePaymentSuccess(sessionId);
        return paymentService.getPaymentBySessionId(sessionId);
    }

    @Operation(
            summary = "Cancel URL",
            description = "Payment system can access here to inform that payment was canceled.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/cancel")
    public PaymentCancelResponseDto handlePaymentCancel(
            @RequestParam("session_id") String sessionId) {
        return paymentService.handlePaymentCanceling(sessionId);
    }
}
