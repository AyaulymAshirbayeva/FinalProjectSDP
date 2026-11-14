package LanguageLearningApp.decorator;

import LanguageLearningApp.factory.products.ExerciseInterface;
import LanguageLearningApp.factory.products.ExerciseText;

public final class HintDecorator extends DecoratorAbstract {
    private final String hint;

    public HintDecorator(ExerciseInterface exercise) {
        super(exercise);
        hint = ExerciseText.requireText(exercise.getWord().getHint(), "Hint");
    }

    @Override
    public String getPrompt() {
        return super.getPrompt() + "\nHint: " + hint;
    }
}
