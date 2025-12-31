package br.com.emanueldias.Payment.service;

import br.com.emanueldias.Payment.broker.BrokerService;
import br.com.emanueldias.Payment.dto.EmailMessage;
import br.com.emanueldias.Payment.dto.LogMessage;
import br.com.emanueldias.Payment.dto.PaymentRequestDTO;
import br.com.emanueldias.Payment.dto.PaymentResponseDTO;
import br.com.emanueldias.Payment.model.Payment;
import br.com.emanueldias.Payment.model.PaymentStatus;
import br.com.emanueldias.Payment.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BrokerService brokerService;
    private final ModelMapper modelMapper;

    public PaymentService(PaymentRepository paymentRepository, BrokerService brokerService,ModelMapper modelMapper) {
        this.paymentRepository = paymentRepository;
        this.modelMapper = modelMapper;
        this.brokerService = brokerService;
    }

    @Transactional
    public PaymentResponseDTO createPayment(PaymentRequestDTO dto) {
        Payment payment = modelMapper.map(dto, Payment.class);
        paymentRepository.save(payment);

        LogMessage logMessage = new LogMessage("INFO", "Pagamento criado: %s".formatted(payment.toString()));
        brokerService.sendMessageForLog(logMessage);

        EmailMessage message = new EmailMessage(PaymentStatus.CREATED, payment);
        brokerService.sendMessageForEmail(message);

        return modelMapper.map(payment, PaymentResponseDTO.class);
    }

    public PaymentResponseDTO markAproved(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado"));
        payment.approve();
        paymentRepository.save(payment);

        //enviar mensagem para broker(LOG -> OPERACAO | EMAIL -> PAGAMENTO APROVADO)

        LogMessage logMessage = new LogMessage("INFO", "Pagamento aprovado: %s".formatted(payment.toString()));
        brokerService.sendMessageForLog(logMessage);

        EmailMessage message = new EmailMessage(PaymentStatus.APPROVED, payment);
        brokerService.sendMessageForEmail(message);

        return modelMapper.map(payment, PaymentResponseDTO.class);
    }

    public PaymentResponseDTO markFaliedPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado"));
        if(payment.getStatus().equals(PaymentStatus.APPROVED) || payment.getStatus().equals(PaymentStatus.FAILED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este pagamento já foi processado");
        }
        payment.markFailed();
        paymentRepository.save(payment);

        //enviar mensagem para broker(LOG -> OPERACAO | EMAIL -> PAGAMENTO FALHO)
        LogMessage logMessage = new LogMessage("INFO", "Pagamento falho: %s".formatted(payment.toString()));
        brokerService.sendMessageForLog(logMessage);

        EmailMessage message = new EmailMessage(PaymentStatus.FAILED, payment);
        brokerService.sendMessageForEmail(message);

        return modelMapper.map(payment, PaymentResponseDTO.class);
    }
}
