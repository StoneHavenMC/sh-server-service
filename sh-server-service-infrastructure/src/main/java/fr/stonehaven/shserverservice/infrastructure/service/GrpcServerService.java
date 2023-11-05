package fr.stonehaven.shserverservice.infrastructure.service;

import com.google.protobuf.Empty;
import fr.stonehaven.server.*;
import fr.stonehaven.shserverservice.core.domain.SHServer;
import fr.stonehaven.shserverservice.core.enums.ServerStatus;
import fr.stonehaven.shserverservice.core.exceptions.ServerNotFoundException;
import fr.stonehaven.shserverservice.core.exceptions.ServerStatusNotFoundException;
import fr.stonehaven.shserverservice.core.services.IServerService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class GrpcServerService extends ServerServiceGrpc.ServerServiceImplBase {

    private final IServerService serverService;

    public GrpcServerService(IServerService serverService) {
        this.serverService = serverService;
    }

    @Override
    public void updateServer(Server request, StreamObserver<ServerResponse> responseObserver) {
        ServerResponse.Builder builder = ServerResponse.newBuilder();
        try {
            SHServer server = new SHServer();
            server.setId(request.getId());
            server.setServerType(request.getServerType());
            server.setStatus(ServerStatus.getById(request.getStatus().name()));
            server.setHost(request.getHost());
            server.setPort(request.getPort());
            server.setGRpcPort(request.getGRpcPort());
            server.setGRpcApiKey(request.getGRpcApiKey());
            server.setPlayers(request.getPlayers());
            server.setMaxPlayers(request.getMaxPlayers());
            server.setUpdatedAt(request.getUpdatedAt());
            server.setCreatedAt(request.getCreatedAt());
            server = serverService.update(server);

            builder.setServer(Server.newBuilder()
                    .setId(server.getId())
                    .setServerType(server.getServerType())
                    .setStatus(fr.stonehaven.server.ServerStatus.valueOf(server.getStatus().name()))
                    .setHost(server.getHost())
                    .setPort(server.getPort())
                    .setGRpcPort(server.getGRpcPort())
                    .setGRpcApiKey(server.getGRpcApiKey())
                    .setPlayers(server.getPlayers())
                    .setMaxPlayers(server.getMaxPlayers())
                    .setUpdatedAt(server.getUpdatedAt())
                    .setCreatedAt(server.getCreatedAt())
                    .build());
        } catch (ServerStatusNotFoundException e) {
            builder.setError(ServerErrorResponse.newBuilder().setMessage("STATUS_NOT_FOUND").build());
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getServers(Empty request, StreamObserver<ServerListResponse> responseObserver) {
        long start = System.currentTimeMillis();
        ServerListResponse.Builder builder = ServerListResponse.newBuilder();
        for (SHServer server : serverService.getServers()) {
            builder.addServers(Server.newBuilder()
                    .setId(server.getId())
                    .setServerType(server.getServerType())
                    .setStatus(fr.stonehaven.server.ServerStatus.valueOf(server.getStatus().name()))
                    .setHost(server.getHost())
                    .setPort(server.getPort())
                    .setGRpcPort(server.getGRpcPort())
                    .setGRpcApiKey(server.getGRpcApiKey())
                    .setPlayers(server.getPlayers())
                    .setMaxPlayers(server.getMaxPlayers())
                    .setUpdatedAt(server.getUpdatedAt())
                    .setCreatedAt(server.getCreatedAt())
                    .build()
            );
        }
        builder.build();
        System.out.println("getServers took " + (System.currentTimeMillis()-start) + " ms!");
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getBestServer(GetBestServerRequest request, StreamObserver<ServerResponse> responseObserver) {
        ServerResponse.Builder builder = ServerResponse.newBuilder();
        try {
            SHServer server = serverService.getBestServer(request.getServerType());

            builder.setServer(Server.newBuilder()
                    .setId(server.getId())
                    .setServerType(server.getServerType())
                    .setStatus(fr.stonehaven.server.ServerStatus.valueOf(server.getStatus().name()))
                    .setHost(server.getHost())
                    .setPort(server.getPort())
                    .setGRpcPort(server.getGRpcPort())
                    .setGRpcApiKey(server.getGRpcApiKey())
                    .setPlayers(server.getPlayers())
                    .setMaxPlayers(server.getMaxPlayers())
                    .setUpdatedAt(server.getUpdatedAt())
                    .setCreatedAt(server.getCreatedAt())
                    .build());
        } catch (ServerNotFoundException e) {
            builder.setError(ServerErrorResponse.newBuilder().setMessage("SERVER_NOT_FOUND").build());
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getServer(GetServerRequest request, StreamObserver<ServerResponse> responseObserver) {
        ServerResponse.Builder builder = ServerResponse.newBuilder();
        try {
            SHServer server = serverService.getServer(request.getServerId());

            builder.setServer(Server.newBuilder()
                    .setId(server.getId())
                    .setServerType(server.getServerType())
                    .setStatus(fr.stonehaven.server.ServerStatus.valueOf(server.getStatus().name()))
                    .setHost(server.getHost())
                    .setPort(server.getPort())
                    .setGRpcPort(server.getGRpcPort())
                    .setGRpcApiKey(server.getGRpcApiKey())
                    .setPlayers(server.getPlayers())
                    .setMaxPlayers(server.getMaxPlayers())
                    .setUpdatedAt(server.getUpdatedAt())
                    .setCreatedAt(server.getCreatedAt())
                    .build());
        } catch (ServerNotFoundException e) {
            builder.setError(ServerErrorResponse.newBuilder().setMessage("SERVER_NOT_FOUND").build());
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
