package fr.stonehaven.shserverservice.infrastructure.configuration;

import io.grpc.*;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

@GrpcGlobalServerInterceptor
public class ApiKeyAuthInterceptor implements ServerInterceptor {

    @Value("${grpc.api_key}")
    private String GRPC_API_KEY;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {

        Metadata.Key<String> apiKeyMetadata = Metadata.Key.of("x-api-key", Metadata.ASCII_STRING_MARSHALLER);
        String apiKey = metadata.get(apiKeyMetadata);

        if (Objects.nonNull(apiKey) && apiKey.equals(System.getenv("GRPC_API_KEY"))) {
            return serverCallHandler.startCall(serverCall, metadata);
        } else {
            Status status = Status.UNAUTHENTICATED.withDescription("Invalid api-key");
            serverCall.close(status, metadata);
        }
        return new ServerCall.Listener<>() {
        };
    }
}