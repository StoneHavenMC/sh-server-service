package fr.stonehaven.shserverservice.core.exceptions;

public class ServerNotFoundException extends Exception {

    public ServerNotFoundException() {
    }

    public ServerNotFoundException(String message) {
        super(message);
    }
}
