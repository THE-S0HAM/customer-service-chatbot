package com.mindease.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mindease.model.JournalEntry;
import com.mindease.model.MoodEntry;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for exporting data to PDF and CSV formats
 */
public class ExportService {
    private static final Logger logger = LoggerFactory.getLogger(ExportService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    /**
     * Exports mood entries to PDF
     * @param entries List of mood entries
     * @param filePath Path to save the PDF
     * @return true if export was successful
     */
    public boolean exportMoodsToPdf(List<MoodEntry> entries, String filePath) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            
            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Mood Tracking Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);
            
            // Create table
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            
            // Add header row
            addTableHeader(table, "Date", "Mood", "Intensity", "Notes");
            
            // Add data rows
            for (MoodEntry entry : entries) {
                addTableRow(table, 
                    entry.getCreatedAt().format(DATE_FORMATTER),
                    entry.getMood().toString(),
                    String.valueOf(entry.getIntensityLevel()),
                    entry.getNotes() != null ? entry.getNotes() : ""
                );
            }
            
            document.add(table);
            document.close();
            
            logger.info("Exported mood entries to PDF: {}", filePath);
            return true;
        } catch (Exception e) {
            logger.error("Error exporting mood entries to PDF", e);
            return false;
        }
    }
    
    /**
     * Exports journal entries to PDF
     * @param entries List of journal entries
     * @param filePath Path to save the PDF
     * @return true if export was successful
     */
    public boolean exportJournalToPdf(List<JournalEntry> entries, String filePath) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            
            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Journal Entries", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            
            // Add entries
            Font entryTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font dateFont = FontFactory.getFont(FontFactory.HELVETICA_ITALIC, 10);
            Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            
            for (JournalEntry entry : entries) {
                document.add(Chunk.NEWLINE);
                
                // Entry title
                Paragraph entryTitle = new Paragraph(entry.getTitle(), entryTitleFont);
                document.add(entryTitle);
                
                // Entry date
                Paragraph date = new Paragraph("Date: " + entry.getCreatedAt().format(DATE_FORMATTER), dateFont);
                document.add(date);
                
                // Prompt used
                if (entry.getPromptUsed() != null && !entry.getPromptUsed().isEmpty()) {
                    Paragraph prompt = new Paragraph("Prompt: " + entry.getPromptUsed(), dateFont);
                    document.add(prompt);
                }
                
                document.add(Chunk.NEWLINE);
                
                // Entry content
                Paragraph content = new Paragraph(entry.getContent(), contentFont);
                document.add(content);
                
                // Sentiment
                if (entry.getSentimentScore() != null && !entry.getSentimentScore().isEmpty()) {
                    document.add(Chunk.NEWLINE);
                    Paragraph sentiment = new Paragraph("Sentiment: " + entry.getSentimentScore(), dateFont);
                    document.add(sentiment);
                }
                
                document.add(new LineSeparator());
            }
            
            document.close();
            
            logger.info("Exported journal entries to PDF: {}", filePath);
            return true;
        } catch (Exception e) {
            logger.error("Error exporting journal entries to PDF", e);
            return false;
        }
    }
    
    /**
     * Exports mood entries to CSV
     * @param entries List of mood entries
     * @param filePath Path to save the CSV
     * @return true if export was successful
     */
    public boolean exportMoodsToCsv(List<MoodEntry> entries, String filePath) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            // Write header
            String[] header = {"Date", "Mood", "Intensity", "Notes"};
            writer.writeNext(header);
            
            // Write data rows
            for (MoodEntry entry : entries) {
                String[] row = {
                    entry.getCreatedAt().format(DATE_FORMATTER),
                    entry.getMood().toString(),
                    String.valueOf(entry.getIntensityLevel()),
                    entry.getNotes() != null ? entry.getNotes() : ""
                };
                writer.writeNext(row);
            }
            
            logger.info("Exported mood entries to CSV: {}", filePath);
            return true;
        } catch (IOException e) {
            logger.error("Error exporting mood entries to CSV", e);
            return false;
        }
    }
    
    /**
     * Exports journal entries to CSV
     * @param entries List of journal entries
     * @param filePath Path to save the CSV
     * @return true if export was successful
     */
    public boolean exportJournalToCsv(List<JournalEntry> entries, String filePath) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            // Write header
            String[] header = {"Date", "Title", "Prompt", "Content", "Sentiment"};
            writer.writeNext(header);
            
            // Write data rows
            for (JournalEntry entry : entries) {
                String[] row = {
                    entry.getCreatedAt().format(DATE_FORMATTER),
                    entry.getTitle(),
                    entry.getPromptUsed() != null ? entry.getPromptUsed() : "",
                    entry.getContent(),
                    entry.getSentimentScore() != null ? entry.getSentimentScore() : ""
                };
                writer.writeNext(row);
            }
            
            logger.info("Exported journal entries to CSV: {}", filePath);
            return true;
        } catch (IOException e) {
            logger.error("Error exporting journal entries to CSV", e);
            return false;
        }
    }
    
    /**
     * Adds a header row to a PDF table
     */
    private void addTableHeader(PdfPTable table, String... headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell();
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setBorderWidth(1);
            cell.setPhrase(new Phrase(header));
            table.addCell(cell);
        }
    }
    
    /**
     * Adds a data row to a PDF table
     */
    private void addTableRow(PdfPTable table, String... values) {
        for (String value : values) {
            PdfPCell cell = new PdfPCell();
            cell.setBorderWidth(1);
            cell.setPhrase(new Phrase(value));
            table.addCell(cell);
        }
    }
}