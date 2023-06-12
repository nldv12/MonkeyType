import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Controller {
    private Model model;
    private View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    public List<MenuItem> createLanguageItems() {
        List<MenuItem> languageItems = new ArrayList<>();
        File dictionaryDirectory = new File(model.getDictionaryDirectory());
        File[] files = dictionaryDirectory.listFiles();

        if (files != null) {
            for (File file : files) {
                MenuItem item = new MenuItem(file.getName());
                item.setOnAction(event -> {

                    String fileName = file.getName();
                    int lastDotIndex = fileName.lastIndexOf(".");
                    model.setSelectedLanguage(fileName.substring(0, lastDotIndex));// tu mam nazwę pliku bez rozszerzenia
                    view.getMenuBar().getMenus().get(0).setText(model.getSelectedLanguage());
                    loadDictionaryWords(file);
                    resetTest();
                });
                languageItems.add(item);
            }
        }

        return languageItems;
    }

    private void loadDictionaryWords(File file) {
        view.getDictionaryWords().clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split(" ");
                for (String word : words) {
                    view.getDictionaryWords().add(word);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<MenuItem> createDurationItems() {
        List<MenuItem> durationItems = new ArrayList<>();

        for (int duration : model.getDdurations()) {
            MenuItem item = new MenuItem(duration + " seconds");
            item.setOnAction(event -> {
                model.setSelectedDuration(duration);
                view.getTimerLabel().setText(duration + "");
                view.getMenuBar().getMenus().get(1).setText(model.getSelectedDuration() + " seconds");
                resetTest();
            });
            durationItems.add(item);
        }

        return durationItems;
    }

    public void resetTest() {
        view.getRoot().setTop(view.createMenu());
        view.getRoot().setCenter(view.getMainText());
        view.getRoot().setBottom(view.createFooter());
        if (!model.getSelectedLanguage().equals("Language") && model.getSelectedDuration() != 0) {
            startTest();
        }
    }

    private void startTest() {

        if (model.getCountdownTimerThread() != null && model.getCountdownTimerThread().isAlive()) {
            model.getCountdownTimerThread().interrupt();
        }

        CountdownTimer countdownTimer = new CountdownTimer(model, view, this, model.getSelectedDuration());

        model.setCountdownTimerThread(new Thread(countdownTimer));
        model.setAccuracy(0);
        model.setAverageWPM(0);
        model.setNumberOfCharsInSec(0);
        model.setNumberOfCharsInWord(0);

        model.setCurrentIndex(0);
        model.setCorrectCount(0);
        model.setMistakeCount(0);
        model.setExtraCount(0);
        model.setSkippedCount(0);

        model.setGameOver(false);
        model.setPaused(false);
        model.setSomethingTyped(false);
        model.setCurrWordLettersCounted(false);

        model.getKeysList().clear();
        model.getWpm_InCurrentSecond().clear();
        model.getAverage_WPM_InCurrentSecond().clear();
        model.getWordsWPM().clear();
        model.getTypedWords().clear();
        model.getWpmPerWordForFile().clear();


        generateParagraph();

        runWaveAnimationThread();

        model.getCountdownTimerThread().setDaemon(true);
        model.getCountdownTimerThread().start();
        model.setPrevTime(System.currentTimeMillis());
    }

    private void runWaveAnimationThread() {
        if (model.getAnimationThread() != null && model.getAnimationThread().isAlive()) {
            // do nothing just continue Wave animation
        } else {
            WaveAnimationTask animationTask = new WaveAnimationTask(model, model.getKeysList());
            model.setAnimationThread(new Thread(animationTask));
            model.getAnimationThread().setDaemon(true);
            model.getAnimationThread().start();
        }
    }


    public void generateParagraph() {
        view.getMainText().getChildren().clear();
        model.getKeysList().clear();

        model.setCurrentIndex(0);

        view.getMainText().getChildren().add(0, view.cursor);
        model.getKeysList().add(0, view.cursor);


        Random random = new Random();
        StringBuilder paragraph = new StringBuilder();

        for (int i = 0; i < model.getWORDS_PER_PARAGRAPH(); i++) {
            int randomIndex = random.nextInt(view.getDictionaryWords().size());
            paragraph.append(view.getDictionaryWords().get(randomIndex)).append(" ");
        }
        for (int i = 0; i < paragraph.length(); i++) {
            char item = paragraph.charAt(i);
            Text textItem = new Text(String.valueOf(item));
            textItem.setFont(Font.font("Consolas", 20));
            textItem.setFill(Color.valueOf("#7A7A7AFF"));
            model.getKeysList().add(textItem);
            view.getMainText().getChildren().add(textItem);
        }

    }

    public void countWPM_forEveryWord() {
        model.setNow(System.currentTimeMillis());
        long deltaTimeMillis = model.getNow() - model.getPrevTime();
        double deltaTimeSeconds = (double) deltaTimeMillis / 1000;
        int wpm = (int) (60 / deltaTimeSeconds);
        model.getWordsWPM().add(wpm);
        model.setPrevTime(System.currentTimeMillis());
    }


    public void lowTimeAnimation(Label label) {
        Duration duration = Duration.millis(500);
        Color startColor = Color.ORANGE;
        Color endColor = Color.RED;
        KeyFrame startFrame = new KeyFrame(Duration.ZERO, new KeyValue(label.textFillProperty(), startColor));
        KeyFrame endFrame = new KeyFrame(duration, new KeyValue(label.textFillProperty(), endColor));
        Timeline timeline = new Timeline(startFrame, endFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.play();
    }

    public void shortcutAnimation(Label label) {
        Duration duration = Duration.millis(200);
        Color startColor = Color.ORANGE;
        Color endColor = Color.GREEN;
        KeyFrame startFrame = new KeyFrame(Duration.ZERO, new KeyValue(label.textFillProperty(), startColor));
        KeyFrame endFrame = new KeyFrame(duration, new KeyValue(label.textFillProperty(), endColor));
        Timeline timeline = new Timeline(startFrame, endFrame);
        timeline.setCycleCount(4);
        timeline.setAutoReverse(true);
        timeline.play();
    }


    public void countCurrentWPMforCurrentSecond(int countSeconds) {

        if (model.getNumberOfCharsInWord() > 0) {
            double wpm_InCurrentSec = ((double) model.getNumberOfCharsInSec() / model.getNumberOfCharsInWord()) * 60;
            model.getWpm_InCurrentSecond().put(countSeconds, (int) wpm_InCurrentSec);
        }else {
            model.getWpm_InCurrentSecond().put(countSeconds, 0);
        }
    }

    public void countAverageWPMforCurrentSecond(int countSeconds) {
        int averageWPM_InCurrentSec = 0;

        int sum = 0;
        for (int value : model.getWpm_InCurrentSecond().values()) {
            sum += value;
        }
        if (model.getWpm_InCurrentSecond().size() != 0)
            averageWPM_InCurrentSec = sum / model.getWpm_InCurrentSecond().size();
        model.getAverage_WPM_InCurrentSecond().put(countSeconds, averageWPM_InCurrentSec);
    }

    public void gameOver() {
        if (model.isSomethingTyped()) {
            getTypedWords();
            calcAverageWPM();
            calcAccuracy();
            fillwpmPerWordList();
            generateStatsFile();
            Platform.runLater(() -> {
                view.nextPage();
            });
            model.setSomethingTyped(false);
        } else {
            Platform.runLater(() -> {
                view.nextPage();
            });
        }
    }


    private void generateStatsFile() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH.mm");
        String formattedDate = dateFormat.format(currentDate);


        String fileName = formattedDate + ".txt";
        File file = new File(fileName);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String line : model.getWpmPerWordForFile()) {
                writer.write(line);
                writer.newLine();
            }
            writer.close();

            System.out.println("Generated File: " + fileName);
        } catch (IOException e) {
            System.err.println("Something went wrong while generating stats file.");
            e.printStackTrace();
        }
    }

    private void fillwpmPerWordList() {
        int size = model.getTypedWords().size();

        for (int i = 0; i < size; i++) {
            String word = model.getTypedWords().get(i);
            int wpm = model.getWordsWPM().get(i);
            model.getWpmPerWordForFile().add(word + " -> " + wpm + "wpm");
        }
    }

    private void getTypedWords() {
        int index = model.getCurrentIndex();
        List<Text> keysList = model.getKeysList();
        String word = "";
        for (int i = 0; i < index; i++) {
            String character = keysList.get(i).getText();
            if (character.equals(" ")) {
                model.getTypedWords().add(word);
                word = "";
            } else {
                word = word + character;
            }
        }
    }

    private void calcAverageWPM() {
        int averageWPM;

        int sum = 0;
        for (int value : model.getWpm_InCurrentSecond().values()) {
            sum += value;
        }
        averageWPM = sum / model.getWpm_InCurrentSecond().size();
        model.setAverageWPM(averageWPM);
    }

    private void calcAccuracy() {

        int index = model.getKeysList().indexOf(view.cursor);
        int accuracy = 0;

        if (index > 0)
            accuracy = model.getCorrectCount() * 100 / index;
        model.setAccuracy(accuracy);


    }

    public void updateTimerLabel(int seconds) {
        Platform.runLater(() -> {
            view.getTimerLabel().setText(String.valueOf(seconds));
            if (seconds < 12)
                lowTimeAnimation(view.getTimerLabel());
            else
                view.getTimerLabel().setTextFill(Color.ORANGE);
        });
    }

    public void updateTimerLabelColor() {
        Platform.runLater(() -> {
            view.getTimerLabel().setTextFill(Color.BLUE);
        });
    }


    public void countLettersForWord() {
        int nrOfCharsInWord = 0;
        int startIndex = model.getCurrentIndex();
        while (!model.getKeysList().get(startIndex).getText().equals(" ")){
            nrOfCharsInWord++;
            startIndex++;
        }
        model.setNumberOfCharsInWord(nrOfCharsInWord);
        model.setCurrWordLettersCounted(true);
    }
}