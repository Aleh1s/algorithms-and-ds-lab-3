package ua.algorithms.exception;

public class RecordAlreadyExistsException extends Exception {

    public RecordAlreadyExistsException() {
    }

    public RecordAlreadyExistsException(String message) {
        super(message);
    }

    public RecordAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecordAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
