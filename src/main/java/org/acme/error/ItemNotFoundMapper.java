package org.acme.error;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider // register the mapper with jax-rs
public class ItemNotFoundMapper implements ExceptionMapper<ItemNotFoundException> {

    @Override
    public Response toResponse(ItemNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("ITEM_NOT_FOUND_MAPPED", ex.getMessage());
        return Response.status(Response.Status.NOT_FOUND).entity(error).build();
    }

}
