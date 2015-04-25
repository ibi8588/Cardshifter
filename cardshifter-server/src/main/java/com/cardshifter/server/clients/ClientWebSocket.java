package com.cardshifter.server.clients;

import com.cardshifter.api.serial.ByteTransformer;
import net.zomis.cardshifter.ecs.usage.CardshifterIO;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;

import com.cardshifter.api.ClientIO;
import com.cardshifter.api.messages.Message;
import com.cardshifter.server.model.Server;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

public class ClientWebSocket extends ClientIO {
	private static final Logger logger = LogManager.getLogger(ClientWebSocket.class);
	
	private final WebSocket conn;
    private final ByteTransformer transformer = CardshifterIO.createByteTransformer();

	public ClientWebSocket(Server server, WebSocket conn) {
		super(server);
		this.conn = conn;
	}
	
	@Override
	public void close() {
		logger.info("Manual close " + this);
		conn.close();
	}

	@Override
	protected void onSendToClient(Message message) {
		String data;
		try {
            byte[] bytes = transformer.transform(message);
            data = Base64.getEncoder().encodeToString(bytes);
            logger.info("Sending to client: " + message + " - " + Arrays.toString(bytes));
            conn.send(data);
		} catch (IOException e) {
            throw new RuntimeException(e);
        }
	}

	@Override
	public String getRemoteAddress() {
		return conn.getRemoteSocketAddress().toString();
	}

}
