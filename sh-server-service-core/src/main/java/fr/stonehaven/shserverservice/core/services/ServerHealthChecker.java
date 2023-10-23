package fr.stonehaven.shserverservice.core.services;

import fr.stonehaven.shserverservice.core.domain.SHServer;
import fr.stonehaven.shserverservice.core.exceptions.ServerNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerHealthChecker implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(ServerHealthChecker.class);
    private final IServerService serverService;

    public ServerHealthChecker(IServerService serverService) {
        this.serverService = serverService;
    }

    public static final int HEALTHCHECK_INTERVAL = 500;
    private static final int OFFSET_DELAY = 100;

    @Override
    public void run() {
        System.out.println("Running healthcheck daemon");
        for (SHServer server : serverService.getServers()) {
            try {
                long lastPingedAt = System.currentTimeMillis() - server.getUpdatedAt();
                if (lastPingedAt <= (HEALTHCHECK_INTERVAL + OFFSET_DELAY)) return;
                serverService.remove(server.getId());
                logger.info("Server " + server.getId() + " haven't passed the healthcheck, removing it!");
            } catch (ServerNotFoundException e) {
                logger.info("Unable to find server " + server.getId() + "!");
            }
        }
    }
}
