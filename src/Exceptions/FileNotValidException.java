package Exceptions;

public class FileNotValidException extends RuntimeException {
    public FileNotValidException(String message) {
        super(message);
    }
}
