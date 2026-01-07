package br.com.emanueldias.Payment.service;

import br.com.emanueldias.Payment.broker.BrokerService;
import br.com.emanueldias.Payment.dto.LogMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
public class StateListener implements SmartLifecycle {

    @Autowired
    private BrokerService brokerService;

    private volatile boolean running = false;

    @Override
    public void start() {
        brokerService.init();
        LogMessage logMessage = new LogMessage(
                "INFO",
                "Serviço de pagamentos inicializado"
        );
        brokerService.sendMessageForLog(logMessage);

        running = true;
    }

    @Override
    public void stop() {
        LogMessage logMessage = new LogMessage(
                "INFO",
                "Serviço de pagamentos encerrado"
        );
        brokerService.sendMessageForLog(logMessage);

        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return Integer.MIN_VALUE;
    }
}

