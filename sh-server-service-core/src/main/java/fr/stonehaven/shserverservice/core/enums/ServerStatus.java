package fr.stonehaven.shserverservice.core.enums;

import fr.stonehaven.shserverservice.core.exceptions.ServerStatusNotFoundException;

public enum ServerStatus {

    STARTING,
    ONLINE,
    SHUTTING_DOWN
    ;

    public static ServerStatus getById(String id) throws ServerStatusNotFoundException {
        try {
            return valueOf(id);
        } catch (Exception e) {
            throw new ServerStatusNotFoundException();
        }
    }
}
