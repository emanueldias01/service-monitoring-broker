package br.com.emanueldias.Log.broker;

import br.com.emanueldias.Log.dto.LogMessage;
import br.com.emanueldias.Log.repository.LogRepository;
import br.com.emanueldias.Log.service.LogService;
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

import java.io.EOFException;
import java.io.IOException;

@Service
public class BrokerService {

    @Autowired
    private Client clientLog;

    @Autowired
    private LogService logService;

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
                consumerQueueLog();
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
            this.clientLog.createSocket(ipBroker, portBroker);
            MessageQueueSelection sel = new MessageQueueSelection("logg.server", Role.CONSUMER);
            this.clientLog.sendConnectionMessage(sel);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("não foi possível se conectar ao broker:");
            e.printStackTrace();
        }
    }

    public void consumerQueueLog() {
        while (true) {
            try {
                Message message = this.clientLog.listenMessage();
                try {
                    LogMessage dto = objectMapper.readValue(
                            message.getBodyMessage(),
                            LogMessage.class
                    );
                    logService.persistMessage(dto);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }catch (IllegalAccessError ex) {
                System.err.println("Não foi possível consumir a fila de log:");
                ex.printStackTrace();
                break;
            }

        }
    }
}
