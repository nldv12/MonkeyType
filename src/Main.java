
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        selectedDuration = 15; // Default duration

        BorderPane root = new BorderPane();
        root.setTop(createMenu());
        root.setCenter(createTextArea());
//        root.setBottom(createFooter());

        Scene scene = new Scene(root, 800, 300);
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

            textArea.setText(paragraph.toString().trim());
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
