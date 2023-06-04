import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class KeyEntryApp_WithoutTextarea extends Application {
    private static final String KEY = "dziś jest poniedziałek";
    private Label keyLabel;
    private Label resLabel;

    String character;

    @Override
    public void start(Stage primaryStage) {
        keyLabel = new Label(KEY);
        resLabel = new Label();

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(keyLabel, resLabel);

        Scene scene = new Scene(root, 400, 200);

        scene.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            character = keyCode.toString().toLowerCase();
            if (event.isAltDown() && keyCode == KeyCode.S){
                character = "ś";
                handleKeyTyped(character);
            }
            else if (event.isAltDown() && keyCode == KeyCode.L){
                character = "ł";
                handleKeyTyped(character);
            }
            else if (!character.equals("control") && !character.equals("alt_graph")){
                handleKeyTyped(character);

            }

        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Test App");
        primaryStage.show();
    }

    private void handleKeyTyped(String character) {
        if (character.equals("space"))
            character = " ";

        if (character.charAt(0) == keyLabel.getText().charAt(0)) {
            resLabel.setText(resLabel.getText()+ character);
            String prevText = keyLabel.getText();
            String newText = prevText.substring(0, 0) + prevText.substring(1);
            keyLabel.setText(newText);
        } else {
            showAlert("Błąd", "Niepoprawny znak");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
