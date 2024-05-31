package ua.rent.masters.easystay.controller;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.rent.masters.easystay.dto.PaymentDto;
import ua.rent.masters.easystay.dto.PaymentResponseDto;
import ua.rent.masters.easystay.service.PaymentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PaymentDto getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PaymentDto> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @ResponseStatus(HttpStatus.SEE_OTHER) // In reality must have @ResponseStatus(HttpStatus.SEE_OTHER)
    @PostMapping("/create-session")
    public void createPaymentSession(@RequestParam Long bookingId,
                                                   HttpServletResponse response) {
        PaymentResponseDto responseDto = paymentService.createPaymentSession(bookingId);
        //information must be return in headers somehow
        response.setHeader(HttpHeaders.LOCATION, responseDto.getSessionUrl());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/success")
    public String handlePaymentSuccess(@RequestParam String sessionId) {
        paymentService.handlePaymentSuccess(sessionId);
        return "Successful!";
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/cancel")
    public String handlePaymentCancel(String sessionId) {
        paymentService.handlePaymentCanceling(sessionId);
        return "Canceled!";
    }
}
