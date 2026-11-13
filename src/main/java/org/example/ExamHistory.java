package org.example;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ExamHistory {
    public void show(Stage stage) {
        Label heading = new Label("Examination history"); heading.getStyleClass().add("page-title");
        Label hint = new Label("Previously completed student attempts are listed below."); hint.getStyleClass().add("muted-text");
        TextArea area = new TextArea(); area.setEditable(false); area.setWrapText(true); area.setPrefRowCount(18); area.getStyleClass().add("history-area");
        Path file = Path.of("exam_history.txt");
        try { area.setText(Files.exists(file) && Files.size(file) > 0 ? Files.readString(file) : "No examination history is available yet."); }
        catch (IOException ex) { area.setText("Unable to read examination history: " + ex.getMessage()); }
        VBox card = new VBox(14, heading, hint, area); card.setPadding(new Insets(26)); card.getStyleClass().add("surface-card");
        VBox root = new VBox(card); root.setPadding(new Insets(24)); root.getStyleClass().add("page-background");
        Scene scene = new Scene(root, 760, 570); UIStyle.apply(scene); stage.setTitle("EXAMPRO | Exam History"); stage.setMinWidth(600); stage.setScene(scene); stage.show();
    }
}

