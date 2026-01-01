package br.com.emanueldias.Email.broker;

import br.com.emanueldias.Email.dto.EmailMessage;
import br.com.emanueldias.Email.service.EmailService;
import br.com.emanueldias.client.Client;
import br.com.emanueldias.message.Message;
import br.com.emanueldias.message.MessageQueueSelection;
import br.com.emanueldias.message.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Service
public class BrokerService {

    @Autowired
    private Client clientEmail;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${ip.broker}")
    private String ipBroker;

    @Value("${port.broker}")
    private int portBroker;


    @EventListener(ApplicationReadyEvent.class)
    public void startConsuming() {
        Thread consumerThread = new Thread(() -> {
            try {
                connectQueuePayment();
                consumerQueueEmailPayment();
            } catch (Exception e) {
                System.err.println("Erro crítico no consumidor: " + e.getMessage());
                e.printStackTrace();
            }
        });

        consumerThread.setName("BrokerConsumerThread");
        consumerThread.start();
    }

    public void connectQueuePayment() {
        try {
            this.clientEmail.createSocket(ipBroker, portBroker);
            MessageQueueSelection sel = new MessageQueueSelection("email.service", Role.CONSUMER);
            this.clientEmail.sendConnectionMessage(sel);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void consumerQueueEmailPayment() {
        while (true) {
            Message message = clientEmail.listenMessage();
            try {
                EmailMessage emailMessage =
                        objectMapper.readValue(
                                message.getBodyMessage(),
                                EmailMessage.class
                        );

                emailService.sendEmailForPayment(emailMessage);

            } catch (Exception e) {
                e.printStackTrace();
            }
//            try {
//                Só pra não explodir de threads criadas
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        }
    }
}
