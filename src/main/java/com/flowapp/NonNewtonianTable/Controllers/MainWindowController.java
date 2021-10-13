package com.flowapp.NonNewtonianTable.Controllers;

import com.flowapp.NonNewtonianTable.Models.LineRow;
import com.flowapp.NonNewtonianTable.NonNewtonianTable;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.*;
import java.util.regex.Pattern;

public class MainWindowController implements Initializable {


    @FXML
    private TextField startQTextField;

    @FXML
    private TextField endQTextField;

    @FXML
    private TextField deltaQTextField;

    @FXML
    private TextField idTextField;

    @FXML
    private TextField lengthTextField;

    @FXML
    private TextField nDashTextField;

    @FXML
    private TextField kDashTextField;

    @FXML
    private TextField bnDashTextField;

    @FXML
    private TextField spGrTextField;

    @FXML
    private TextField anDashTextField;

    @FXML
    private TextField hsTextField;

    @FXML
    private Button calculateBtn;

    @FXML
    private TableView<LineRow> resultTableView;

    @FXML
    private TableColumn<LineRow, Float> qColumn;

    @FXML
    private TableColumn<LineRow, Float> vColumn;

    @FXML
    private TableColumn<LineRow, Float> nreColumn;

    @FXML
    private TableColumn<LineRow, Float> fColumn;

    @FXML
    private TableColumn<LineRow, Float> hfColumn;

    @FXML
    private TableColumn<LineRow, Float> htColumn;

    @FXML
    private ImageView facebookIcon;

    @FXML
    private ImageView linkedInIcon;

    @FXML
    private ImageView emailIcon;

    Stage getStage() {
        return (Stage) calculateBtn.getScene().getWindow();
    }

    private final Application application;

    public MainWindowController(Application application) {
        this.application = application;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final TextField[] textFields = {
                startQTextField,endQTextField,deltaQTextField,
                idTextField,lengthTextField,nDashTextField,kDashTextField,
                bnDashTextField,spGrTextField,anDashTextField,hsTextField,
        };
        for (var field: textFields) {
            field.setTextFormatter(createDecimalFormatter());
        }

        qColumn.setCellValueFactory(new PropertyValueFactory<LineRow, Float>("q"));
        vColumn.setCellValueFactory(new PropertyValueFactory<LineRow, Float>("v"));
        nreColumn.setCellValueFactory(new PropertyValueFactory<LineRow, Float>("nre"));
        fColumn.setCellValueFactory(new PropertyValueFactory<LineRow, Float>("f"));
        hfColumn.setCellValueFactory(new PropertyValueFactory<LineRow, Float>("hf"));
        htColumn.setCellValueFactory(new PropertyValueFactory<LineRow, Float>("ht"));

        calculateBtn.setOnAction(e -> {
            try {
                calculate();
            } catch (Exception ex) {
                ex.printStackTrace();
                final var errorDialog = createErrorDialog(getStage(), ex);
                errorDialog.show();
            }
        });

        setUpIcons();
    }

    private void setUpIcons() {
        var packagePath = getClass().getPackageName().split("\\.");
        packagePath[packagePath.length-1] = "Images";
        String fontPath = Arrays.stream(packagePath).reduce("", (s, s2) -> s + "/" + s2);
        final var facebookImage = getClass().getResource(fontPath + "/facebook.png");
        final var linkedInImage = getClass().getResource(fontPath + "/linkedin.png");
        final var emailImage = getClass().getResource(fontPath + "/email.png");
        facebookIcon.setImage(new Image(Objects.requireNonNull(facebookImage).toString()));
        linkedInIcon.setImage(new Image(Objects.requireNonNull(linkedInImage).toString()));
        emailIcon.setImage(new Image(Objects.requireNonNull(emailImage).toString()));
        facebookIcon.setPickOnBounds(true);
        linkedInIcon.setPickOnBounds(true);
        emailIcon.setPickOnBounds(true);
        facebookIcon.setOnMouseClicked(e -> {
            openBrowser("https://www.facebook.com/Moustafa.essam.hpp");
        });
        linkedInIcon.setOnMouseClicked(e -> {
            openBrowser("https://www.linkedin.com/in/moustafa-essam-726262174");
        });
        emailIcon.setOnMouseClicked(e -> {
            final var email = "mailto:essam.moustafa15@gmail.com";
            openBrowser(email);
            copyToClipboard(email);
        });
    }

    void openBrowser(String url) {
        application.getHostServices().showDocument(url);
    }

    private void copyToClipboard(String answer) {
        Clipboard.getSystemClipboard().setContent(Map.of(DataFormat.PLAIN_TEXT, answer));
    }

    private final Pattern numbersExpr = Pattern.compile("[-]?[\\d]*[.]?[\\d]*");
    TextFormatter<?> createDecimalFormatter() {
        final var pattern = numbersExpr.pattern();
        return new TextFormatter<>(c -> {
            if (c.getControlNewText().isEmpty()) { return c; }
            final var isGood = c.getControlNewText().matches(pattern);
            if (isGood) { return c; }
            else { return null; }
        });
    }

    void calculate() {
        final float startQM3H = getFloat(startQTextField.getText());
        final float endQM3H = getFloat(endQTextField.getText());
        final float deltaQM3H = getFloat(deltaQTextField.getText());
        final float spGr = getFloat(spGrTextField.getText());
        final float iDmm = getFloat(idTextField.getText());
        final float lengthM = getFloat(lengthTextField.getText());
        final float hs = getFloat(hsTextField.getText());
        final float nDash = getFloat(nDashTextField.getText());
        final float kDash = getFloat(kDashTextField.getText());
        final float anDash = getFloat(anDashTextField.getText());
        final float bnDash = getFloat(bnDashTextField.getText());

        final var task = new Service<List<LineRow>>() {
            @Override
            protected Task<List<LineRow>> createTask() {
                return new Task<>() {
                    @Override
                    protected List<LineRow> call() {
                        return new NonNewtonianTable().nonNewtonianTable(startQM3H, endQM3H, deltaQM3H, spGr, iDmm, lengthM, hs, nDash, kDash, anDash, bnDash);
                    }
                };
            }
        };
        final var loadingDialog = createProgressAlert(getStage(), task);
        task.setOnRunning(e -> {
            loadingDialog.show();
        });
        task.setOnSucceeded(e -> {
            final var result = task.getValue();
            setAnswer(result);
            loadingDialog.close();
        });
        task.setOnFailed(e -> {
            final var error = e.getSource().getException();
            final var errorDialog = createErrorDialog(getStage(), error);
            errorDialog.show();
            setAnswer(new ArrayList<>());
            loadingDialog.close();
        });
        task.setOnCancelled(e -> {
            loadingDialog.close();
        });
        task.restart();
    }

    Float getFloat(String value) {
        try {
            return Float.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }

    Integer getInteger(String value) {
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }

    void setAnswer(List<LineRow> answer) {
        resultTableView.setItems(FXCollections.observableList(answer));
    }

    Alert createErrorDialog(Stage owner, Throwable e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(owner);
        alert.setTitle("Error");
        alert.setContentText(e.getMessage());
        return alert;
    }

    Alert createProgressAlert(Stage owner, Service<?> task) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.initOwner(owner);
        alert.titleProperty().bind(task.titleProperty());
        alert.contentTextProperty().bind(task.messageProperty());

        ProgressIndicator pIndicator = new ProgressIndicator();
        pIndicator.progressProperty().bind(task.progressProperty());
        alert.setGraphic(pIndicator);
        alert.setHeaderText("Loading...");

        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alert.getDialogPane().lookupButton(ButtonType.OK)
                .disableProperty().bind(task.runningProperty());

        alert.getDialogPane().cursorProperty().bind(
                Bindings.when(task.runningProperty())
                        .then(Cursor.WAIT)
                        .otherwise(Cursor.DEFAULT)
        );
        return alert;
    }
}
