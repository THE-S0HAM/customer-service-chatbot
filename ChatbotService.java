package com.mindease.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for handling chatbot conversations and responses
 */
public class ChatbotService {
    private static final Logger logger = LoggerFactory.getLogger(ChatbotService.class);
    
    // Crisis keywords that trigger support resources
    private static final List<String> CRISIS_KEYWORDS = Arrays.asList(
            "suicide", "kill myself", "end my life", "want to die", 
            "don't want to live", "hopeless", "can't go on", "self harm",
            "hurt myself", "no reason to live", "better off dead"
    );
    
    // Patterns for matching crisis phrases
    private final List<Pattern> crisisPatterns;
    
    // Response templates for different conversation types
    private final Map<String, List<String>> responseTemplates;
    
    public ChatbotService() {
        // Initialize crisis patterns
        crisisPatterns = new ArrayList<>();
        for (String keyword : CRISIS_KEYWORDS) {
            crisisPatterns.add(Pattern.compile("\\b" + Pattern.quote(keyword) + "\\b", Pattern.CASE_INSENSITIVE));
        }
        
        // Initialize response templates
        responseTemplates = new HashMap<>();
        initializeResponseTemplates();
        
        logger.info("ChatbotService initialized");
    }
    
    /**
     * Processes user input and generates a response
     * @param userInput The user's message
     * @return Chatbot response
     */
    public String processInput(String userInput) {
        // Check for crisis keywords first
        if (containsCrisisKeywords(userInput)) {
            return getCrisisResponse();
        }
        
        // Determine the type of conversation
        String conversationType = determineConversationType(userInput);
        
        // Generate response based on conversation type
        return generateResponse(conversationType, userInput);
    }
    
    /**
     * Checks if the input contains any crisis keywords
     * @param input User input
     * @return true if crisis keywords are detected
     */
    private boolean containsCrisisKeywords(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        
        for (Pattern pattern : crisisPatterns) {
            if (pattern.matcher(input).find()) {
                logger.warn("Crisis keyword detected in user input");
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Gets a crisis response with support resources
     * @return Crisis response message
     */
    private String getCrisisResponse() {
        return "I notice you're expressing some difficult thoughts. " +
               "If you're in crisis, please reach out for immediate help:\n\n" +
               "• National Suicide Prevention Lifeline: 1-800-273-8255\n" +
               "• Crisis Text Line: Text HOME to 741741\n" +
               "• Emergency Services: 911\n\n" +
               "You're not alone, and help is available. Would you like me to provide more resources?";
    }
    
    /**
     * Determines the type of conversation based on user input
     * @param input User input
     * @return Conversation type
     */
    private String determineConversationType(String input) {
        input = input.toLowerCase();
        
        if (input.contains("anxious") || input.contains("anxiety") || input.contains("worried") || input.contains("stress")) {
            return "anxiety";
        } else if (input.contains("sad") || input.contains("depress") || input.contains("unhappy") || input.contains("down")) {
            return "depression";
        } else if (input.contains("happy") || input.contains("joy") || input.contains("good") || input.contains("great")) {
            return "positive";
        } else if (input.contains("meditat") || input.contains("breath") || input.contains("calm") || input.contains("relax")) {
            return "meditation";
        } else if (input.contains("goal") || input.contains("habit") || input.contains("track") || input.contains("progress")) {
            return "goals";
        } else if (input.contains("journal") || input.contains("write") || input.contains("diary") || input.contains("reflect")) {
            return "journaling";
        } else if (input.contains("thought") || input.contains("cbt") || input.contains("reframe") || input.contains("negative")) {
            return "cbt";
        } else if (input.contains("hello") || input.contains("hi ") || input.contains("hey") || input.contains("greetings")) {
            return "greeting";
        } else if (input.contains("bye") || input.contains("goodbye") || input.contains("see you") || input.contains("exit")) {
            return "farewell";
        } else if (input.contains("thank")) {
            return "thanks";
        } else if (input.contains("help") || input.contains("what can you do") || input.contains("features")) {
            return "help";
        } else {
            return "general";
        }
    }
    
    /**
     * Generates a response based on conversation type
     * @param conversationType Type of conversation
     * @param userInput Original user input
     * @return Generated response
     */
    private String generateResponse(String conversationType, String userInput) {
        List<String> templates = responseTemplates.getOrDefault(conversationType, responseTemplates.get("general"));
        
        // Select a random response template
        int index = (int) (Math.random() * templates.size());
        return templates.get(index);
    }
    
    /**
     * Initializes response templates for different conversation types
     */
    private void initializeResponseTemplates() {
        // Anxiety responses
        List<String> anxietyResponses = Arrays.asList(
            "I understand you're feeling anxious. Would you like to try a quick breathing exercise?",
            "Anxiety can be challenging. Have you tried grounding techniques like the 5-4-3-2-1 method?",
            "When you're feeling anxious, it can help to focus on what's in your control. Would you like to talk about that?",
            "I'm here for you during this anxious time. Would a guided meditation help right now?"
        );
        responseTemplates.put("anxiety", anxietyResponses);
        
        // Depression responses
        List<String> depressionResponses = Arrays.asList(
            "I'm sorry you're feeling down. Would you like to talk about what's been happening?",
            "Depression can make everything feel harder. What's one small thing you could do for yourself today?",
            "Remember that your feelings are valid, and it's okay to not be okay. Would journaling help process these emotions?",
            "When you're feeling low, sometimes gentle movement like a short walk can help shift your energy a bit."
        );
        responseTemplates.put("depression", depressionResponses);
        
        // Positive responses
        List<String> positiveResponses = Arrays.asList(
            "I'm glad you're feeling good! What's contributing to your positive mood today?",
            "That's wonderful to hear! Celebrating these good moments is important. Would you like to journal about it?",
            "It's great that you're feeling positive. How can we build on this momentum?",
            "I'm happy you're doing well! Positive emotions are worth savoring and reflecting on."
        );
        responseTemplates.put("positive", positiveResponses);
        
        // Meditation responses
        List<String> meditationResponses = Arrays.asList(
            "Meditation is a powerful practice. Would you like to try a short guided meditation now?",
            "Taking time to breathe and center yourself is so valuable. Our breathing exercise tool might help.",
            "Mindfulness can help bring you back to the present moment. Would you like to explore some techniques?",
            "Even a few minutes of meditation can make a difference. Would you like to set a reminder for regular practice?"
        );
        responseTemplates.put("meditation", meditationResponses);
        
        // Goals responses
        List<String> goalsResponses = Arrays.asList(
            "Setting meaningful goals can help provide direction. What kind of goal are you thinking about?",
            "Tracking your progress can be motivating. Would you like to set up a new goal in the tracker?",
            "Breaking down larger goals into smaller steps can make them more manageable. Would that be helpful?",
            "Celebrating small wins along the way is important for maintaining motivation. How do you acknowledge your progress?"
        );
        responseTemplates.put("goals", goalsResponses);
        
        // Journaling responses
        List<String> journalingResponses = Arrays.asList(
            "Journaling is a great way to process thoughts and feelings. Would you like a prompt to get started?",
            "Writing can help provide clarity. Our guided journaling tool has several themes you might find helpful.",
            "Regular journaling can reveal patterns in your thoughts and emotions. Would you like to start a new entry?",
            "Even a few minutes of reflective writing can be beneficial. What would you like to explore in your journal today?"
        );
        responseTemplates.put("journaling", journalingResponses);
        
        // CBT responses
        List<String> cbtResponses = Arrays.asList(
            "Cognitive reframing can help shift negative thought patterns. Would you like to work through a thought record?",
            "Identifying automatic thoughts is the first step in changing them. What thought would you like to examine?",
            "Looking for evidence that challenges negative thoughts can be eye-opening. Shall we try that approach?",
            "CBT techniques can help create more balanced thinking. Would you like to explore some strategies?"
        );
        responseTemplates.put("cbt", cbtResponses);
        
        // Greeting responses
        List<String> greetingResponses = Arrays.asList(
            "Hello! How are you feeling today?",
            "Hi there! What brings you to MindEase today?",
            "Greetings! How can I support your mental wellbeing today?",
            "Hello! I'm here to help with your mental wellness journey. What would you like to focus on?"
        );
        responseTemplates.put("greeting", greetingResponses);
        
        // Farewell responses
        List<String> farewellResponses = Arrays.asList(
            "Take care! Remember to be kind to yourself.",
            "Goodbye for now. I'll be here when you need support.",
            "Until next time! Remember that self-care is important.",
            "Farewell! I hope our conversation was helpful."
        );
        responseTemplates.put("farewell", farewellResponses);
        
        // Thanks responses
        List<String> thanksResponses = Arrays.asList(
            "You're welcome! I'm glad I could help.",
            "It's my pleasure to support you on your wellness journey.",
            "I'm here for you anytime you need to talk.",
            "You're very welcome. Taking care of your mental health is important."
        );
        responseTemplates.put("thanks", thanksResponses);
        
        // Help responses
        List<String> helpResponses = Arrays.asList(
            "I can help with mood tracking, guided journaling, CBT thought reframing, meditation guidance, and more. What would you like to explore?",
            "MindEase offers several tools: mood tracking, journaling, thought reframing, meditation guides, goal setting, and crisis support. How can I assist you today?",
            "I'm here to support your mental wellness journey. You can track moods, journal, practice CBT techniques, meditate, set goals, or just chat. What interests you?",
            "I can guide you through various mental wellness activities including mood tracking, journaling, CBT exercises, meditation, and goal setting. Where would you like to start?"
        );
        responseTemplates.put("help", helpResponses);
        
        // General responses
        List<String> generalResponses = Arrays.asList(
            "I'm here to support you. Would you like to explore any specific wellness tools?",
            "How are you feeling today? We could track that in your mood journal.",
            "Is there something specific on your mind that you'd like to discuss or work through?",
            "Would you like to try journaling, meditation, or thought reframing today?"
        );
        responseTemplates.put("general", generalResponses);
    }
}