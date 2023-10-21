package fr.stonehaven.shserverservice.core.domain;

import fr.stonehaven.shserverservice.core.enums.ServerStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SHServer {

    private String id;
    private String serverType;
    private ServerStatus status;

    private String host;
    private int port;
    private int gRpcPort;
    private String gRpcApiKey;

    private int players;
    private int maxPlayers;

    private long updatedAt;
    private long createdAt;

}
