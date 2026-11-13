package org.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StudentLogin {
    public void show() {
        Stage stage = new Stage();
        Label title = new Label("Student sign in"); title.getStyleClass().add("section-title");
        Label hint = new Label("Enter your student account details to begin the examination."); hint.getStyleClass().add("muted-text"); hint.setWrapText(true);
        TextField username = new TextField(); username.setPromptText("Username");
        PasswordField password = new PasswordField(); password.setPromptText("Password");
        Label feedback = new Label(); feedback.getStyleClass().add("error-text");
        Button login = new Button("Continue to exam"); login.getStyleClass().addAll("primary-button", "wide-button");
        login.setOnAction(e -> {
            if ("student".equals(username.getText().trim()) && "student123".equals(password.getText())) {
                var questions = new Admin().getQuestions();
                if (questions.isEmpty()) { feedback.setText("No exam questions are available yet."); return; }
                Stage examStage = new Stage();
                if (new StudentExam().startExam(examStage, questions)) stage.close();
            } else feedback.setText("Incorrect username or password.");
        });
        VBox card = new VBox(16, title, hint, username, password, login, feedback); card.setPadding(new Insets(30)); card.setAlignment(Pos.CENTER_LEFT); card.getStyleClass().add("surface-card"); card.setMaxWidth(420);
        VBox root = new VBox(card); root.setAlignment(Pos.CENTER); root.setPadding(new Insets(28)); root.getStyleClass().add("page-background");
        Scene scene = new Scene(root, 520, 460); UIStyle.apply(scene); stage.setTitle("EXAMPRO | Student Login"); stage.setScene(scene); stage.show();
    }
}
