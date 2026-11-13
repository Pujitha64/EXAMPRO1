package org.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentExam {
    private List<Question> questions;
    private ArrayList<String> answers;
    private int currentQuestion;
    private int timeRemaining = 600;
    private final Label questionLabel = new Label();
    private final Label timerLabel = new Label();
    private final Label progressLabel = new Label();
    private final RadioButton optionA = new RadioButton();
    private final RadioButton optionB = new RadioButton();
    private final RadioButton optionC = new RadioButton();
    private final RadioButton optionD = new RadioButton();
    private final ToggleGroup answerGroup = new ToggleGroup();
    private Timeline timer;
    private Stage examStage;
    private String studentName;
    private boolean submitted;

    public boolean startExam(Stage stage, List<Question> questionList) {
        if (questionList == null || questionList.isEmpty()) return false;
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Student details"); dialog.setHeaderText("Before you begin"); dialog.setContentText("Enter your name:");
        Optional<String> name = dialog.showAndWait();
        if (name.isEmpty() || name.get().trim().isEmpty()) return false;
        studentName = name.get().trim(); questions = new ArrayList<>(questionList); examStage = stage;
        answers = new ArrayList<>(); for (int i = 0; i < questions.size(); i++) answers.add(null);
        optionA.setToggleGroup(answerGroup); optionB.setToggleGroup(answerGroup); optionC.setToggleGroup(answerGroup); optionD.setToggleGroup(answerGroup);
        questionLabel.setWrapText(true); questionLabel.getStyleClass().add("exam-question");
        VBox options = new VBox(14, optionA, optionB, optionC, optionD); options.getStyleClass().add("answer-options");
        timerLabel.getStyleClass().add("timer-pill"); progressLabel.getStyleClass().add("muted-text");
        Button previous = new Button("Previous"); previous.getStyleClass().add("light-button");
        Button next = new Button("Next question"); next.getStyleClass().add("primary-button");
        Button submit = new Button("Submit exam"); submit.getStyleClass().add("danger-button");
        previous.setOnAction(e -> previousQuestion()); next.setOnAction(e -> nextQuestion());
        submit.setOnAction(e -> confirmSubmit());
        HBox controls = new HBox(12, previous, next, submit); controls.setAlignment(Pos.CENTER_RIGHT);
        HBox top = new HBox(progressLabel, timerLabel); top.setAlignment(Pos.CENTER_LEFT); HBox.setHgrow(progressLabel, Priority.ALWAYS);
        VBox card = new VBox(24, top, questionLabel, new Separator(), options, controls); card.setPadding(new Insets(30)); card.getStyleClass().add("surface-card");
        VBox root = new VBox(card); root.setPadding(new Insets(30)); root.getStyleClass().add("page-background");
        Scene scene = new Scene(root, 850, 600); UIStyle.apply(scene);
        stage.setTitle("EXAMPRO | Student Examination"); stage.setMinWidth(650); stage.setMinHeight(500); stage.setScene(scene); stage.show();
        displayQuestion(); startTimer(); return true;
    }

    private void displayQuestion() {
        Question q = questions.get(currentQuestion);
        progressLabel.setText("QUESTION  " + (currentQuestion + 1) + "  /  " + questions.size());
        questionLabel.setText(q.getQuestion());
        optionA.setText("A     " + q.getOptionA()); optionB.setText("B     " + q.getOptionB());
        optionC.setText("C     " + q.getOptionC()); optionD.setText("D     " + q.getOptionD());
        answerGroup.selectToggle(null);
        String saved = answers.get(currentQuestion);
        if (saved != null) switch (saved) { case "A" -> optionA.setSelected(true); case "B" -> optionB.setSelected(true); case "C" -> optionC.setSelected(true); case "D" -> optionD.setSelected(true); default -> { } }
    }

    private void saveAnswer() {
        Toggle selected = answerGroup.getSelectedToggle();
        if (selected == optionA) answers.set(currentQuestion, "A");
        else if (selected == optionB) answers.set(currentQuestion, "B");
        else if (selected == optionC) answers.set(currentQuestion, "C");
        else if (selected == optionD) answers.set(currentQuestion, "D");
    }

    private void nextQuestion() { saveAnswer(); if (currentQuestion < questions.size() - 1) { currentQuestion++; displayQuestion(); } }
    private void previousQuestion() { saveAnswer(); if (currentQuestion > 0) { currentQuestion--; displayQuestion(); } }

    private void startTimer() {
        updateTimerLabel();
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeRemaining--; updateTimerLabel();
            if (timeRemaining <= 0) submitExam();
        }));
        timer.setCycleCount(Timeline.INDEFINITE); timer.play();
    }
    private void updateTimerLabel() {
        timerLabel.setText(String.format("TIME LEFT   %02d:%02d", timeRemaining / 60, timeRemaining % 60));
        if (timeRemaining <= 60 && !timerLabel.getStyleClass().contains("timer-warning")) timerLabel.getStyleClass().add("timer-warning");
    }
    private void confirmSubmit() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to submit your exam?", ButtonType.CANCEL, ButtonType.OK);
        confirm.setTitle("Submit examination"); confirm.setHeaderText("Finish your attempt?");
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) submitExam();
    }

    private void submitExam() {
        if (submitted) return; submitted = true;
        if (timer != null) timer.stop(); saveAnswer();
        int correct = 0;
        for (int i = 0; i < questions.size(); i++) if (answers.get(i) != null && answers.get(i).equalsIgnoreCase(questions.get(i).getCorrectAnswer())) correct++;
        ExamResult result = new ExamResult(questions.size(), correct);
        FileHandler.saveExamHistory(studentName, questions.size(), correct, result.getPercentage(), result.getStatus());
        Label heading = new Label("Your examination result"); heading.getStyleClass().add("page-title");
        Label greeting = new Label("Well done, " + studentName + ". Your attempt has been recorded."); greeting.getStyleClass().add("muted-text");
        Label score = new Label(correct + " / " + questions.size()); score.getStyleClass().add("result-score");
        Label marks = new Label("Marks earned: " + result.getMarks());
        Label percent = new Label(String.format("Percentage: %.2f%%", result.getPercentage()));
        Label status = new Label(result.getStatus()); status.getStyleClass().add(result.getStatus().equals("PASS") ? "result-pass" : "result-fail");
        Label wrong = new Label("Incorrect or unanswered: " + (questions.size() - correct));
        Button close = new Button("Close result"); close.getStyleClass().add("primary-button"); close.setOnAction(e -> examStage.close());
        VBox card = new VBox(16, heading, greeting, new Separator(), new Label("CORRECT ANSWERS"), score, marks, wrong, percent, status, close);
        card.setAlignment(Pos.CENTER); card.setPadding(new Insets(32)); card.getStyleClass().add("surface-card");
        VBox root = new VBox(card); root.setAlignment(Pos.CENTER); root.setPadding(new Insets(30)); root.getStyleClass().add("page-background");
        Scene scene = new Scene(root, 540, 570); UIStyle.apply(scene); examStage.setTitle("EXAMPRO | Result"); examStage.setScene(scene);
    }
}
