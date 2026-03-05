package edu.lopezalejandro._aMarcha.controllers;

import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import edu.lopezalejandro._aMarcha.services.PayPalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/paypal")
@CrossOrigin(origins = "*")
public class PayPalController {

    @Autowired
    private PayPalService payPalService;

    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestBody Map<String, Object> datos) {

        try {

            // Convertir correctamente el tipo de suscripción
            int tipoSuscripcion = ((Number) datos.get("tipoSuscripcion")).intValue();
            String precio;

            // Establecer precios según el tipo de cuenta
            switch (tipoSuscripcion) {
                case 0: // Superusuario
                    precio = "10.00";
                    break;

                case 1: // Usuario Coche
                    precio = "7.00";
                    break;

                case 2: // Usuario Moto
                    precio = "5.00";
                    break;

                default:
                    return ResponseEntity.badRequest().body("Tipo de suscripción no válido");
            }

            OrdersCreateRequest request = new OrdersCreateRequest();
            request.prefer("return=representation");

            request.requestBody(
                    new OrderRequest()
                            .checkoutPaymentIntent("CAPTURE")
                            .purchaseUnits(List.of(
                                    new PurchaseUnitRequest()
                                            .amountWithBreakdown(
                                                    new AmountWithBreakdown()
                                                            .currencyCode("EUR")
                                                            .value(precio)
                                            )
                            ))
            );

            HttpResponse<Order> response = payPalService.client().execute(request);

            return ResponseEntity.ok(response.result().id());

        } catch (IOException e) {

            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al crear la orden");

        } catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(500).body("Error interno en PayPal");

        }
    }
}