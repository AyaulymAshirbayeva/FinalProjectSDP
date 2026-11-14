package LanguageLearningApp.factory.products;

import LanguageLearningApp.models.Word;
import java.util.List;

/** Shared contract for factory products and their decorators. No console input here. */
public interface ExerciseInterface {
    Word getWord();
    String getPrompt();
    List<String> getOptions();
    boolean checkAnswer(String answer);
}
