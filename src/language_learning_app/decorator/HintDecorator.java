package language_learning_app.decorator;

import language_learning_app.factory.products.ExerciseInterface;
import language_learning_app.factory.products.ExerciseText;

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
