import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ShortcutExample extends Application {

    private boolean tabPressed = false;

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 300, 200);

        // Ustawianie obsługi zdarzeń dla sceny
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.TAB) {
                    tabPressed = true;
                } else if (tabPressed && event.getCode() == KeyCode.ENTER) {
                    // Obsługa skrótu klawiszowego Tab + Enter
                    showAlert("Wykryto skrót klawiszowy: Tab + Enter");
                    tabPressed = false; // Zresetowanie stanu przycisku Tab
                } else {
                    tabPressed = false; // Zresetowanie stanu przycisku Tab
                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Metoda pomocnicza do wyświetlania alertu
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Skrót klawiszowy");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
