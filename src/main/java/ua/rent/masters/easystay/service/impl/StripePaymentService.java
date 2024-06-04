package ua.rent.masters.easystay.service.impl;

import static com.stripe.param.checkout.SessionCreateParams.LineItem;
import static com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import static com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData.ProductData;

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

    public Session createStripeSession(
            String paymentTitle,
            BigDecimal amount,
            String successUrl,
            String cancelUrl
    ) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(cancelUrl + "?session_id={CHECKOUT_SESSION_ID}")
                .addLineItem(getLineItem(paymentTitle, amount))
                .build();
        return Session.create(params);
    }

    public String getSessionUrl(String sessionId) throws StripeException {
        Session session = Session.retrieve(sessionId);
        return session.getUrl();
    }

    private LineItem getLineItem(String accommodationSimpleName, BigDecimal amount) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(getPriceData(accommodationSimpleName, amount))
                .build();
    }

    private PriceData getPriceData(String accommodationPaymentName, BigDecimal amount) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmount(amount.longValue() * 100)
                .setProductData(getProductData(accommodationPaymentName))
                .build();
    }

    private ProductData getProductData(String accommodationSimpleName) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(accommodationSimpleName)
                .build();
    }
}
