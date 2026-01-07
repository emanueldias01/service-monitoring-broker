package br.com.emanueldias.Payment.broker;

import br.com.emanueldias.Payment.dto.EmailMessage;
import br.com.emanueldias.Payment.dto.LogMessage;
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

    private Client clientLog;
    private Client clientEmail;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${ip.broker}")
    private String ipBroker;

    @Value("${port.broker}")
    private int portBroker;

    public void init() {
        this.clientLog = new Client();
        this.clientEmail = new Client();

        connectQueueLog(ipBroker, portBroker);
        connectQueueEmail(ipBroker, portBroker);
    }

    private void connectQueueLog(String ip, int port) {
        try {
            this.clientLog.createSocket(ip, port);
            MessageQueueSelection sel = new MessageQueueSelection("logg.server", Role.PRODUCER);
            this.clientLog.sendConnectionMessage(sel);
        } catch (Exception e) {
            System.err.println("Erro ao conectar fila de Log: " + e.getMessage());
        }
    }

    private void connectQueueEmail(String ip, int port) {
        try {
            this.clientEmail.createSocket(ip, port);
            MessageQueueSelection sel = new MessageQueueSelection("email.service", Role.PRODUCER);
            this.clientEmail.sendConnectionMessage(sel);
        } catch (Exception e) {
            System.err.println("Erro ao conectar fila de Email: " + e.getMessage());
        }
    }

    public void sendMessageForEmail(EmailMessage emailMessage) {
        String messageJsonString = this.objectMapper.writeValueAsString(emailMessage);
        Message message = new Message("payment", messageJsonString);
        try {
            this.clientEmail.sendMessage(message);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Não foi possível mandar mensagens para a fila de email:");
            e.printStackTrace();
        }
    }

    public void sendMessageForLog(LogMessage logMessage) {
        String messageJsonString = this.objectMapper.writeValueAsString(logMessage);
        Message message = new Message("payment", messageJsonString);
        try {
            this.clientLog.sendMessage(message);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Não foi possível mandar mensagens para a fila de log:");
            e.printStackTrace();
        }
    }
}