package org.example;

public class ExamResult {
    private final int totalQuestions;
    private final int correctAnswers;
    public ExamResult(int totalQuestions, int correctAnswers) {
        if (totalQuestions < 0 || correctAnswers < 0 || correctAnswers > totalQuestions) throw new IllegalArgumentException("Invalid exam result values");
        this.totalQuestions = totalQuestions; this.correctAnswers = correctAnswers;
    }
    public int getMarks() { return correctAnswers; }
    public double getPercentage() { return totalQuestions == 0 ? 0 : correctAnswers * 100.0 / totalQuestions; }
    public String getStatus() { return getPercentage() >= 40.0 ? "PASS" : "FAIL"; }
}
