package org.example.exception;

public class ExistStudentException extends RuntimeException{
    public ExistStudentException(String message) {
        super(message);
    }
}
