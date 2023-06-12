import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.List;
import java.util.Objects;


public class KeyTyped implements EventHandler<KeyEvent> {

    private View view;
    private Model model;
    private Controller controller;

    public KeyTyped(Model model, View view, Controller controller) {
        this.view = view;
        this.model = model;
        this.controller = controller;
    }


    @Override
    public void handle(KeyEvent event) {

        String character = event.getCharacter();
        if (character.equals("\t"))
            character = "";
        else if (character.equals("\r"))
            character = "";
        else if (character.equals("\u0010"))
            character = "";
        else if (character.equals("\u001B"))
            character = "";

        if (Objects.equals("|", model.getKeysList().get(model.getCurrentIndex()).getText())){
            model.incCurrentIndex();
            if (model.getCurrentIndex() >= model.getKeysList().size())
                controller.generateParagraph();

        }


        if (!model.isPaused() && !character.equals("")) {

            if (!model.isCurrWordLettersCounted()){
                controller.countLettersForWord();
            }

            if (!character.equals(" "))
                handleLetterTyped(character);
            else {
                handleSpaceTyped(character);
            }
        }

    }

    private void handleLetterTyped(String character) {

        List<Text> keysList = model.getKeysList();
        int indexOfLetter = model.getCurrentIndex();

        if (indexOfLetter == 1)
            model.setSomethingTyped(true);


        if (Objects.equals(character, keysList.get(indexOfLetter).getText())) {// if correct letter
            Text successText = keysList.get(indexOfLetter);
            successText.setFont(Font.font("Consolas", 20));
            successText.setFill(Color.valueOf("#007010"));
            moveCursor(indexOfLetter);
            model.incCurrentIndex();
            model.incCorrectCount();
        } else if (keysList.get(indexOfLetter).getText().equals(" ")) {// needed space, recived letter
            Text errorText = new Text(character);
            errorText.setFont(Font.font("Consolas", 20));
            errorText.setFill(Color.valueOf("#cc5c00"));
            view.getMainText().getChildren().add(indexOfLetter, errorText);
            model.getKeysList().add(indexOfLetter, errorText);
            moveCursor(indexOfLetter);
            model.incCurrentIndex();
            model.incExtraCount();
        } else {// if wrong letter
            Text errorText = keysList.get(indexOfLetter);
            errorText.setFont(Font.font("Consolas", 20));
            errorText.setFill(Color.valueOf("#af2200"));
            moveCursor(indexOfLetter);
            model.incCurrentIndex();
            model.incMistakeCount();

        }
        model.incNumberOfCharsInSec();

    }

    private void handleSpaceTyped(String character) {
        if (model.getCurrentIndex() == 1)
            model.setSomethingTyped(true);
        List<Text> keysList = model.getKeysList();
        if (!keysList.get(model.getCurrentIndex()).getText().equals(" ") && character.equals(" ")) {
            while (!keysList.get(model.getCurrentIndex()).getText().equals(" ")) {
                Text errorText = keysList.get(model.getCurrentIndex());
                errorText.setFont(Font.font("Consolas", 20));
                errorText.setFill(Color.valueOf("#151515FF"));
                model.incCurrentIndex();
                model.incSkippedCount();
                model.incNumberOfCharsInSec();
            }
            controller.countWPM_forEveryWord();
            moveCursor(model.getCurrentIndex());
            model.incCurrentIndex();
            model.incSkippedCount();

        } else {
            model.setCurrWordLettersCounted(false);
            controller.countWPM_forEveryWord();
            moveCursor(model.getCurrentIndex());
            model.incCurrentIndex();
            model.incCorrectCount();
        }

        if (model.getCurrentIndex() >= model.getKeysList().size())
            controller.generateParagraph();


    }

    private void moveCursor( int newIndex){
        model.getKeysList().remove(view.cursor);
        model.getKeysList().add(newIndex, view.cursor);

        view.getMainText().getChildren().remove(view.cursor);
        view.getMainText().getChildren().add(newIndex,view.cursor);

    }


}
