package fr.stonehaven.shserverservice.core.exceptions;

public class ServerStatusNotFoundException extends Exception {

    public ServerStatusNotFoundException() {
    }

    public ServerStatusNotFoundException(String message) {
        super(message);
    }
}
