package com.mindease.service;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for analyzing sentiment in text using OpenNLP
 */
public class SentimentAnalysisService {
    private static final Logger logger = LoggerFactory.getLogger(SentimentAnalysisService.class);
    
    private DocumentCategorizerME categorizer;
    private TokenizerME tokenizer;
    
    /**
     * Initializes the sentiment analysis service with pre-trained models
     */
    public SentimentAnalysisService() {
        try {
            // Load tokenizer model
            InputStream tokenizerModelIn = getClass().getResourceAsStream("/models/en-token.bin");
            TokenizerModel tokenizerModel = new TokenizerModel(tokenizerModelIn);
            tokenizer = new TokenizerME(tokenizerModel);
            
            // Load sentiment model
            InputStream sentimentModelIn = getClass().getResourceAsStream("/models/en-sentiment.bin");
            DoccatModel sentimentModel = new DoccatModel(sentimentModelIn);
            categorizer = new DocumentCategorizerME(sentimentModel);
            
            logger.info("Sentiment analysis service initialized successfully");
        } catch (IOException e) {
            logger.error("Failed to initialize sentiment analysis service", e);
        }
    }
    
    /**
     * Analyzes the sentiment of the provided text
     * @param text Text to analyze
     * @return Sentiment result (POSITIVE, NEGATIVE, NEUTRAL) or null if analysis fails
     */
    public SentimentResult analyzeSentiment(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Tokenize the text
            String[] tokens = tokenizer.tokenize(text);
            
            // Get the probabilities of different categories
            double[] probabilities = categorizer.categorize(tokens);
            
            // Get the category with highest probability
            String category = categorizer.getBestCategory(probabilities);
            
            // Create sentiment result
            SentimentResult result = new SentimentResult();
            result.setSentiment(category);
            result.setConfidence(probabilities[categorizer.getIndex(category)]);
            
            // Add additional scores
            if (categorizer.getNumberOfCategories() >= 3) {
                result.setPositiveScore(probabilities[categorizer.getIndex("positive")]);
                result.setNegativeScore(probabilities[categorizer.getIndex("negative")]);
                result.setNeutralScore(probabilities[categorizer.getIndex("neutral")]);
            }
            
            logger.debug("Sentiment analysis result: {}", result);
            return result;
        } catch (Exception e) {
            logger.error("Error analyzing sentiment", e);
            return null;
        }
    }
    
    /**
     * Class representing the result of sentiment analysis
     */
    public static class SentimentResult {
        private String sentiment;
        private double confidence;
        private double positiveScore;
        private double negativeScore;
        private double neutralScore;
        
        public String getSentiment() {
            return sentiment;
        }
        
        public void setSentiment(String sentiment) {
            this.sentiment = sentiment;
        }
        
        public double getConfidence() {
            return confidence;
        }
        
        public void setConfidence(double confidence) {
            this.confidence = confidence;
        }
        
        public double getPositiveScore() {
            return positiveScore;
        }
        
        public void setPositiveScore(double positiveScore) {
            this.positiveScore = positiveScore;
        }
        
        public double getNegativeScore() {
            return negativeScore;
        }
        
        public void setNegativeScore(double negativeScore) {
            this.negativeScore = negativeScore;
        }
        
        public double getNeutralScore() {
            return neutralScore;
        }
        
        public void setNeutralScore(double neutralScore) {
            this.neutralScore = neutralScore;
        }
        
        @Override
        public String toString() {
            return "SentimentResult{" +
                    "sentiment='" + sentiment + '\'' +
                    ", confidence=" + confidence +
                    ", positiveScore=" + positiveScore +
                    ", negativeScore=" + negativeScore +
                    ", neutralScore=" + neutralScore +
                    '}';
        }
    }
}