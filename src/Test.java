import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpan;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Test extends Application {
    private static final String STYLE_CLASS_RED = "red-text";
    private static final String STYLE_CLASS_GREEN = "green-text";

    @Override
    public void start(Stage primaryStage) {
        CodeArea codeArea = new CodeArea();


        codeArea.textProperty().addListener((obs, oldText, newText) -> {
            codeArea.setStyleSpans(0, computeHighlighting(newText));
        });
        codeArea.getStylesheets().add(Test.class.getResource("styles.css").toExternalForm());


        VBox root = new VBox(codeArea);
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Text Editor");


        primaryStage.show();
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        List<String> styleClasses = new ArrayList<>();
        StringBuilder plainTextBuilder = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == 'a') {
                if (plainTextBuilder.length() > 0) {
                    styleClasses.add("");
                    spansBuilder.add(Collections.emptyList(), plainTextBuilder.length());
                    plainTextBuilder.setLength(0);
                }

                styleClasses.add(STYLE_CLASS_RED);
                plainTextBuilder.append(c);
            } else if (c == 'b') {
                if (plainTextBuilder.length() > 0) {
                    styleClasses.add("");
                    spansBuilder.add(Collections.emptyList(), plainTextBuilder.length());
                    plainTextBuilder.setLength(0);
                }

                styleClasses.add(STYLE_CLASS_GREEN);
                plainTextBuilder.append(c);
            } else {
                plainTextBuilder.append(c);
            }
        }

        styleClasses.add("");
        spansBuilder.add(styleClasses, plainTextBuilder.length());

        return spansBuilder.create();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
