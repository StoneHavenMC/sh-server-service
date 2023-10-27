package fr.stonehaven.shserverservice.infrastructure.service;

import com.google.protobuf.Empty;
import fr.stonehaven.server.Host;
import fr.stonehaven.server.ServerNetworkServiceGrpc;
import fr.stonehaven.shserverservice.infrastructure.configuration.ClientIPInterceptor;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class GrpcServerNetworkService extends ServerNetworkServiceGrpc.ServerNetworkServiceImplBase {

    @Override
    public void getAddress(Empty request, StreamObserver<Host> responseObserver) {
        Host.Builder builder = Host.newBuilder();
        String clientAddress = ClientIPInterceptor.CLIENT_IP_KEY.get();
        builder.setHost(clientAddress);
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
