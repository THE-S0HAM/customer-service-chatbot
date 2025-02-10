package com.chatbot.tests;

import com.chatbot.controller.ChatController;
import com.chatbot.model.ChatRequest;
import com.chatbot.model.ChatResponse;
import com.chatbot.service.ChatService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChatControllerTest {

    @Test
    public void testChatResponse() {
        // Mock the ChatService
        ChatService chatService = new ChatService();
        
        // Instantiate the ChatController with the mocked service
        ChatController chatController = new ChatController();
        chatController.chatService = chatService; // Inject service dependency
        
        // Create a ChatRequest
        ChatRequest request = new ChatRequest();
        request.setQuery("What is Java?");
        
        // Call the chat method
        ChatResponse response = chatController.chat(request);

        // Assertions
        assertNotNull(response);
        assertTrue(response.getResponse().contains("You asked: What is Java?"));
    }
}
