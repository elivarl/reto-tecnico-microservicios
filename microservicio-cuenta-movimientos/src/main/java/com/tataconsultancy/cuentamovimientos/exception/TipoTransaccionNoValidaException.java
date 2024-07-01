package com.tataconsultancy.cuentamovimientos.exception;

public class TipoTransaccionNoValidaException extends RuntimeException{
    private String message;
    public TipoTransaccionNoValidaException(String message) {
        super(message);
        this.message = message;
    }
}
