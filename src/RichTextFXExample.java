import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.StyledTextArea;

public class RichTextFXExample extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Tworzenie obszaru tekstowego RichTextFX
        StyleClassedTextArea textArea = new StyleClassedTextArea();


        textArea.setPrefSize(400, 300);

        // Tworzenie przycisku pogrubienia
        ToggleButton boldButton = new ToggleButton("Pogrubienie");
        boldButton.setOnAction(event -> {
            FontWeight fontWeight = boldButton.isSelected() ? FontWeight.BOLD : FontWeight.NORMAL;
            textArea.setStyle("-fx-font-weight: " + fontWeight.toString());
        });
//        ToggleButton makeGreenButton = new ToggleButton("Make green");
//        makeGreenButton.setOnAction(event -> {
////            textArea.setStyle("-fx-fill: " + "green");
//
//        });


        // Tworzenie paska narzędzi
        ToolBar toolbar = new ToolBar();
        toolbar.getItems().add(boldButton);
//        toolbar.getItems().add(makeGreenButton);

        // Tworzenie głównego kontenera
        BorderPane root = new BorderPane();
        root.setTop(toolbar);
        root.setCenter(textArea);

        // Tworzenie sceny i wyświetlanie
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Prosty edytor RichTextFX");
        primaryStage.show();
    }
}
