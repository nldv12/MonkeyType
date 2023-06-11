import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Model {


    private Thread countdownTimerThread;



    private Thread animationThread;

    private static final String DICTIONARY_DIRECTORY = "dictionary";
    private static final int[] durations = {15, 20, 45, 60, 90, 120, 300};
    private static final int WORDS_PER_PARAGRAPH = 30;

    private String selectedLanguage = "Language";
    private int selectedDuration;


    private long prevTime;
    private long now;


    private int averageWPM;

    private int accuracy;
    private List<Text> keysList = new ArrayList();
    private Map<Integer, Integer> wpm_InCurrentSecond = new LinkedHashMap<>();
    private Map<Integer, Integer> average_WPM_InCurrentSecond = new LinkedHashMap<>();

    private List<Integer> wordsWPM = new ArrayList();
    private List<String> typedWords = new ArrayList();

    private List<String> wpmPerWordForFile = new ArrayList();

    private boolean isPaused;



    private boolean somethingTyped;

    private boolean isGameOver;


    private int currentIndex = 0;


    private int spacesAtAll = 0;

    private int correctCount;
    private int mistakeCount;
    private int extraCount;
    private int skippedCount;


    // GETTERS ================================================
    public int[] getDdurations() {
        return durations;
    }

    public int getWORDS_PER_PARAGRAPH() {
        return WORDS_PER_PARAGRAPH;
    }

    public List<Text> getKeysList() {
        return keysList;
    }

    public List<String> getTypedWords() {
        return typedWords;
    }

    public String getDictionaryDirectory() {
        return DICTIONARY_DIRECTORY;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public int getMistakeCount() {
        return mistakeCount;
    }

    public int getExtraCount() {
        return extraCount;
    }

    public int getSkippedCount() {
        return skippedCount;
    }

    public int getSpacesAtAll() {
        return spacesAtAll;
    }

    public Map<Integer, Integer> getWpm_InCurrentSecond() {
        return wpm_InCurrentSecond;
    }

    public Map<Integer, Integer> getAverage_WPM_InCurrentSecond() {
        return average_WPM_InCurrentSecond;
    }

    public int getAverageWPM() {
        return averageWPM;
    }

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    public int getSelectedDuration() {
        return selectedDuration;
    }

    public Thread getCountdownTimerThread() {
        return countdownTimerThread;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public List<Integer> getWordsWPM() {
        return wordsWPM;
    }

    public long getPrevTime() {
        return prevTime;
    }

    public long getNow() {
        return now;
    }

    public List<String> getWpmPerWordForFile() {
        return wpmPerWordForFile;
    }
    public boolean isGameOver() {
        return isGameOver;
    }
    public boolean isSomethingTyped() {
        return somethingTyped;
    }

    public Thread getAnimationThread() {
        return animationThread;
    }



    // SETTERS =================================================================================================================================

    public void setAnimationThread(Thread animationThread) {
        this.animationThread = animationThread;
    }
    public void setSomethingTyped(boolean somethingTyped) {
        this.somethingTyped = somethingTyped;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }
    public void setPrevTime(long prevTime) {
        this.prevTime = prevTime;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public void setCountdownTimerThread(Thread countdownTimerThread) {
        this.countdownTimerThread = countdownTimerThread;
    }

    public void setSelectedDuration(int selectedDuration) {
        this.selectedDuration = selectedDuration;
    }

    public void setSelectedLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    public void setAverageWPM(int averageWPM) {
        this.averageWPM = averageWPM;
    }


    public void setSpacesAtAll(int spacesAtAll) {
        this.spacesAtAll = spacesAtAll;
    }

    public void incSpacesAtAll() {
        this.spacesAtAll++;
    }


    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

    public void setMistakeCount(int mistakeCount) {
        this.mistakeCount = mistakeCount;
    }

    public void setExtraCount(int extraCount) {
        this.extraCount = extraCount;
    }

    public void setSkippedCount(int skippedCount) {
        this.skippedCount = skippedCount;
    }


    public void incCorrectCount() {
        this.correctCount++;
    }

    public void incMistakeCount() {
        this.mistakeCount++;
    }

    public void incExtraCount() {
        this.extraCount++;
    }

    public void incSkippedCount() {
        this.skippedCount++;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public void incCurrentIndex() {
        this.currentIndex++;
    }


}
