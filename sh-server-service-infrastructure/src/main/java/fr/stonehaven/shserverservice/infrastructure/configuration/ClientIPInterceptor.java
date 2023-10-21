package fr.stonehaven.shserverservice.infrastructure.configuration;

import io.grpc.*;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;

@GrpcGlobalServerInterceptor
public class ClientIPInterceptor implements ServerInterceptor {
    public static final Context.Key<String> CLIENT_IP_KEY = Context.key("client-ip");

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        // Extract the client's IP address from the metadata
        String clientIP = headers.get(Metadata.Key.of("x-forwarded-for", Metadata.ASCII_STRING_MARSHALLER));

        // Put the client's IP address in the call context for access in the service implementation
        Context context = Context.current().withValue(CLIENT_IP_KEY, clientIP);
        return Contexts.interceptCall(context, call, headers, next);
    }
}