
import org.fxmisc.richtext.StyleClassedTextArea;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
public class WorkingGreenText extends Application {

    @Override
    public void start(Stage primaryStage) {
        StyleClassedTextArea textArea = new StyleClassedTextArea();
        textArea.appendText("Hello, world!");

        // Ustawienie stylu dla zielonego tekstu
        textArea.setStyleClass(0, 5, "green-text");

        StackPane root = new StackPane(textArea);
        Scene scene = new Scene(root, 400, 300);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
