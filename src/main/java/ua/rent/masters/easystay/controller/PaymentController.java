package ua.rent.masters.easystay.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.rent.masters.easystay.dto.payment.PaymentCancelResponseDto;
import ua.rent.masters.easystay.dto.payment.PaymentResponseDto;
import ua.rent.masters.easystay.dto.payment.PaymentResponseSessionUrlDto;
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
            description =
                    "USER can get payment for theirs bookings, MANAGER can get any payment")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_USER')")
    public PaymentResponseDto getPaymentById(@PathVariable Long id,
                                             @AuthenticationPrincipal User user) {
        return paymentService.getPaymentById(id, user);
    }

    @Operation(
            summary = "Get All Payments",
            description = "USER can get page of payments for theirs bookings, MANAGER get page"
                    + " of payments of all users")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public List<PaymentResponseDto> getAllPayments(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @AuthenticationPrincipal User user) {
        Pageable pageable = PageRequest.of(page, size);
        return paymentService.getAllPayments(user, pageable);
    }

    @Operation(
            summary = "Create Payment Session",
            description = "Anyone can make a payment for anyone. No roles restrictions")

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/create-session/{bookingId}")
    public PaymentResponseSessionUrlDto createPaymentSession(@PathVariable Long bookingId)
            throws Exception {
        return new PaymentResponseSessionUrlDto(paymentService.createPaymentSession(bookingId));
    }

    @Operation(
            summary = "Success URL",
            description = "Payment system access to inform that payment was done.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/success")
    public PaymentResponseDto handlePaymentSuccess(@RequestParam("session_id") String sessionId) {
        paymentService.handlePaymentSuccess(sessionId);
        return paymentService.getPaymentBySessionId(sessionId);
    }

    @Operation(
            summary = "Cancel URL",
            description = "Payment system access to inform that payment was canceled.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/cancel")
    public PaymentCancelResponseDto handlePaymentCancel(
            @RequestParam("session_id") String sessionId) {
        return paymentService.handlePaymentCanceling(sessionId);
    }
}
