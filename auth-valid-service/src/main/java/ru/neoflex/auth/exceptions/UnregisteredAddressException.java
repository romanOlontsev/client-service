package ru.neoflex.auth.exceptions;

public class UnregisteredAddressException extends RuntimeException {

    public UnregisteredAddressException(String message) {
        super(message);
    }
}
