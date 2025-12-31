package br.com.emanueldias.Payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymentApplication {

	public static void main(String[] args) {

		System.setProperty("javax.net.ssl.trustStore", "/Users/emanuel/Documents/Github/service-monitoring-broker/Payment/target/cliente_truststore.p12");
		System.setProperty("javax.net.ssl.trustStorePassword", "123456");
		System.setProperty("javax.net.ssl.trustStoreType", "PKCS12");
		SpringApplication.run(PaymentApplication.class, args);
	}

}
