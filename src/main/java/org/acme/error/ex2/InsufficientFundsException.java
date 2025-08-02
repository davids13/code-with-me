package org.acme.error.ex2;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class InsufficientFundsException extends WebApplicationException {

    // Custom Business Exceptions

    public InsufficientFundsException(final String message) {
        super(message, Response.Status.BAD_REQUEST);
    }

}
