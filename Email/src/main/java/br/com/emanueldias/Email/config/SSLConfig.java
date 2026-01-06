package br.com.emanueldias.Email.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class SSLConfig {

    @Value("${broker.ssl.trust-store}")
    private String trustStoreFileName;

    @Value("${broker.ssl.trust-store-password}")
    private String trustStorePassword;

    @Value("${broker.ssl.trust-store-type}")
    private String trustStoreType;

    @PostConstruct
    public void initSslSystemProperties() {
        ApplicationHome home = new ApplicationHome(getClass());
        File jarDir = home.getDir();

        File trustStoreFile = new File(jarDir, trustStoreFileName);

        if (!trustStoreFile.exists()) {
            System.err.println("ERRO CRÍTICO: Truststore não encontrado em: " + trustStoreFile.getAbsolutePath());
        }

        System.setProperty("javax.net.ssl.trustStore", trustStoreFile.getAbsolutePath());
        System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
        System.setProperty("javax.net.ssl.trustStoreType", trustStoreType);

        System.out.println("SSL System Properties configuradas.");
        System.out.println("Truststore carregado de: " + trustStoreFile.getAbsolutePath());
    }
}