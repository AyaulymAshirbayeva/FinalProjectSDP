package language_learning_app.factory.products;

import language_learning_app.models.Word;
import java.util.List;

public interface ExerciseInterface {
    Word getWord();
    String getPrompt();
    List<String> getOptions();
    boolean checkAnswer(String answer);
}
