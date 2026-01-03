package br.com.emanueldias.Payment.monitor;

import java.util.function.Supplier;

public class LatencyMonitor {

    private static final long LATENCY_THRESHOLD_MS = 1000;

    public static <T> LatencyResult<T> measure(Supplier<T> supplier) {
        long start = System.currentTimeMillis();
        T result = supplier.get();
        long latency = System.currentTimeMillis() - start;
        return new LatencyResult<>(result, latency);
    }

    public static boolean isLatencyHigh(long latency) {
        return latency >= LATENCY_THRESHOLD_MS;
    }
}

