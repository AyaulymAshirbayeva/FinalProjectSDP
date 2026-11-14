package LanguageLearningApp.decorator;

import LanguageLearningApp.factory.products.ExerciseInterface;
import LanguageLearningApp.models.Word;
import java.util.List;
import java.util.Objects;

public abstract class DecoratorAbstract implements ExerciseInterface {
    private final ExerciseInterface exercise;

    protected DecoratorAbstract(ExerciseInterface exercise) {
        this.exercise = Objects.requireNonNull(exercise, "Wrapped exercise must not be null.");
    }

    @Override
    public final Word getWord() {
        return exercise.getWord();
    }

    @Override
    public String getPrompt() {
        return exercise.getPrompt();
    }

    @Override
    public final List<String> getOptions() {
        return exercise.getOptions();
    }

    @Override
    public final boolean checkAnswer(String answer) {
        return exercise.checkAnswer(answer);
    }
}
