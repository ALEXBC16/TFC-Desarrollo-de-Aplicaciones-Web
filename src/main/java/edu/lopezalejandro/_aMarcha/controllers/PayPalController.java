package edu.lopezalejandro._aMarcha.controllers;

import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import edu.lopezalejandro._aMarcha.services.PayPalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/paypal")
@CrossOrigin(origins = "http://localhost:3000")
public class PayPalController {

    @Autowired
    private PayPalService payPalService;

    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder() {
        OrdersCreateRequest request = new OrdersCreateRequest();
        request.prefer("return=representation");
        request.requestBody(new OrderRequest()
                .checkoutPaymentIntent("CAPTURE")
                .purchaseUnits(List.of(
                        new PurchaseUnitRequest()
                                .amountWithBreakdown(new AmountWithBreakdown()
                                        .currencyCode("USD")
                                        .value("5.00")))));

        try {
            HttpResponse<Order> response = payPalService.client().execute(request);
            return ResponseEntity.ok(response.result().id());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al crear la orden");
        }
    }
}
