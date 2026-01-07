package br.com.emanueldias.Email.service;

import br.com.emanueldias.Email.broker.BrokerService;
import br.com.emanueldias.Email.dto.LogMessage;
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
                "Serviço de email inicializado"
        );
        brokerService.sendMessageForLog(logMessage);

        running = true;
    }

    @Override
    public void stop() {
        LogMessage logMessage = new LogMessage(
                "INFO",
                "Serviço de email encerrado"
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
