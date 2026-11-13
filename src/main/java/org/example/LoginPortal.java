package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginPortal extends Application {
    @Override public void start(Stage stage) {
        Label logo = new Label("EXAMPRO"); logo.getStyleClass().add("hero-title");
        Label subtitle = new Label("SMART ONLINE EXAMINATION & EVALUATION SYSTEM"); subtitle.getStyleClass().add("hero-subtitle");
        VBox hero = new VBox(12, logo, subtitle); hero.setAlignment(Pos.CENTER); hero.getStyleClass().add("login-hero");
        Label heading = new Label("Welcome to your examination portal"); heading.getStyleClass().add("section-title");
        Label hint = new Label("Choose how you would like to continue."); hint.getStyleClass().add("muted-text");
        Button admin = new Button("Administrator Login"); admin.getStyleClass().addAll("primary-button", "wide-button");
        Button student = new Button("Student Login"); student.getStyleClass().addAll("secondary-button", "wide-button");
        admin.setOnAction(e -> new AdminLogin().show()); student.setOnAction(e -> new StudentLogin().show());
        VBox card = new VBox(18, heading, hint, admin, student); card.setAlignment(Pos.CENTER); card.setPadding(new Insets(34)); card.getStyleClass().add("surface-card"); card.setMaxWidth(440);
        VBox root = new VBox(28, hero, card); root.setAlignment(Pos.CENTER); root.setPadding(new Insets(28)); root.getStyleClass().add("page-background");
        Scene scene = new Scene(root, 680, 620); UIStyle.apply(scene);
        stage.setTitle("EXAMPRO | Login Portal"); stage.setMinWidth(560); stage.setMinHeight(520); stage.setScene(scene); stage.show();
    }
    public static void main(String[] args) { launch(args); }
}
