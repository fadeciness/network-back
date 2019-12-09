package ru.rosbank.javaschool.crudapi.ws;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WsChatHandler extends AbstractWebSocketHandler {
  private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

  @Override
  public void afterConnectionEstablished(WebSocketSession webSocketSession) {
    System.out.println("connect");
    sessions.put(webSocketSession.getId(), webSocketSession);
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) {
    for (Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
      try {
        entry.getValue().sendMessage(message);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) {
    sessions.remove(webSocketSession.getId());
  }
}
