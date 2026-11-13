package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final Path QUESTION_FILE = Path.of("questions.dat");
    private static final Path HISTORY_FILE = Path.of("exam_history.txt");

    public static void saveQuestions(List<Question> questions) {
        try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(QUESTION_FILE))) {
            output.writeObject(new ArrayList<>(questions));
        } catch (IOException e) { throw new UncheckedIOException("Unable to save questions", e); }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Question> loadQuestions() {
        if (!Files.exists(QUESTION_FILE)) return new ArrayList<>();
        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(QUESTION_FILE))) {
            Object data = input.readObject();
            if (data instanceof ArrayList<?> list) return (ArrayList<Question>) list;
            throw new IOException("Unexpected question file format");
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            System.err.println("Could not load questions: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveExamHistory(String studentName, int totalQuestions, int correctAnswers, double percentage, String status) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        String entry = "Student Name: " + studentName + System.lineSeparator() +
                "Date: " + date + System.lineSeparator() +
                "Total Questions: " + totalQuestions + System.lineSeparator() +
                "Correct Answers: " + correctAnswers + System.lineSeparator() +
                "Marks: " + correctAnswers + System.lineSeparator() +
                String.format(java.util.Locale.US, "Percentage: %.2f%%", percentage) + System.lineSeparator() +
                "Status: " + status + System.lineSeparator() +
                "----------------------------------------" + System.lineSeparator();
        try { Files.writeString(HISTORY_FILE, entry, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND); }
        catch (IOException e) { throw new UncheckedIOException("Unable to save exam history", e); }
    }
}
