package br.com.emanueldias.Payment.service;

import br.com.emanueldias.Payment.broker.BrokerService;
import br.com.emanueldias.Payment.dto.LogMessage;
import br.com.emanueldias.Payment.monitor.LatencyMonitor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MonitoringService {

    private final BrokerService brokerService;

    public MonitoringService(BrokerService brokerService) {
        this.brokerService = brokerService;
    }

    public void checkLatency(long latencyMs) {
        if (LatencyMonitor.isLatencyHigh(latencyMs)) {

            var mapInfo = Map.of(
                    "latencyMs", latencyMs,
                    "thresholdMs", 1000
            );
            LogMessage log = new LogMessage(
                    "WARNING",
                    "Serviço de pagamento está com alta latência: ".concat(mapInfo.toString())


            );

            brokerService.sendMessageForLog(log);
        }
    }
}
