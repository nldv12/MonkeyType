import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.List;

public class WaveAnimationTask implements Runnable {
    private List<Text> keysList;

    Timeline timeline;
    Model model;
    int currentIndex;

    public WaveAnimationTask(Model model, List<Text> keysList) {
        this.model = model;
        this.keysList = keysList;
    }

    @Override
    public void run() {
        while ( true ) {

            timeline = new Timeline(new KeyFrame(Duration.millis(50), event -> runNextAnimation()));
            timeline.setCycleCount(keysList.size());
            timeline.play();

            try {
                Thread.sleep(15000);
                currentIndex = 0;
            } catch (InterruptedException e) {
                return;
            }
        }
    }



    private void runNextAnimation() {
        if (currentIndex < keysList.size()) {
            Text currentText = keysList.get(currentIndex);
            runWaveAnimation(currentText);
            currentIndex++;
        }
    }


    private void runWaveAnimation(Text text) {
        KeyValue raiseKeyValue = new KeyValue(text.translateYProperty(), -5);
        KeyFrame raiseKeyFrame = new KeyFrame(Duration.millis(300), raiseKeyValue);

        KeyValue lowerKeyValue = new KeyValue(text.translateYProperty(), 0);
        KeyFrame lowerKeyFrame = new KeyFrame(Duration.millis(300), lowerKeyValue);

        timeline = new Timeline();
        timeline.getKeyFrames().addAll(raiseKeyFrame, lowerKeyFrame);
        timeline.setCycleCount(2);
        timeline.setAutoReverse(true);

        timeline.setDelay(Duration.millis(100));
        timeline.play();
    }
}
