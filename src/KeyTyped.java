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


        if (!model.isPaused() && !character.equals("")) {
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

        if (indexOfLetter == 0)
            model.setSomethingTyped(true);


        if (Objects.equals(character, keysList.get(indexOfLetter).getText())) {// if correct letter
            Text successText = keysList.get(indexOfLetter);
            successText.setFont(Font.font("Consolas", 20));
            successText.setFill(Color.valueOf("#007010"));
            model.incCurrentIndex();
            model.incCorrectCount();
        } else if (keysList.get(indexOfLetter).getText().equals(" ")) {// needed space, recived letter
            Text errorText = keysList.get(indexOfLetter - 1);
//            Text errorText = new Text(character);
            errorText.setFont(Font.font("Consolas", 20));
            errorText.setFill(Color.ORANGE);
//            keysList.add(indexOfLetter, errorText);

            errorText.setText(errorText.getText() + character);

            model.incExtraCount();
        } else {// if wrong letter
            Text errorText = keysList.get(indexOfLetter);
            errorText.setFont(Font.font("Consolas", 20));
            errorText.setFill(Color.valueOf("#B00303FF"));
            model.incCurrentIndex();
            model.incMistakeCount();

        }

    }

    private void handleSpaceTyped(String character) {
        if (model.getCurrentIndex() == 0)
            model.setSomethingTyped(true);
        List<Text> keysList = model.getKeysList();
        if (!keysList.get(model.getCurrentIndex()).getText().equals(" ") && character.equals(" ")) {
            while (!keysList.get(model.getCurrentIndex()).getText().equals(" ")) {
                Text errorText = keysList.get(model.getCurrentIndex());
                errorText.setFont(Font.font("Consolas", 20));
                errorText.setFill(Color.BLACK);
                model.incCurrentIndex();
                model.incSkippedCount();
            }
            controller.countWPM_forEveryWord();
            model.incCurrentIndex();
            model.incSkippedCount();

            model.incSpacesAtAll();
        } else {
            controller.countWPM_forEveryWord();
            model.incCurrentIndex();
            model.incCorrectCount();

            model.incSpacesAtAll();
        }

        if (model.getCurrentIndex() == model.getKeysList().size())
            controller.generateParagraph();


    }


}
