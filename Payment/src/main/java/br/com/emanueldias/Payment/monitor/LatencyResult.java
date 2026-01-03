package br.com.emanueldias.Payment.monitor;

public class LatencyResult<T> {

    private final T result;
    private final long latency;

    public LatencyResult(T result, long latency) {
        this.result = result;
        this.latency = latency;
    }

    public T getResult() {
        return result;
    }

    public long getLatency() {
        return latency;
    }
}
