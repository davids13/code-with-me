package org.acme.websocket;

import io.quarkus.websockets.next.OnClose;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.PathParam;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
import org.jboss.logging.Logger;

import java.util.Map;
/*

@WebSocket(path = "chat/:user")
public class Chat {

    // https://www.baeldung.com/java-quarkus-websockets-next

    public static final Logger LOG = Logger.getLogger(Chat.class);

    // handle msg: text or binary
    @OnTextMessage(broadcast = true)
    public String onMessage(String message, @PathParam("user") String user) {
        return user + ": " + message;
    }

    @OnTextMessage
    public Map<String, String> onTextMessage(String message, WebSocketConnection connection) {
        return Map.of(
                "message", message,
                "connection", connection.toString()
        );
    }

    @OnOpen
    public void onOpen() {
        LOG.info("Connection opened");
    }

    @OnClose
    public void onClose() {
        LOG.info("Connection closed");
    }

    @OnOpen
    public String onOpen(WebSocketConnection connection) {
        return "Hello, " + connection.id();
    }
}
*/
