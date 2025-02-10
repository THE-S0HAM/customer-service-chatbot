package com.chatbot.service;

import com.chatbot.model.ChatRequest;
import com.chatbot.model.ChatResponse;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    public ChatResponse processQuery(ChatRequest chatRequest) {
        // Mock implementation, integrate Wikipedia and ChatGPT logic here
        String userQuery = chatRequest.getQuery();

        // Placeholder response
        return new ChatResponse("You asked: " + userQuery);
    }
}