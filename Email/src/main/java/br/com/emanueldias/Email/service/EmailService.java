package br.com.emanueldias.Email.service;

import br.com.emanueldias.Email.broker.BrokerService;
import br.com.emanueldias.Email.dto.EmailMessage;
import br.com.emanueldias.Email.dto.LogMessage;
import br.com.emanueldias.Email.dto.Payment;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import static br.com.emanueldias.Email.dto.PaymentStatus.*;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final BrokerService brokerService;

    public EmailService(JavaMailSender mailSender, @Lazy BrokerService brokerService) {
        this.mailSender = mailSender;
        this.brokerService = brokerService;
    }

    private final String EMAIL_TEMPLATE = """
        <html>
        <body style="font-family: sans-serif; color: #333;">
            <div style="max-width: 600px; margin: 0 auto; border: 1px solid #ddd; border-radius: 8px; overflow: hidden;">
                <div style="background-color: %s; padding: 20px; text-align: center; color: white;">
                    <h2>%s</h2>
                </div>
                <div style="padding: 20px;">
                    <p>Olá, <strong>%s</strong>,</p>
                    <p>%s</p>
                    <hr style="border: 0; border-top: 1px solid #eee;" />
                    <p><strong>Resumo:</strong></p>
                    <ul>
                        <li><strong>ID:</strong> %s</li>
                        <li><strong>Valor:</strong> R$ %s</li>
                    </ul>
                </div>
            </div>
        </body>
        </html>
        """;

    public void sendEmailForPayment(EmailMessage emailMessage) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            Payment payment = emailMessage.getPayment();
            String color;
            String title;
            String description;

            switch (emailMessage.getStatus()) {
                case CREATED -> {
                    color = "#2c3e50"; title = "Pagamento Criado";
                    description = "Seu pedido de pagamento foi recebido com sucesso!";
                }
                case APPROVED -> {
                    color = "#27ae60"; title = "Pagamento Aprovado";
                    description = "Confirmamos o recebimento do seu pagamento.";
                }
                case FAILED -> {
                    color = "#c0392b"; title = "Falha no Pagamento";
                    description = "Não conseguimos processar seu pagamento. Verifique seus dados.";
                }
                default -> {
                    color = "#7f8c8d"; title = "Atualização";
                    description = "O status do seu pagamento foi alterado.";
                }
            }

            String htmlContent = EMAIL_TEMPLATE.formatted(
                    color, title, payment.getClientName(), description,
                    payment.getId(), payment.getAmount()
            );

            helper.setFrom("noreply@redes.com");
            helper.setTo(payment.getEmail());
            helper.setSubject("[Pagamento] " + title);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

            LogMessage logMessage = new LogMessage(
                    "INFO",
                    "E-mail enviado com sucesso para: " + payment.getEmail()
            );

            brokerService.sendMessageForLog(logMessage);
            System.out.println("E-mail enviado com sucesso para: " + payment.getEmail());

        } catch (MessagingException e) {
            throw new RuntimeException("Falha ao enviar e-mail", e);
        }
    }
}