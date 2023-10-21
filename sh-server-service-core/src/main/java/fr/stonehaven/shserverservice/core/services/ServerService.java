package fr.stonehaven.shserverservice.core.services;

import fr.stonehaven.shserverservice.core.domain.SHServer;
import fr.stonehaven.shserverservice.core.exceptions.ServerNotFoundException;
import lombok.Getter;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

public class ServerService implements IServerService {


    @Getter
    private final CopyOnWriteArraySet<SHServer> servers;

    public ServerService() {
        this.servers = new CopyOnWriteArraySet<>();
    }

    @Override
    public SHServer add(SHServer server) {
        server.setCreatedAt(System.currentTimeMillis());
        server.setUpdatedAt(System.currentTimeMillis());
        servers.add(server);
        return server;
    }

    @Override
    public void remove(String serverId) throws ServerNotFoundException {
        SHServer server = servers.stream().filter(s -> s.getId().equals(serverId)).findFirst().orElseThrow(ServerNotFoundException::new);
        servers.remove(server);
    }

    @Override
    public SHServer update(SHServer server) throws ServerNotFoundException {
        SHServer existingServer = servers.stream().filter(s -> s.getId().equals(server.getId())).findFirst().orElseThrow(ServerNotFoundException::new);

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
    public Set<SHServer> getServers(String serverType) {
        return servers.stream().filter(s -> s.getServerType().equals(serverType)).collect(Collectors.toSet());
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
        return servers.stream().filter(s -> s.getId().equals(serverId)).findFirst().orElseThrow(ServerNotFoundException::new);
    }
}
