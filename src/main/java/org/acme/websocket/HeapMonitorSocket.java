package org.acme.websocket;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.websockets.next.OnClose;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OpenConnections;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.text.DecimalFormat;

/**
 * Websocket endpoint at /monitor/heap and push heap usage stats every 3 seconds
 */
@WebSocket(path = "monitor/heap")
public class HeapMonitorSocket {

    private static final Logger LOG = Logger.getLogger(HeapMonitorSocket.class);
    private static final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean(); // gives us portable access to JVM internals like heap usage
    private static final DecimalFormat df = new DecimalFormat("#.##");
    private static final double MEGABYTE = 1024.0 * 1024.0;

    WebSocketConnection webSocketConnection;
    OpenConnections openConnections;

    @Inject
    public HeapMonitorSocket(OpenConnections openConnections, WebSocketConnection webSocketConnection) {
        this.openConnections = openConnections;
        this.webSocketConnection = webSocketConnection;
    }

    @OnOpen // executed when a WebSocket client connects
    void onOpen() {
        LOG.infof("Client connected: %s", webSocketConnection.id());
        sendHeapUsage();
    }

    @OnClose
    void onClose() {
        LOG.infof("Client disconnected: %s", webSocketConnection.id());
    }

    @Scheduled(every = "3s", delay = 1) // Runs sendHeapUsage() every 3 seconds — no need for threads or timers.
    void sendHeapUsage() {
        // Get heap memory usage once
        MemoryUsage usage = memoryBean.getHeapMemoryUsage();
        double usedMB = usage.getUsed() / MEGABYTE;
        String formattedUsage = df.format(usedMB) + " MB";

        LOG.infof("Heap usage: %s", formattedUsage);

        // Filter and send to only open connections
        openConnections.stream() // iterates over all the open connections and sends a text to them
                .filter(WebSocketConnection::isOpen) // Only process open connections
                .forEach(c -> {
                    c.sendTextAndAwait(formattedUsage);
                    LOG.infof("Heap usage sent to connection: %s", c.id());
                });

        // Log skipped connections
        openConnections.stream() // iterates over all the open connections and sends a text to them
                .filter(c -> !c.isOpen())
                .forEach(c -> LOG.warnf("Skipping closed connection: %s", c.id()));
    }
}
