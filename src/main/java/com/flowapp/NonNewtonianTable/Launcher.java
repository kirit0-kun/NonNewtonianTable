package com.flowapp.NonNewtonianTable;

import com.flowapp.NonNewtonianTable.Controllers.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        showMainWindow(primaryStage);
    }

    private void showMainWindow(Stage primaryStage) throws java.io.IOException {
        MainWindowController mainWindowController = new MainWindowController(this);
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("Views/MainWindow.fxml")));
        fxmlLoader.setController(mainWindowController);
        Parent root = fxmlLoader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}