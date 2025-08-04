package org.acme.error.ex2;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("/account")
public class BankingResource {

    // https://www.the-main-thread.com/i/168654387/define-rfc-compliant-error-response

    @Inject
    BankingService bankingService;

    @POST
    @Path("/withdraw")
    public Response withdraw(@QueryParam("amount") @Min(value = 1, message = "Amount must be at least 1") double amount) {
        bankingService.withdraw(amount);
        return Response.ok("Withdrawal successful!").build();
    }

}
