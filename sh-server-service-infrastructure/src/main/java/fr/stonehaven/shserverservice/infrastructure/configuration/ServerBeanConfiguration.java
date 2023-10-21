package fr.stonehaven.shserverservice.infrastructure.configuration;

import fr.stonehaven.shserverservice.core.services.IServerService;
import fr.stonehaven.shserverservice.core.services.ServerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerBeanConfiguration {

    @Bean
    IServerService serverService() {
        return new ServerService();
    }

}
