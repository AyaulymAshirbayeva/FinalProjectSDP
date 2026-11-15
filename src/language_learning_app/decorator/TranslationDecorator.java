package language_learning_app.decorator;

import language_learning_app.factory.products.ExerciseInterface;
import language_learning_app.factory.products.ExerciseText;

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
