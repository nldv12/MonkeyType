import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;
//--module-path "C:\javafx-sdk-20.0.1\lib" --add-modules javafx.controls,javafx.fxml

public class View extends Application {

    Model model = new Model();
    Controller controller = new Controller(model, this);


    private BorderPane root;
    private TextFlow mainText;

    private final List<String> dictionaryWords = new ArrayList<>();
    private MenuBar menuBar;
    private HBox footer;
    private Label timerLabel;


    private Label restartLabel;
    private Label pauseLabel;
    private Label endLabel;


    private VBox statsVBox;


    private HBox bigSatsAndChart;
    private VBox wpmAndAcc;
    private HBox chart;
    private HBox smallStats;
    private VBox testType;
    private VBox characters;
    public Text cursor = new Text("|");



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        model.setPaused(true);
        cursor.setStyle("-fx-font-size: 25; -fx-fill: white");


        mainText = new TextFlow();
        mainText.setTextAlignment(TextAlignment.CENTER);
        mainText.setPadding(new Insets(130,30,0,30));
        mainText.setLineSpacing(5);

        root = new BorderPane();


        root.setStyle("-fx-background-color: #323438;");
        root.setTop(createMenu());
        root.setCenter(mainText);
        root.setBottom(createFooter());
        runCursorAnimation();



        Scene scene = new Scene(root, 850, 450);
        scene.setOnKeyPressed(new KeyPressed(model, this, controller));
        scene.setOnKeyTyped(new KeyTyped(model, this, controller));
        scene.getStylesheets().add("styles.css");

        primaryStage.setScene(scene);
        primaryStage.setTitle("Monkeytype");
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            primaryStage.close();
        });
    }


    public MenuBar createMenu() {
        menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: #323438");

        Menu languageMenu = new Menu(model.getSelectedLanguage());
        languageMenu.getStyleClass().add("menu");
        Menu durationMenu = new Menu(model.getSelectedDuration() == 0?"Duration": String.valueOf(model.getSelectedDuration()));
        durationMenu.getStyleClass().add("menu");

        List<MenuItem> languageItems = controller.createLanguageItems();
        List<MenuItem> durationItems = controller.createDurationItems();

        languageMenu.getItems().addAll(languageItems);
        durationMenu.getItems().addAll(durationItems);

        menuBar.getMenus().addAll(languageMenu, durationMenu);
        return menuBar;
    }



    public HBox createFooter() {
        footer = new HBox();
        footer.setPadding(new Insets(10));
        footer.setSpacing(10);

        timerLabel = new Label();
        timerLabel.setFont(Font.font("Arial", 17));
        timerLabel.setTextFill(Color.ORANGE);


        restartLabel = new Label("Restart (Tab + Enter)");
        restartLabel.setFont(Font.font("Arial", 13));
        restartLabel.setTextFill(Color.ORANGE);

        pauseLabel = new Label("Pause (Ctrl + Shift + P)");
        pauseLabel.setFont(Font.font("Arial", 13));
        pauseLabel.setTextFill(Color.ORANGE);

        endLabel = new Label("End (Esc)");
        endLabel.setFont(Font.font("Arial", 13));
        endLabel.setTextFill(Color.ORANGE);

        footer.getChildren().addAll(timerLabel, restartLabel, pauseLabel, endLabel);
        return footer;
    }


    // GETTERS
    public MenuBar getMenuBar() {
        return menuBar;
    }

    public List<String> getDictionaryWords() {
        return dictionaryWords;
    }

    public Label getTimerLabel() {
        return timerLabel;
    }

    public TextFlow getMainText() {
        return mainText;
    }

    public Label getRestartLabel() {
        return restartLabel;
    }

    public Label getPauseLabel() {
        return pauseLabel;
    }

    public Label getEndLabel() {
        return endLabel;
    }

    public BorderPane getRoot() {
        return root;
    }


    public void nextPage() {
        statsVBox = new VBox(0);
        statsVBox.setStyle("-fx-background-color: #323438");
        root.getChildren().clear();

        VBox emptyBox = new VBox();
        emptyBox.setMinWidth(15);
        emptyBox.setStyle("-fx-background-color: #323438");

        root.setLeft(emptyBox);

        root.setCenter(statsVBox);

        bigSatsAndChart = new HBox();
        wpmAndAcc = new VBox(createWPMandACC());
        chart = new HBox(generateChart());
        bigSatsAndChart.getChildren().addAll(wpmAndAcc, chart);

        smallStats = new HBox(30);
        testType = new VBox(createTestType());
        characters = new VBox(createCharacters());
        smallStats.getChildren().addAll(testType, characters);
        VBox footer = new VBox();
        footer.setMinHeight(25);
        footer.getChildren().add(restartLabel);


        statsVBox.getChildren().addAll(bigSatsAndChart, smallStats, footer);
    }

    private VBox createWPMandACC() {
        VBox wpmANDacc = new VBox();

        // wpm
        Label wpmLabel = new Label("wpm");
        wpmLabel.setFont(Font.font("Consolas", 35));
        wpmLabel.setStyle("-fx-text-fill: #636569;");

        Label wpmNumber = new Label(model.getAverageWPM() + "");
        wpmNumber.setFont(Font.font("Consolas", 55));
        wpmNumber.setStyle("-fx-text-fill: #deb737;");
        // acc
        Label accLabel = new Label("acc");
        accLabel.setFont(Font.font("Consolas", 35));
        accLabel.setStyle("-fx-text-fill: #636569;");

        Label accNumber = new Label(model.getAccuracy() + "%");
        accNumber.setFont(Font.font("Consolas", 55));
        accNumber.setStyle("-fx-text-fill: #deb737;");
        wpmANDacc.getChildren().addAll(wpmLabel, wpmNumber, accLabel, accNumber);

        return wpmANDacc;
    }

    private HBox generateChart() {
        HBox chart = new HBox();

        NumberAxis xAxis = new NumberAxis(1,model.getWpm_InCurrentSecond().size(),3);
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time in seconds");
        yAxis.setLabel("Words per Minute");

        XYChart.Series<Number, Number> currentWPM = new XYChart.Series<>();

        model.getWpm_InCurrentSecond().forEach((sec, wpm) -> {
            currentWPM.getData().add(new XYChart.Data<>(sec, wpm));
        });

        XYChart.Series<Number, Number> averageWPM = new XYChart.Series<>();
        model.getAverage_WPM_InCurrentSecond().forEach((sec, wpm) -> {
            averageWPM.getData().add(new XYChart.Data<>(sec, wpm));
        });
        AreaChart<Number, Number> areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setMinWidth(700);
        areaChart.setMinHeight(300);


        areaChart.getData().addAll(currentWPM, averageWPM);

        areaChart.setLegendVisible(false);


        chart.getChildren().add(areaChart);

        return chart;

    }

    private VBox createTestType() {
        VBox testType = new VBox(3);

        Label testTypeLabel = new Label("test type");
        testTypeLabel.setFont(Font.font("Consolas", 19));
        testTypeLabel.setStyle("-fx-text-fill: #636569;");

        Label timeLabel = new Label("time " + model.getSelectedDuration());
        timeLabel.setFont(Font.font("Consolas", 19));
        timeLabel.setStyle("-fx-text-fill: #deb737;");

        Label languageLabel = new Label(model.getSelectedLanguage());
        languageLabel.setFont(Font.font("Consolas", 19));
        languageLabel.setStyle("-fx-text-fill: #deb737;");

        Label emptyLabel = new Label();

        testType.getChildren().addAll(testTypeLabel, timeLabel, languageLabel, emptyLabel);

        return testType;
    }

    private VBox createCharacters() {
        VBox characters = new VBox(3);

        Label charactersLabel = new Label("characters");
        charactersLabel.setFont(Font.font("Consolas", 19));
        charactersLabel.setStyle("-fx-text-fill: #636569;");

        Label charStatsLabel = new Label(model.getCorrectCount() + "/" + model.getMistakeCount() + "/" + model.getExtraCount() + "/" + model.getSkippedCount());
        charStatsLabel.setFont(Font.font("Consolas", 40));
        charStatsLabel.setStyle("-fx-text-fill: #deb737;");


        characters.getChildren().addAll(charactersLabel, charStatsLabel);

        return characters;
    }

    private void runCursorAnimation (){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(cursor.opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(cursor.opacityProperty(), 0.0))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.play();
    }




}

