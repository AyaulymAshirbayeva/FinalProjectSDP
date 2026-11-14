package LanguageLearningApp.decorator.decorators;

import LanguageLearningApp.factory.products.ExerciseInterface;
import LanguageLearningApp.factory.products.ExerciseText;

public final class TranslationDecorator extends DecoratorAbstract {
    private final String translation;

    public TranslationDecorator(ExerciseInterface exercise) {
        super(exercise);
        translation = ExerciseText.requireText(exercise.getWord().getTranslation(), "Translation");
    }

    @Override
    public String getPrompt() {
        return super.getPrompt() + "\nTranslation: " + translation;
    }
}
