package LanguageLearningApp.factory.products;

import LanguageLearningApp.models.Word;
import java.util.List;
import java.util.Objects;

public abstract class ExerciseAbstract implements ExerciseInterface {
    private final Word word;

    protected ExerciseAbstract(Word word) {
        this.word = Objects.requireNonNull(word, "Word must not be null.");
        ExerciseText.requireText(word.getLanguage(), "Word language");
        ExerciseText.requireText(word.getTerm(), "Word term");
        ExerciseText.requireText(word.getTranslation(), "Word translation");
    }

    @Override
    public final Word getWord() {
        return word;
    }

    @Override
    public List<String> getOptions() {
        return List.of();
    }

    @Override
    public boolean checkAnswer(String answer) {
        return ExerciseText.normalize(word.getTerm()).equals(ExerciseText.normalize(answer));
    }
}
