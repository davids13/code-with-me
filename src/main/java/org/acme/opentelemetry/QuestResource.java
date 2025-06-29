package org.acme.opentelemetry;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.UUID;

@Path("quests")
public class QuestResource {

    // https://myfear.substack.com/p/quarkus-opentelemetry-microservices-tracing?utm_source=post-email-title&publication_id=4194688&post_id=165769836&utm_campaign=email-post-title&isFreemail=true&r=1om7hi&triedRedirect=true&utm_medium=email

    private static final Logger LOG = Logger.getLogger(QuestResource.class);

    ForgeClient forgeClient;

    @Inject
    public QuestResource(@RestClient ForgeClient forgeClient) {
        this.forgeClient = forgeClient;
    }

    @GET
    @Path("start")
    public String startQuest() {
        LOG.infof("Quest starting! We need a sword.");
        String result = forgeClient.craftSword();
        LOG.info("Quest update: " + result);
        logQuestId();
        Span.current().addEvent("quest#start");
        return "Quest Started! Acquired: " + result;
    }

    @WithSpan("user-id")
    void logQuestId() {
        Span.current().setAttribute("quest.id", UUID.randomUUID().toString());
    }
}
