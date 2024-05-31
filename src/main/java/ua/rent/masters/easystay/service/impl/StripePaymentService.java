package ua.rent.masters.easystay.service.impl;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripePaymentService {
    private final String apiKey;

    public StripePaymentService(@Value("${stripe.api.key}") String apiKey) {
        this.apiKey = apiKey;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey;
    }

    public String createStripeSession(BigDecimal amount, String successUrl, String cancelUrl)
        throws StripeException {
        SessionCreateParams params =
            SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(amount.longValue())
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName("Booking For "
                                                    + "Apartment X")
                                            .build())
                                .build())
                        .build())
                .build();

        Session session = Session.create(params);
        return session.getId();
    }

    public String getSessionUrl(String sessionId) throws StripeException {
        Session session = Session.retrieve(sessionId);
        return session.getUrl();
    }
}
