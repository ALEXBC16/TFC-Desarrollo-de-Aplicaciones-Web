package edu.lopezalejandro._aMarcha.services;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.Order;
import com.paypal.orders.OrdersGetRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PayPalService {

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    public PayPalHttpClient client() {
        PayPalEnvironment environment = "sandbox".equals(mode)
                ? new PayPalEnvironment.Sandbox(clientId, clientSecret)
                : new PayPalEnvironment.Live(clientId, clientSecret);
        return new PayPalHttpClient(environment);
    }

    public boolean verificarPago(String orderId) {
        OrdersGetRequest request = new OrdersGetRequest(orderId);
        try {
            HttpResponse<Order> response = client().execute(request);
            return "COMPLETED".equalsIgnoreCase(response.result().status());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
