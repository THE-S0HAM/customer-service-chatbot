package com.chatbot.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WikipediaService {

    private static final String WIKIPEDIA_API_URL = "https://en.wikipedia.org/api/rest_v1/page/summary/";

    public String fetchWikipediaSummary(String topic) {
        RestTemplate restTemplate = new RestTemplate();
        String url = WIKIPEDIA_API_URL + topic;
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            return "No information found for the topic: " + topic;
        }
    }
}