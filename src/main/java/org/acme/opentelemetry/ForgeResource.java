package org.acme.opentelemetry;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("forge")
public class ForgeResource {

    /*
        @WithSpan creates a detailed child span, and
        Span.current().addEvent(...) adds a specific log point within the parent span's timeline.
    */

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String craftSword() throws InterruptedException {
        // this custom span will appear nested inside the JAX-RS span
        workTheBellows();
        Thread.sleep(250); // simulating hard work
        Span.current().addEvent("Sword is quenched");
        return "Legendary Sword forged!";
    }

    @WithSpan("heating-the-metal") // creates a new span for this method
    void workTheBellows() throws InterruptedException {
        Span.current().setAttribute("forge.temperature", "1315ºC");
        Thread.sleep(150);
    }
}
