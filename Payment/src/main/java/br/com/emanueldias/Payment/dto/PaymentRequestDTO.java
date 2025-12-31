package br.com.emanueldias.Payment.dto;

import java.math.BigDecimal;

public class PaymentRequestDTO {
    private BigDecimal amount;
    private String email;

    public PaymentRequestDTO() {

    }

    public PaymentRequestDTO(BigDecimal amount, String email) {
        this.amount = amount;
        this.email = email;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
