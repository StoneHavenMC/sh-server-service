package fr.stonehaven.shserverservice.core.services;

import fr.stonehaven.shserverservice.core.domain.SHServer;
import fr.stonehaven.shserverservice.core.exceptions.ServerNotFoundException;

import java.util.Set;

public interface IServerService {

    SHServer update(SHServer server);

    void remove(String serverId) throws ServerNotFoundException;

    Set<SHServer> getServers();

    Set<SHServer> getServers(String serverType);

    SHServer getBestServer(String serverType) throws ServerNotFoundException;

    SHServer getServer(String id) throws ServerNotFoundException;
}
