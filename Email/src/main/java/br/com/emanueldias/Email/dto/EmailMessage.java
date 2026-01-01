package br.com.emanueldias.Email.dto;

public class EmailMessage {
    private PaymentStatus status;
    private Payment payment;

    public EmailMessage() {
    }

    public EmailMessage(PaymentStatus status, Payment payment) {
        this.status = status;
        this.payment = payment;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}

