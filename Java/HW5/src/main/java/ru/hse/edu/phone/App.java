package ru.hse.edu.phone;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
  private static final String TITLE = "Телефонная книга";

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    VBox root = loadLayout(loader);
    primaryStage.setScene(new Scene(root));

    primaryStage.setMinHeight(root.getPrefHeight());
    primaryStage.setMinWidth(root.getPrefWidth());

    primaryStage.setTitle(TITLE);
    primaryStage.show();
  }

  private VBox loadLayout(FXMLLoader loader) {
    try {
      loader.setLocation(getClass().getResource(Settings.APP_FXML));
      return loader.load();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
