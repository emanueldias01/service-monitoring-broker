package br.com.emanueldias.Email.broker;

import br.com.emanueldias.Email.dto.EmailMessage;
import br.com.emanueldias.Email.dto.LogMessage;
import br.com.emanueldias.Email.service.EmailService;
import br.com.emanueldias.client.Client;
import br.com.emanueldias.message.Message;
import br.com.emanueldias.message.MessageQueueSelection;
import br.com.emanueldias.message.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Service
public class BrokerService {

    private Client clientLog;
    private Client clientEmail;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${ip.broker}")
    private String ipBroker;

    @Value("${port.broker}")
    private int portBroker;

    public void init() {
        this.clientLog = new Client();
        this.clientEmail = new Client();
        connectQueueLog();
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
            clientEmail.createSocket(ipBroker, portBroker);
            MessageQueueSelection sel = new MessageQueueSelection("email.service", Role.CONSUMER);
            clientEmail.sendConnectionMessage(sel);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void connectQueueLog() {
        try {
            this.clientLog.createSocket(ipBroker, portBroker);
            MessageQueueSelection sel = new MessageQueueSelection("logg.server", Role.PRODUCER);
            clientLog.sendConnectionMessage(sel);
        } catch (Exception e) {
            System.err.println("Erro ao conectar fila de Log: " + e.getMessage());
        }
    }

    public void sendMessageForLog(LogMessage logMessage) {
        String messageJsonString = objectMapper.writeValueAsString(logMessage);
        Message message = new Message("email", messageJsonString);
        try {
            clientLog.sendMessage(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
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
