package net.mhulea.powerplant.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class PowerplantApplicationException extends RuntimeException {
    public PowerplantApplicationException() {
        super();
    }
    public PowerplantApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
    public PowerplantApplicationException(String message) {
        super(message);
    }
    public PowerplantApplicationException(Throwable cause) {
        super(cause);
    }
}