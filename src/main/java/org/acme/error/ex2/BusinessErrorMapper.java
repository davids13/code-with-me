package org.acme.error.ex2;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BusinessErrorMapper implements ExceptionMapper<WebApplicationException> {

    // https://www.the-main-thread.com/i/168654387/create-exception-mappers-for-clean-responses
    // create Exception mappers for clean responses

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(WebApplicationException e) {
        ProblemDetail problemDetail = new ProblemDetail();
        //problemDetail.type = "about:blank";
        //problemDetail.type = String.valueOf(requestContext.getUriInfo().getAbsolutePath());
        problemDetail.type = String.valueOf(uriInfo.getAbsolutePath());
        problemDetail.title = "Business Error";
        problemDetail.status = e.getResponse().getStatus();
        problemDetail.detail = e.getMessage();
        return Response.status(problemDetail.status).entity(problemDetail).build();
    }

}
