package br.com.emanueldias.Payment.monitor;


public class WarningSimulator {

    public static void simulateHighLatency() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
