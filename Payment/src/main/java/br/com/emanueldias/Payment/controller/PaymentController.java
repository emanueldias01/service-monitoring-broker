package br.com.emanueldias.Payment.controller;

import br.com.emanueldias.Payment.dto.PaymentRequestDTO;
import br.com.emanueldias.Payment.dto.PaymentResponseDTO;
import br.com.emanueldias.Payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/")
    public ResponseEntity<PaymentResponseDTO> createPayment(
            @RequestBody PaymentRequestDTO dto
    )
    {
       PaymentResponseDTO response = paymentService.createPayment(dto);
        URI uri = URI.create(response.getId().toString());

        return ResponseEntity.created(uri).body(response);
    }

    @PostMapping("/approve/{paymentId}")
    public ResponseEntity<PaymentResponseDTO> approvePayment(
            @PathVariable UUID paymentId
    )
    {
        return ResponseEntity.ok(paymentService.markAproved(paymentId));
    }

    @PostMapping("/failed/{paymentId}")
    public ResponseEntity<PaymentResponseDTO> markFaliedPayment(
            @PathVariable UUID paymentId
    )
    {
        return ResponseEntity.ok(paymentService.markFaliedPayment(paymentId));
    }
}
