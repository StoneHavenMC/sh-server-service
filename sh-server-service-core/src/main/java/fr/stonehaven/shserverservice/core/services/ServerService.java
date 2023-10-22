package fr.stonehaven.shserverservice.core.services;

import fr.stonehaven.shserverservice.core.domain.SHServer;
import fr.stonehaven.shserverservice.core.exceptions.ServerNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ServerService implements IServerService {

    private final Logger logger = LoggerFactory.getLogger(ServerService.class);

    private final Map<String, SHServer> servers;

    private final ScheduledExecutorService executorService;
    private Thread serverHealthChecker;

    public ServerService() {
        this.servers = new HashMap<>();
        this.executorService = Executors.newScheduledThreadPool(1);
        startHealthChecker();
    }

    private void startHealthChecker() {
        executorService.scheduleAtFixedRate(() -> {
            if (serverHealthChecker == null || !serverHealthChecker.isAlive()) {
                serverHealthChecker = new Thread(new ServerHealthChecker(this));
                serverHealthChecker.start();
            }
        }, 0, ServerHealthChecker.HEALTHCHECK_INTERVAL, TimeUnit.MILLISECONDS);
    }

    @Override
    public SHServer update(SHServer server) {
        SHServer existingServer = servers.getOrDefault(server.getId(), null);
        if (existingServer == null) {
            existingServer = new SHServer();
            existingServer.setId(server.getId());
            existingServer.setCreatedAt(server.getCreatedAt());
            servers.put(existingServer.getId(), existingServer);
            logger.info("Discovered new server " + server.getId() + " of type " + server.getServerType() + " !");
        }

        existingServer.setServerType(server.getServerType());
        existingServer.setStatus(server.getStatus());

        existingServer.setHost(server.getHost());
        existingServer.setPort(server.getPort());
        existingServer.setGRpcPort(server.getGRpcPort());
        existingServer.setGRpcApiKey(server.getGRpcApiKey());

        existingServer.setPlayers(server.getPlayers());
        existingServer.setMaxPlayers(server.getMaxPlayers());

        existingServer.setUpdatedAt(System.currentTimeMillis());
        return existingServer;
    }

    @Override
    public void remove(String serverId) throws ServerNotFoundException {
        SHServer existingServer = servers.getOrDefault(serverId, null);
        if(existingServer == null) throw new ServerNotFoundException();
        servers.remove(serverId);
    }

    @Override
    public Set<SHServer> getServers() {
        return new HashSet<>(servers.values());
    }

    @Override
    public Set<SHServer> getServers(String serverType) {
        return servers.values().stream().filter(s -> s.getServerType().equals(serverType)).collect(Collectors.toSet());
    }

    @Override
    public SHServer getBestServer(String serverType) throws ServerNotFoundException {
        /**
         * Implement LOAD_BALANCER ALGORITHMS HERE
         */
        return getServers(serverType).stream().findAny().orElseThrow(ServerNotFoundException::new);
    }

    @Override
    public SHServer getServer(String serverId) throws ServerNotFoundException {
        SHServer existingServer = servers.getOrDefault(serverId, null);
        if(existingServer == null) throw new ServerNotFoundException();
        return existingServer;
    }
}
