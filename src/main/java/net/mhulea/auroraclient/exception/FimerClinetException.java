package net.mhulea.auroraclient.exception;

public class FimerClinetException extends Exception{
    public FimerClinetException(String message) {
        super(message);
    }

    public FimerClinetException(String message, Throwable cause) {
        super(cause);
    }
}
