package org.acme.websocket;

import io.quarkus.websockets.next.OnClose;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.inject.Inject;

@WebSocket(path = "/chat/{username}")
public class ChatWebSocket {

    @Inject
    WebSocketConnection connection; // represents the connection to the client.

    // Declare the type of messages that can be sent and received
    public enum MessageType {
        USER_JOINED, USER_LEFT, CHAT_MESSAGE
    }

    public record ChatMessage(MessageType type, String from, String message) {
    }

    /**
     * This method is called when a new client connects.
     * The broadcast = true attribute indicates that the returned message should be sent to all connected clients.
     *
     * @return
     */
    @OnOpen(broadcast = true)
    public ChatMessage onOpen() {
        return new ChatMessage(MessageType.USER_JOINED, connection.pathParam("username"), null);
    }

    /**
     * This method is called when a client disconnects.
     * The method uses the WebSocketConnection to broadcast a message to all remaining connected clients.
     */
    @OnClose
    public void onClose() {
        ChatMessage departure = new ChatMessage(MessageType.USER_LEFT, connection.pathParam("username"), null);
        connection.broadcast().sendTextAndAwait(departure);
    }

    /**
     * This method is called when a client sends a message.
     * The broadcast = true attribute indicates that the returned message should be sent to all connected clients.
     * Here, we just returns the received (text) message.
     * @param message
     * @return
     */
    @OnTextMessage(broadcast = true)
    public ChatMessage onMessage(ChatMessage message) {
        return message;
    }

}
