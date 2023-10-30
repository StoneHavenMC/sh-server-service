package fr.stonehaven.shserverservice.infrastructure.configuration;

import io.grpc.*;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;

@GrpcGlobalServerInterceptor
public class ClientIPInterceptor implements ServerInterceptor {
    public static final Context.Key<String> CLIENT_IP_KEY = Context.key("client-ip");
    public static final Metadata.Key<String> FORWARDED_FOR_KEY = Metadata.Key.of("X-FORWARDED-FOR", Metadata.ASCII_STRING_MARSHALLER);
    public static final Metadata.Key<String> REAL_IP_KEY = Metadata.Key.of("X-REAL-IP", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        // Extract the client's IP address from the metadata
        String clientIP = call.getAttributes().get(Grpc.TRANSPORT_ATTR_REMOTE_ADDR).toString();
        clientIP = clientIP.replace("/", "");
        clientIP = clientIP.split(":")[0];
        System.out.println("Received request from " + clientIP);

        clientIP = headers.get(FORWARDED_FOR_KEY);
        System.out.println("Received request from " + clientIP);

        clientIP = headers.get(REAL_IP_KEY);
        System.out.println("Received request from " + clientIP);

        // Put the client's IP address in the call context for access in the service implementation
        Context context = Context.current().withValue(CLIENT_IP_KEY, clientIP);
        return Contexts.interceptCall(context, call, headers, next);
    }
}