package br.com.emanueldias.Payment.repository;

import br.com.emanueldias.Payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
