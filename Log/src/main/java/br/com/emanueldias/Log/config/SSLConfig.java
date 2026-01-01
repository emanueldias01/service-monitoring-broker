package br.com.emanueldias.Log.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SSLConfig {

    @Value("${broker.ssl.trust-store}")
    private String trustStore;

    @Value("${broker.ssl.trust-store-password}")
    private String trustStorePassword;

    @Value("${broker.ssl.trust-store-type}")
    private String trustStoreType;

    @PostConstruct
    public void initSslSystemProperties() {
        String cleanPath = trustStore.replace("file:", "");

        System.setProperty("javax.net.ssl.trustStore", cleanPath);
        System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
        System.setProperty("javax.net.ssl.trustStoreType", trustStoreType);

        System.out.println("SSL System Properties carregadas com sucesso!");
    }
}
