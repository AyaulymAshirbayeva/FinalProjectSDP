package LanguageLearningApp.factory.products;

import LanguageLearningApp.models.Word;
import java.util.List;

public interface ExerciseInterface {
    Word getWord();
    String getPrompt();
    List<String> getOptions();
    boolean checkAnswer(String answer);
}
