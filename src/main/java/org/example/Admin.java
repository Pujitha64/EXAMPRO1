package org.example;

import java.util.ArrayList;
import java.util.List;

public class Admin {
    private final ArrayList<Question> questions;

    public Admin() {
        questions = FileHandler.loadQuestions();
    }

    public void addQuestion(Question question) {
        if (question == null) throw new IllegalArgumentException("Question cannot be null");
        questions.add(question);
        FileHandler.saveQuestions(questions);
    }

    public void updateQuestion(int index, Question question) {
        if (index >= 0 && index < questions.size() && question != null) {
            questions.set(index, question);
            FileHandler.saveQuestions(questions);
        }
    }

    public void deleteQuestion(int index) {
        if (index >= 0 && index < questions.size()) {
            questions.remove(index);
            FileHandler.saveQuestions(questions);
        }
    }

    public ArrayList<Question> getQuestions() {
        return new ArrayList<>(questions);
    }

    public int getQuestionCount() { return questions.size(); }
}

