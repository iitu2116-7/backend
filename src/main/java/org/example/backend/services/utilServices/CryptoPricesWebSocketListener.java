package org.example.backend.services.utilServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CryptoPricesWebSocketListener extends TextWebSocketHandler {

    private final WebSocketClient webSocketClient;
    private final CryptoPricesService cryptoPricesService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String BINANCE_WS_URL = "wss://stream.binance.com:9443/ws/!ticker@arr";

    @PostConstruct
    public void connect() {
        webSocketClient.doHandshake(this, BINANCE_WS_URL);
    }

    @Override
    public void handleTextMessage(@NotNull WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        JsonNode jsonNode = objectMapper.readTree(payload);

        for (JsonNode ticker : jsonNode) {
            String symbol = ticker.get("s").asText();
            double price = ticker.get("c").asDouble();
            double openPrice = ticker.get("o").asDouble();
            double highPrice = ticker.get("h").asDouble();
            double lowPrice = ticker.get("l").asDouble();
            double volume = ticker.get("v").asDouble();

            cryptoPricesService.updateCryptoPrice(symbol, price, openPrice, highPrice, lowPrice, volume);
        }
    }

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) {
        System.out.println("WebSocket connection established with Binance.");
    }

    @Override
    public void handleTransportError(@NotNull WebSocketSession session, Throwable exception) {
        System.err.println("WebSocket error: " + exception.getMessage());
    }
}