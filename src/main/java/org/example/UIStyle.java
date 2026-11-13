package org.example;

import javafx.scene.Scene;
import javafx.scene.Node;

import java.net.URL;

public final class UIStyle {

    private UIStyle() {
    }

    public static void apply(Scene scene) {

        URL css = UIStyle.class.getResource("/style.css");

        if (css == null) {
            System.out.println("ERROR: style.css file not found!");
            return;
        }

        scene.getStylesheets().clear();
        scene.getStylesheets().add(css.toExternalForm());

        System.out.println("CSS successfully loaded: " + css);
    }

    public static void style(Node node, String... classes) {
        node.getStyleClass().addAll(classes);
    }
}