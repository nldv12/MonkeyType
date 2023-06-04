//import javafx.application.Application;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//
//public class KeyEntryApp extends Application {
//    private static final String KEY = "dziś jest Poniedziałek";
//    private TextField textField;
//    private Label keyLabel;
//    private Label resLabel;
//
//    @Override
//    public void start(Stage primaryStage) {
//        keyLabel = new Label(KEY);
//        resLabel = new Label();
//        textField = new TextField();
//        textField.setOnKeyTyped(event -> handleKeyTyped(event.getCharacter()));
//
//        VBox root = new VBox(10);
//        root.setAlignment(Pos.CENTER);
//        root.getChildren().addAll(keyLabel, resLabel, textField);
//
//        primaryStage.setScene(new Scene(root, 400, 200));
//        primaryStage.setTitle("Key Entry App");
//        primaryStage.show();
//    }
//
//    private void handleKeyTyped(String character) {
//        String currentText = textField.getText();
//
//        if (currentText.length() < KEY.length() && character.charAt(0) == KEY.charAt(currentText.length() - 1)) {
//            if (currentText.length() == 1) {
//                resLabel.setText(character);
//            }
//            else {
//                resLabel.setText(resLabel.getText()+ character);
//            }
//            keyLabel.setText(KEY.substring(currentText.length() ));
//        } else {
//            showAlert("Błąd", "Niepoprawny znak");
//        }
//    }
//
//    private void showAlert(String title, String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
