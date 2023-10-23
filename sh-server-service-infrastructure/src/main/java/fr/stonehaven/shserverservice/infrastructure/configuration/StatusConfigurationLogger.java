package fr.stonehaven.shserverservice.infrastructure.configuration;

import fr.stonehaven.shserverservice.core.domain.SHServer;
import fr.stonehaven.shserverservice.core.enums.ServerStatus;
import fr.stonehaven.shserverservice.core.services.IServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class StatusConfigurationLogger {

    private final Logger logger = LoggerFactory.getLogger(StatusConfigurationLogger.class);
    private final IServerService serverService;

    public StatusConfigurationLogger(IServerService serverService) {
        this.serverService = serverService;
    }

    @Scheduled(cron = "*/10 * * * * *")
    public void logCurrentServers() {
        StringBuilder serversLog = new StringBuilder();
        serversLog.append("\n");
        serversLog.append("Servers: ").append(serverService.getServers().size());
        serversLog.append(" | Starting: ").append(serverService.getServers().stream().filter(s -> s.getStatus() == ServerStatus.STARTING).toList().size());
        serversLog.append(" | Online: ").append(serverService.getServers().stream().filter(s -> s.getStatus() == ServerStatus.ONLINE).toList().size());
        serversLog.append(" | Shutting Down: ").append(serverService.getServers().stream().filter(s -> s.getStatus() == ServerStatus.SHUTTING_DOWN).toList().size());
        serversLog.append("\nServers:");

        for (SHServer server : serverService.getServers()) {
            serversLog.append("\n").append(server.getId()).append(" [").append(server.getServerType()).append("]")
                    .append("   | Host: ").append(server.getHost()).append(":").append(server.getPort())
                    .append("   | Status: ").append(server.getStatus())
                    .append("   | Players: ").append(server.getPlayers()).append("/").append(server.getMaxPlayers())
                    .append("   | Last Ping: ").append((System.currentTimeMillis() - server.getUpdatedAt())).append(" ms ago");
        }

        logger.info(serversLog.toString());
    }
}
