import javafx.application.Platform;
import javafx.scene.paint.Color;

public class CountdownTimer implements Runnable {

    private volatile boolean isInterrupted = false;
    private int seconds;
    private boolean isPaused = false;
    private final Model model;
    private final View view;
    private final Controller controller;
    private int countSeconds = 1;
    private int prevSecWordsCount = 0;


    public CountdownTimer(Model model, View view, Controller controller, int seconds) {
        this.view = view;
        this.model = model;
        this.controller = controller;
        this.seconds = seconds;
    }

    @Override
    public void run() {

        while (seconds > 0 && !Thread.interrupted() && !isInterrupted) {
            isPaused = model.isPaused();
            if (!isPaused) {
                controller.updateTimerLabel(seconds);
                seconds--;
            } else {
                controller.updateTimerLabelColor();
            }
            try {
                Thread.sleep(1000);
                controller.countCurrentWPMforCurrentSecond(prevSecWordsCount, countSeconds);
                prevSecWordsCount = model.getSpacesAtAll();
                controller.countAverageWPMforCurrentSecond(countSeconds);
                countSeconds++;
            } catch (InterruptedException e) {
                break;
            }
        }

        if (seconds <= 0 && !model.isGameOver())
            controller.gameOver();

    }

    public void pause() {
        isPaused = !isPaused;
    }

    public void stop() {
        isInterrupted = true;
    }

}
