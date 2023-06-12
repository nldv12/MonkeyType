import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class KeyPressed implements EventHandler<KeyEvent> {
    private boolean tabPressed = false;
    private View view;
    private Model model;
    private Controller controller;

    public KeyPressed(Model model, View view, Controller controller) {
        this.view = view;
        this.model = model;
        this.controller = controller;
    }


    @Override
    public void handle(KeyEvent event) {
        KeyCode keyCode = event.getCode();

        // TAB+ENTER
        if (keyCode == KeyCode.TAB) {
            tabPressed = true;
        } else if (tabPressed && keyCode == KeyCode.ENTER) {

                controller.resetTest();
                controller.shortcutAnimation(view.getRestartLabel());

            tabPressed = false;
        } else {
            tabPressed = false;
        }

        // Ctrl + Shift + P
        if (event.isControlDown() && event.isShiftDown() && keyCode == KeyCode.P) {
            model.setPaused(!model.isPaused());
            controller.shortcutAnimation(view.getPauseLabel());
        }
        // Esc
        if (keyCode == KeyCode.ESCAPE && !model.isGameOver() ){
            controller.shortcutAnimation(view.getEndLabel());
            model.setGameOver(true);
            controller.gameOver();
        }
    }
}
