//import javafx.application.Application;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.input.KeyCode;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.scene.text.Text;
//import javafx.scene.text.TextFlow;
//import javafx.stage.Stage;
//
//public class KeyEntryApp_WithoutTextarea extends Application {
//    private static final String KEY = "dziś jest poniedziałek";
//    private Text keyLabel;
//    private TextFlow resLabel;
//    String character;
//
//    @Override
//    public void start(Stage primaryStage) {
//        resLabel = new TextFlow();
//        resLabel.setLineSpacing(5);
//        keyLabel = new Text(KEY);
//        keyLabel.setFont(Font.font("Arial", 20));
//        keyLabel.setFill(Color.LIGHTGRAY);
//
//        HBox labelsBox = new HBox(0);
//        labelsBox.setAlignment(Pos.CENTER);
//        labelsBox.getChildren().addAll(resLabel, keyLabel);
//
//        VBox root = new VBox(0);
//        root.setAlignment(Pos.CENTER);
//        root.getChildren().add(labelsBox);
//
//        Scene scene = new Scene(root, 400, 200);
//
//        scene.setOnKeyPressed(event -> {
//            KeyCode keyCode = event.getCode();
//            character = keyCode.toString().toLowerCase();
//            if (event.isAltDown() && keyCode == KeyCode.S) {
//                character = "ś";
//                handleKeyTyped(character);
//            } else if (event.isAltDown() && keyCode == KeyCode.L) {
//                character = "ł";
//                handleKeyTyped(character);
//            } else if (!character.equals("control") && !character.equals("alt_graph")) {
//                handleKeyTyped(character);
//            }
//        });
//
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Test App");
//        primaryStage.show();
//    }
//
//    private void handleKeyTyped(String character) {
//        if (character.equals("space"))
//            character = " ";
//
//        if (character.charAt(0) == keyLabel.getText().charAt(0)) {
//            Text successText = new Text(character);
//            successText.setFont(Font.font("Arial", 20));
//            successText.setFill(Color.GREEN);
//            resLabel.getChildren().add(successText);
//
//            String prevText = keyLabel.getText();
//            String newText = prevText.substring(0, 0) + prevText.substring(1);
//            keyLabel.setText(newText);
//
//        }else if (keyLabel.getText().charAt(0) != ' ' && character.equals(" ")){
//            while (keyLabel.getText().charAt(0) != ' '){
//                Text errorText = new Text(keyLabel.getText().charAt(0)+ "");
//                errorText.setFont(Font.font("Arial", 20));
//                errorText.setFill(Color.BLACK);
//                resLabel.getChildren().add(errorText);
//                String prevText = keyLabel.getText();
//                String newText = prevText.substring(0, 0) + prevText.substring(1);
//                keyLabel.setText(newText);
//            }
//            handleKeyTyped(" ");
//        }else if (keyLabel.getText().charAt(0) == ' '){
//            Text errorText = new Text(character);
//            errorText.setFont(Font.font("Arial", 20));
//            errorText.setFill(Color.ORANGE);
//            resLabel.getChildren().add(errorText);
//        }
//        else {
//            Text errorText = new Text(character);
//            errorText.setFont(Font.font("Arial", 20));
//            errorText.setFill(Color.RED);
//            resLabel.getChildren().add(errorText);
//
//            String prevText = keyLabel.getText();
//            String newText = prevText.substring(0, 0) + prevText.substring(1);
//            keyLabel.setText(newText);
//        }
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
