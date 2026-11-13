package org.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main {
    private final Admin admin = new Admin();
    private final TextField questionField = new TextField();
    private final TextField optionAField = new TextField();
    private final TextField optionBField = new TextField();
    private final TextField optionCField = new TextField();
    private final TextField optionDField = new TextField();
    private final ComboBox<String> correctAnswerBox = new ComboBox<>();
    private final ListView<String> questionList = new ListView<>();
    private final Label totalQuestionsLabel = new Label("0");

    public void show(Stage stage) {
        Label logo = new Label("EXAMPRO");
        logo.getStyleClass().add("brand-title");
        Label tagline = new Label("SMART ONLINE EXAMINATION & EVALUATION");
        tagline.getStyleClass().add("brand-subtitle");
        VBox brand = new VBox(4, logo, tagline);
        Label adminBadge = new Label("ADMIN WORKSPACE");
        adminBadge.getStyleClass().add("header-badge");
        BorderPane header = new BorderPane();
        header.setLeft(brand);
        header.setRight(adminBadge);
        BorderPane.setAlignment(brand, Pos.CENTER_LEFT);
        BorderPane.setAlignment(adminBadge, Pos.CENTER_RIGHT);
        header.getStyleClass().add("app-header");
        header.setPadding(new Insets(22, 30, 22, 30));

        Label welcome = new Label("Welcome back, Administrator");
        welcome.getStyleClass().add("page-title");
        Label intro = new Label("Manage your question bank and organize examinations.");
        intro.getStyleClass().add("muted-text");
        VBox welcomeBox = new VBox(7, welcome, intro);

        Label countCaption = new Label("QUESTION BANK");
        countCaption.getStyleClass().add("stat-caption");
        totalQuestionsLabel.getStyleClass().add("stat-number");
        VBox statCard = new VBox(6, countCaption, totalQuestionsLabel);
        statCard.getStyleClass().add("stat-card");
        statCard.setMinWidth(175);
        HBox welcomeRow = new HBox(20, welcomeBox, statCard);
        welcomeRow.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(welcomeBox, Priority.ALWAYS);

        Label formTitle = new Label("Question Management");
        formTitle.getStyleClass().add("section-title");
        Label formHint = new Label("Add a new MCQ or select an existing question to edit it.");
        formHint.getStyleClass().add("muted-text");
        questionField.setPromptText("Type the question here");
        optionAField.setPromptText("Option A");
        optionBField.setPromptText("Option B");
        optionCField.setPromptText("Option C");
        optionDField.setPromptText("Option D");
        correctAnswerBox.getItems().setAll("A", "B", "C", "D");
        correctAnswerBox.setValue("A");
        correctAnswerBox.setMaxWidth(Double.MAX_VALUE);
        GridPane form = new GridPane();
        form.setHgap(14); form.setVgap(13);
        ColumnConstraints labels = new ColumnConstraints(112);
        ColumnConstraints inputs = new ColumnConstraints(); inputs.setHgrow(Priority.ALWAYS);
        form.getColumnConstraints().addAll(labels, inputs);
        addRow(form, "Question", questionField, 0);
        addRow(form, "Option A", optionAField, 1);
        addRow(form, "Option B", optionBField, 2);
        addRow(form, "Option C", optionCField, 3);
        addRow(form, "Option D", optionDField, 4);
        addRow(form, "Correct option", correctAnswerBox, 5);

        Button add = new Button("＋  Add Question"); add.getStyleClass().add("primary-button");
        Button update = new Button("Update"); update.getStyleClass().add("secondary-button");
        Button delete = new Button("Delete"); delete.getStyleClass().add("danger-button");
        Button clear = new Button("Clear"); clear.getStyleClass().add("light-button");
        HBox actions = new HBox(10, add, update, delete, clear); actions.setAlignment(Pos.CENTER_LEFT);
        add.setOnAction(e -> addQuestion());
        update.setOnAction(e -> updateQuestion());
        delete.setOnAction(e -> deleteQuestion());
        clear.setOnAction(e -> clearFields());

        VBox formCard = new VBox(15, formTitle, formHint, form, actions);
        formCard.getStyleClass().add("surface-card");
        formCard.setPadding(new Insets(24));
        HBox.setHgrow(formCard, Priority.ALWAYS);

        Label listTitle = new Label("Question Bank"); listTitle.getStyleClass().add("section-title");
        Label listHint = new Label("Select an item to edit or remove it."); listHint.getStyleClass().add("muted-text");
        questionList.setPlaceholder(new Label("No questions yet. Add your first question."));
        questionList.setPrefHeight(430);
        questionList.getSelectionModel().selectedIndexProperty().addListener((obs, oldV, newV) -> {
            int index = newV.intValue();
            var questions = admin.getQuestions();
            if (index >= 0 && index < questions.size()) {
                Question q = questions.get(index);
                questionField.setText(q.getQuestion());
                optionAField.setText(q.getOptionA()); optionBField.setText(q.getOptionB());
                optionCField.setText(q.getOptionC()); optionDField.setText(q.getOptionD());
                correctAnswerBox.setValue(q.getCorrectAnswer());
            }
        });
        VBox listCard = new VBox(13, listTitle, listHint, questionList);
        listCard.getStyleClass().add("surface-card"); listCard.setPadding(new Insets(24));
        listCard.setMinWidth(285); listCard.setPrefWidth(350);

        HBox content = new HBox(20, formCard, listCard); content.setAlignment(Pos.TOP_LEFT);
        Button startExam = new Button("Start Student Exam"); startExam.getStyleClass().add("primary-button");
        Button history = new Button("View Exam History"); history.getStyleClass().add("secondary-button");
        startExam.setOnAction(e -> {
            if (admin.getQuestionCount() == 0) { message(Alert.AlertType.WARNING, "Add questions before starting an exam."); return; }
            new StudentExam().startExam(new Stage(), admin.getQuestions());
        });
        history.setOnAction(e -> new ExamHistory().show(new Stage()));
        HBox bottom = new HBox(12, startExam, history); bottom.setAlignment(Pos.CENTER_LEFT);

        VBox page = new VBox(24, welcomeRow, content, bottom);
        page.setPadding(new Insets(28));
        ScrollPane scroll = new ScrollPane(page); scroll.setFitToWidth(true); scroll.setPannable(true);
        BorderPane root = new BorderPane();root.getStyleClass().add("page-background");
        root.setTop(header); root.setCenter(scroll);
        Scene scene = new Scene(root, 1200, 790); UIStyle.apply(scene);
        stage.setTitle("EXAMPRO | Admin Dashboard"); stage.setMinWidth(950); stage.setMinHeight(650);
        stage.setScene(scene); refreshQuestionList(); stage.show();
    }

    private void addRow(GridPane grid, String text, Node field, int row) {
        Label label = new Label(text); label.getStyleClass().add("field-label");
        grid.add(label, 0, row); grid.add(field, 1, row);
        if (field instanceof Region region) region.setMaxWidth(Double.MAX_VALUE);
    }

    private Question createQuestion() {
        return new Question(questionField.getText().trim(), optionAField.getText().trim(),
                optionBField.getText().trim(), optionCField.getText().trim(),
                optionDField.getText().trim(), correctAnswerBox.getValue());
    }

    private boolean validateFields() {
        if (questionField.getText().isBlank() || optionAField.getText().isBlank() ||
                optionBField.getText().isBlank() || optionCField.getText().isBlank() ||
                optionDField.getText().isBlank() || correctAnswerBox.getValue() == null) {
            message(Alert.AlertType.WARNING, "Please complete the question and all four options.");
            return false;
        }
        return true;
    }

    private void addQuestion() {
        if (!validateFields()) return;
        admin.addQuestion(createQuestion()); refreshQuestionList(); clearFields();
        message(Alert.AlertType.INFORMATION, "Question added successfully.");
    }

    private void updateQuestion() {
        int index = questionList.getSelectionModel().getSelectedIndex();
        if (index < 0) { message(Alert.AlertType.WARNING, "Select a question from the list before updating."); return; }
        if (!validateFields()) return;
        admin.updateQuestion(index, createQuestion()); refreshQuestionList();
        questionList.getSelectionModel().select(index);
        message(Alert.AlertType.INFORMATION, "Question updated successfully.");
    }

    private void deleteQuestion() {
        int index = questionList.getSelectionModel().getSelectedIndex();
        if (index < 0) { message(Alert.AlertType.WARNING, "Select a question to delete."); return; }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete the selected question?", ButtonType.CANCEL, ButtonType.OK);
        confirm.setTitle("Confirm deletion"); confirm.setHeaderText("Delete question");
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;
        admin.deleteQuestion(index); refreshQuestionList(); clearFields();
    }

    private void refreshQuestionList() {
        questionList.getItems().clear();
        var questions = admin.getQuestions();
        for (int i = 0; i < questions.size(); i++) questionList.getItems().add(String.format("%02d   %s", i + 1, questions.get(i).getQuestion()));
        totalQuestionsLabel.setText(String.valueOf(questions.size()));
    }

    private void clearFields() {
        questionField.clear(); optionAField.clear(); optionBField.clear(); optionCField.clear(); optionDField.clear();
        correctAnswerBox.setValue("A"); questionList.getSelectionModel().clearSelection();
    }

    private void message(Alert.AlertType type, String text) {
        Alert alert = new Alert(type); alert.setTitle("EXAMPRO"); alert.setHeaderText(null); alert.setContentText(text); alert.showAndWait();
    }
}
