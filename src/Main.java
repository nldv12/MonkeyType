
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
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

    //    --module-path "C:\javafx-sdk-20.0.1\lib" --add-modules javafx.controls,javafx.fxml


    private static final String KEY = "dziś jest poniedziałek";
    private Text keyLabel;
    private TextFlow resLabel;
    String character;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        
        // todo: implement new scheme to this package now not working good
        selectedDuration = 15; // Default duration

        resLabel = new TextFlow();
        resLabel.setLineSpacing(5);
        keyLabel = new Text(KEY);
        keyLabel.setFont(Font.font("Arial", 20));
        keyLabel.setFill(Color.LIGHTGRAY);

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
//        root.setBottom(createFooter());

        Scene scene = new Scene(root, 800, 300);

        scene.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            character = keyCode.toString().toLowerCase();
            if (event.isAltDown() && keyCode == KeyCode.S) {
                character = "ś";
                handleKeyTyped(character);
            } else if (event.isAltDown() && keyCode == KeyCode.L) {
                character = "ł";
                handleKeyTyped(character);
            } else if (!character.equals("control") && !character.equals("alt_graph")) {
                handleKeyTyped(character);
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Monkeytype");
        primaryStage.show();
    }


    //MENU ========================================================================

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


    //TEXT_AREA ========================================================================
    private void handleKeyTyped(String character) {
        if (character.equals("space"))
            character = " ";

        if (character.charAt(0) == keyLabel.getText().charAt(0)) {
            Text successText = new Text(character);
            successText.setFont(Font.font("Arial", 20));
            successText.setFill(Color.GREEN);
            resLabel.getChildren().add(successText);

            String prevText = keyLabel.getText();
            String newText = prevText.substring(0, 0) + prevText.substring(1);
            keyLabel.setText(newText);

        }else if (keyLabel.getText().charAt(0) != ' ' && character.equals(" ")){
            while (keyLabel.getText().charAt(0) != ' '){
                Text errorText = new Text(keyLabel.getText().charAt(0)+ "");
                errorText.setFont(Font.font("Arial", 20));
                errorText.setFill(Color.BLACK);
                resLabel.getChildren().add(errorText);
                String prevText = keyLabel.getText();
                String newText = prevText.substring(0, 0) + prevText.substring(1);
                keyLabel.setText(newText);
            }
            handleKeyTyped(" ");
        }else if (keyLabel.getText().charAt(0) == ' '){
            Text errorText = new Text(character);
            errorText.setFont(Font.font("Arial", 20));
            errorText.setFill(Color.ORANGE);
            resLabel.getChildren().add(errorText);
        }
        else {
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


    private void startTest() {
        currentIndex = 0;
        correctCount = 0;
        mistakeCount = 0;
        extraCount = 0;
        skippedCount = 0;
        isPaused = false;
        textArea.requestFocus();
        generateParagraph();

    }

    private void generateParagraph() {
        if (!dictionaryWords.isEmpty()) {
            Random random = new Random();
            StringBuilder paragraph = new StringBuilder();

            for (int i = 0; i < WORDS_PER_PARAGRAPH; i++) {
                int randomIndex = random.nextInt(dictionaryWords.size());
                paragraph.append(dictionaryWords.get(randomIndex)).append(" ");
            }

            keyLabel.setText(paragraph.toString().trim());
//            textArea.setText(paragraph.toString().trim());
        }
    }


    private void resetTest() {
        if (!selectedLanguage.isEmpty()) {
            startTest();
        }
    }

    private void restartTest() {
        resetTest();
    }

    private void togglePause() {
        isPaused = !isPaused;
    }

    private void endTest() {
        isPaused = true;
        textArea.clear();
        currentIndex = 0;
    }
}
