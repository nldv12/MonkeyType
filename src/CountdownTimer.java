
public class CountdownTimer implements Runnable {

    private volatile boolean isInterrupted = false;
    private int seconds;
    private boolean isPaused = false;
    private final Model model;
    private final View view;
    private final Controller controller;
    private int countSecondsPassed = 1;



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
                if (!isPaused) {
                    controller.countCurrentWPMforCurrentSecond(countSecondsPassed);
                    controller.countAverageWPMforCurrentSecond(countSecondsPassed);
                    countSecondsPassed++;
                    model.setNumberOfCharsInSec(0);
                }
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
