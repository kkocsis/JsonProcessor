package com.kkocsis.jsonprocessor;

import com.google.common.collect.ImmutableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JsonProcessorApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class JsonProcessorApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Value("${local.server.port}")
    private int port;
    private StompSession stompSession;
    private WebSocketStompClient stompClient;

    private CompletableFuture<Map<?, ?>> completableFuture;

    @Before
    public void setUp() throws Exception {
        completableFuture = new CompletableFuture<>();

        stompClient = new WebSocketStompClient(new SockJsClient(ImmutableList.of(new WebSocketTransport(new StandardWebSocketClient()))));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        String wsURL = "ws://localhost:" + port + "/messages-websocket";
        stompSession = stompClient.connect(wsURL, new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);

        stompSession.subscribe("/topic/message", new CreateMessageStompFrameHandler());
    }

    @After
    public void tearDown() throws Exception {
        stompSession.disconnect();
        stompClient.stop();
    }

    @Test
    public void givenMessage_whenPostMessage_thenSaveToDatabaseAndSendToRedisAndWebsocket() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/messages")
                .content("{\"test\": \"message\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(""));

        Map<?, ?> messageFromWs = completableFuture.get(5, SECONDS);
        assertNotNull(messageFromWs);

        mvc.perform(MockMvcRequestBuilders.get("/messages")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(1)))
                .andExpect(jsonPath("$[0].message.test", equalTo("message")));
    }

    private class CreateMessageStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return Map.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            completableFuture.complete((Map<?, ?>) o);
        }
    }

}
