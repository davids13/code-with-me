package org.acme.twitter;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ChirpEventListener {

    private static final Logger LOG = Logger.getLogger(ChirpEventListener.class);

    @Incoming("chirps")
    public void handleChirpEvent(String event) {
        LOG.infof("Received chirp event: %s", event);
    }
}
