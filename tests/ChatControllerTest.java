package com.chatbot.tests;

import com.chatbot.controller.ChatController;
import com.chatbot.model.ChatRequest;
import com.chatbot.model.ChatResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChatControllerTest {

    @Test
    public void testChatResponse() {
        ChatController chatController = new ChatController();
        ChatRequest request = new ChatRequest();
        request.setQuery("What is Java?");
        
        ChatResponse response = chatController.chat(request);
        assertNotNull(response);
        assertTrue(response.getResponse().contains("You asked: What is Java?"));
    }
}