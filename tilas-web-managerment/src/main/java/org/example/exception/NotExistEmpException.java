package org.example.exception;

public class NotExistEmpException extends RuntimeException{
    public NotExistEmpException(String message) {
        super(message);
    }
}
