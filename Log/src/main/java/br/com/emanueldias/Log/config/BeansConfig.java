package br.com.emanueldias.Log.config;

import br.com.emanueldias.client.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class BeansConfig {

    @Bean
    public Client client() {
        return new Client();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
