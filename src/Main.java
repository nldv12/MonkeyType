
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main extends Application {

    private static final String DICTIONARY_DIRECTORY = "dictionary";
    private static final int WORDS_PER_PARAGRAPH = 30;
    private static final int[] durations = {15, 20, 45, 60, 90, 120, 300};

    private final List<String> dictionaryWords = new ArrayList<>();
    private String selectedLanguage;
    private int selectedDuration;
    private MenuBar menuBar;
    private TextArea textArea;

    private int currentIndex;
    private int correctCount;
    private int mistakeCount;
    private int extraCount;
    private int skippedCount;
    private boolean isPaused;
    private boolean isTimerRunning;

    //    --module-path "C:\javafx-sdk-20.0.1\lib" --add-modules javafx.controls,javafx.fxml

    private Text keyLabel;
    private TextFlow resLabel;
    String character;

    private boolean tabPressed = false;
    private boolean altPressed = false;
    private boolean ctrlPressed = false;
    private boolean shiftPressed = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        selectedDuration = 15; // Default duration
        resLabel = new TextFlow();
        resLabel.setLineSpacing(5);
        keyLabel = new Text();
        keyLabel.setWrappingWidth(400);

        keyLabel.setFont(Font.font("Arial", 20));
        keyLabel.setFill(Color.LIGHTGRAY);

        resLabel.getChildren().add(keyLabel);




        HBox labelsBox = new HBox(0);
        labelsBox.setAlignment(Pos.CENTER);
        labelsBox.getChildren().addAll(resLabel, keyLabel);

        VBox textArea = new VBox(0);
        textArea.setAlignment(Pos.CENTER);
        textArea.getChildren().add(labelsBox);

        BorderPane root = new BorderPane();
        root.setTop(createMenu());
        root.setCenter(textArea);
//        root.setCenter(createTextArea());
        root.setBottom(createFooter());

        Scene scene = new Scene(root, 800, 300);

        scene.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            // tab Clicked
            if (keyCode == KeyCode.TAB) {
                tabPressed = true;
            } else if (tabPressed && keyCode == KeyCode.ENTER) {

                resetTest();
                System.out.println("test restarted");
                tabPressed = false;
            } else {
                tabPressed = false;
            }

            // Shift Clicked
            if (event.isShiftDown())
                character = keyCode.toString();
            else
                character = keyCode.toString().toLowerCase();


            boolean isJustLetter = !tabPressed && !ctrlPressed && !altPressed && !shiftPressed && keyCode.isLetterKey();


            if (event.isAltDown()) {
                // alt Clicked
                altPressed = true;
                checkIfAltDown(event, keyCode);
                altPressed = false;
            } else if (event.isControlDown() && event.isShiftDown() && keyCode == KeyCode.P) {
                // Ctrl + Shift + P
                ctrlPressed = true;
                shiftPressed = true;
                handlePause();
                ctrlPressed = false;
                shiftPressed = false;
            } else if (isJustLetter && !isPaused) {
                // letter clicked
                handleLetterTyped(character);
            } else if (character.equals("space")){
                handleSpaceTyped(character);
            }

        });


        primaryStage.setScene(scene);
        primaryStage.setTitle("Monkeytype");
        primaryStage.show();
    }

    private void handlePause() {
        if (isPaused)
            isPaused = false;
        else {
            isPaused = true;
            isTimerRunning = false;
        }
    }


    private void checkIfAltDown(KeyEvent event, KeyCode keyCode) {
        // polish signs

        if (event.isAltDown() && keyCode == KeyCode.A) {
            character = "ą";
            handleLetterTyped(character);
        } else if (event.isAltDown() && keyCode == KeyCode.C) {
            character = "ć";
            handleLetterTyped(character);
        } else if (event.isAltDown() && keyCode == KeyCode.E) {
            character = "ę";
            handleLetterTyped(character);
        } else if (event.isAltDown() && keyCode == KeyCode.L) {
            character = "ł";
            handleLetterTyped(character);
        } else if (event.isAltDown() && keyCode == KeyCode.N) {
            character = "ń";
            handleLetterTyped(character);
        } else if (event.isAltDown() && keyCode == KeyCode.O) {
            character = "ó";
            handleLetterTyped(character);
        } else if (event.isAltDown() && keyCode == KeyCode.S) {
            character = "ś";
            handleLetterTyped(character);
        } else if (event.isAltDown() && keyCode == KeyCode.X) {
            character = "ź";
            handleLetterTyped(character);
        } else if (event.isAltDown() && keyCode == KeyCode.Z) {
            character = "ż";
            handleLetterTyped(character);
        }
    }


    //HEADER ========================================================================

    private MenuBar createMenu() {
        menuBar = new MenuBar();

        Menu languageMenu = new Menu("language");
        Menu durationMenu = new Menu("duration");

        List<MenuItem> languageItems = createLanguageItems();
        List<MenuItem> durationItems = createDurationItems();

        languageMenu.getItems().addAll(languageItems);
        durationMenu.getItems().addAll(durationItems);

        menuBar.getMenus().addAll(languageMenu, durationMenu);
        return menuBar;
    }

    private List<MenuItem> createLanguageItems() {
        List<MenuItem> languageItems = new ArrayList<>();
        File dictionaryDirectory = new File(DICTIONARY_DIRECTORY);
        File[] files = dictionaryDirectory.listFiles();

        if (files != null) {
            for (File file : files) {
                MenuItem item = new MenuItem(file.getName());
                item.setOnAction(event -> {

                    String fileName = file.getName();
                    int lastDotIndex = fileName.lastIndexOf(".");
                    selectedLanguage = fileName.substring(0, lastDotIndex);// tu mam nazwę pliku bez rozszerzenia
                    menuBar.getMenus().get(0).setText(selectedLanguage);
                    loadDictionaryWords(file);
//                    resetTest();
                });
                languageItems.add(item);
            }
        }

        return languageItems;
    }

    private List<MenuItem> createDurationItems() {
        List<MenuItem> durationItems = new ArrayList<>();

        for (int duration : durations) {
            MenuItem item = new MenuItem(duration + " seconds");
            item.setOnAction(event -> {
                selectedDuration = duration;
                menuBar.getMenus().get(1).setText(selectedDuration + " seconds");
                resetTest();
            });
            durationItems.add(item);
        }

        return durationItems;
    }

    private void loadDictionaryWords(File file) {
        dictionaryWords.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split(" ");
                for (String word : words) {
                    dictionaryWords.add(word);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<File> loadDictionaryFiles() {
        List<File> files = new ArrayList<>();

        try {
            Files.walk(Paths.get(DICTIONARY_DIRECTORY))
                    .filter(Files::isRegularFile)
                    .forEach(path -> files.add(path.toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }


    //BODY ========================================================================

    private void handleSpaceTyped(String character) {
        if (character.equals("space"))
            character = " ";

        if (keyLabel.getText().charAt(0) != ' ' && character.equals(" ")) {
            while (keyLabel.getText().charAt(0) != ' ') {
                Text errorText = new Text(keyLabel.getText().charAt(0) + "");
                errorText.setFont(Font.font("Arial", 20));
                errorText.setFill(Color.BLACK);
                resLabel.getChildren().add(errorText);
                String prevText = keyLabel.getText();
                String newText = prevText.substring(0, 0) + prevText.substring(1);
                keyLabel.setText(newText);
            }
            handleLetterTyped(" ");
        }else {
            Text successText = new Text(character);
            resLabel.getChildren().add(successText);
            String prevText = keyLabel.getText();
            String newText = prevText.substring(0, 0) + prevText.substring(1);
            keyLabel.setText(newText);
        }


    }

    private void handleLetterTyped(String character) {


        if (character.charAt(0) == keyLabel.getText().charAt(0)) {// if correct letter
            Text successText = new Text(character);
            successText.setFont(Font.font("Arial", 20));
            successText.setFill(Color.GREEN);
            resLabel.getChildren().add(successText);

            String prevText = keyLabel.getText();
            String newText = prevText.substring(0, 0) + prevText.substring(1);
            keyLabel.setText(newText);
        } else if (keyLabel.getText().charAt(0) == ' ') {// needed space, recived letter
            Text errorText = new Text(character);
            errorText.setFont(Font.font("Arial", 20));
            errorText.setFill(Color.ORANGE);
            resLabel.getChildren().add(errorText);
        } else {// if wrong letter
            Text errorText = new Text(character);
            errorText.setFont(Font.font("Arial", 20));
            errorText.setFill(Color.RED);
            resLabel.getChildren().add(errorText);

            String prevText = keyLabel.getText();
            String newText = prevText.substring(0, 0) + prevText.substring(1);
            keyLabel.setText(newText);
        }
    }

    private TextArea createTextArea() {
        textArea = new TextArea();
        textArea.setEditable(true);
        textArea.setWrapText(true);
        textArea.setStyle("-fx-font-size: 20px;");


        textArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                event.consume();
            }
        });

        return textArea;
    }


    // FOOTER ========================================================================
    private HBox createFooter() {
        HBox footer = new HBox();
        footer.setPadding(new Insets(10));
        footer.setSpacing(10);

        Label restartLabel = new Label("Restart (Tab + Enter)");
        Label pauseLabel = new Label("Pause (Ctrl + Shift + P)");
        Label endLabel = new Label("End (Esc)");
        footer.getChildren().addAll(restartLabel, pauseLabel, endLabel);
        return footer;
    }

    private void startTest() {
        currentIndex = 0;
        correctCount = 0;
        mistakeCount = 0;
        extraCount = 0;
        skippedCount = 0;
        isPaused = false;
//        textArea.requestFocus();
        generateParagraph();

    }


    private void generateParagraph() {
//        if (!dictionaryWords.isEmpty() && !paragrafGenerated) {
        Random random = new Random();
        StringBuilder paragraph = new StringBuilder();

        for (int i = 0; i < WORDS_PER_PARAGRAPH; i++) {
            int randomIndex = random.nextInt(dictionaryWords.size());
            paragraph.append(dictionaryWords.get(randomIndex)).append(" ");
        }

        keyLabel.setText(paragraph.toString().trim());
        resLabel.getChildren().clear();

//            keyLabel = new Text(paragraph.toString().trim());

//            textArea.setText(paragraph.toString().trim());
//        }
    }

    private void resetTest() {
        if (!selectedLanguage.isEmpty()) {
            startTest();
        }
    }


    private void endTest() {
//        isPaused = true;
//        textArea.clear();
//        currentIndex = 0;
    }
}
