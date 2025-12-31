package br.com.emanueldias.Payment.broker;

import br.com.emanueldias.Payment.dto.EmailMessage;
import br.com.emanueldias.Payment.dto.LogMessage;
import br.com.emanueldias.client.Client;
import br.com.emanueldias.message.Message;
import br.com.emanueldias.message.MessageQueueSelection;
import br.com.emanueldias.message.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class BrokerService {

    private final Client client;

    @Value("${ip.broker}")
    private String ipBroker;

    @Value("${port.broker}")
    private int portBroker;

    public BrokerService(Client client) {
        this.client = client;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        conectedQueueLog(ipBroker, portBroker);
        conectedQueueEmail(ipBroker, portBroker);
    }

    private void conectedQueueLog(String ip, int port) {
        try {
            this.client.createSocket(ip, port);
            MessageQueueSelection messageQueueSelection = new MessageQueueSelection("logg.server", Role.PRODUCER);
            this.client.sendConnectionMessage(messageQueueSelection);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void conectedQueueEmail(String ip, int port) {
        try {
            this.client.createSocket(ip, port);
            MessageQueueSelection messageQueueSelection = new MessageQueueSelection("email.service", Role.PRODUCER);
            this.client.sendConnectionMessage(messageQueueSelection);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessageForEmail(EmailMessage emailMessage) {
        Message message = new Message("payment", emailMessage);
        try {
            this.client.sendMessage(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendMessageForLog(LogMessage logMessage) {
        Message message = new Message("payment", logMessage);
        try {
            this.client.sendMessage(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
