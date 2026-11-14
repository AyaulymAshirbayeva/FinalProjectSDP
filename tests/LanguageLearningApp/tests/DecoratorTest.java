package LanguageLearningApp.tests;

import LanguageLearningApp.builder.LessonDirector;
import LanguageLearningApp.builder.LessonProduct;
import LanguageLearningApp.builder.StandardLessonBuilder;
import LanguageLearningApp.decorator.decorators.HintDecorator;
import LanguageLearningApp.decorator.decorators.TranslationDecorator;
import LanguageLearningApp.factory.creators.CreatorAbstract;
import LanguageLearningApp.factory.creators.FillBlankCreator;
import LanguageLearningApp.factory.creators.MultipleChoiceCreator;
import LanguageLearningApp.factory.products.ExerciseInterface;
import LanguageLearningApp.factory.products.FillBlankExercise;
import LanguageLearningApp.models.Word;
import java.util.List;
import java.util.Random;

public class DecoratorTest {
    public static void main(String[] args) {
        LessonProduct lesson = new LessonDirector(new StandardLessonBuilder()).greetings();
        for (CreatorAbstract creator : List.of(new FillBlankCreator(), new MultipleChoiceCreator(new Random(9)))) {
            ExerciseInterface base = creator.createExercises(lesson).get(0);
            String originalPrompt = base.getPrompt();
            ExerciseInterface hint = new HintDecorator(base);
            ExerciseInterface translation = new TranslationDecorator(base);
            ExerciseInterface both = new TranslationDecorator(hint);
            ExerciseInterface reverse = new HintDecorator(translation);
            Checks.that(hint.getPrompt().contains("Hint: It starts with h."), "Hint uses team's Word model");
            Checks.that(translation.getPrompt().contains("Translation: привет"), "Translation uses Word model");
            Checks.equal(originalPrompt + "\nHint: It starts with h.\nTranslation: привет", both.getPrompt(),
                    "Decorators compose in wrapping order");
            Checks.equal(originalPrompt + "\nTranslation: привет\nHint: It starts with h.", reverse.getPrompt(),
                    "Reverse wrapping order also works");
            for (ExerciseInterface wrapped : List.of(hint, translation, both, reverse)) {
                Checks.that(wrapped.getWord() == base.getWord(), "Preserve Word identity for scheduling");
                Checks.equal(base.getOptions(), wrapped.getOptions(), "Preserve choices and their order");
                for (String answer : new String[] {"hello", " HELLO ", "thanks", "1", "2", "0", "", null}) {
                    Checks.equal(base.checkAnswer(answer), wrapped.checkAnswer(answer), "Decoration preserves grading");
                }
                Checks.equal(wrapped.getPrompt(), wrapped.getPrompt(), "Rendering has no side effects");
            }
            Checks.equal(originalPrompt, base.getPrompt(), "Wrapping does not mutate the original exercise");
        }

        // A different product proves decorators delegate, rather than reimplementing answer checking.
        int[] checks = {0};
        Word word = lesson.getVocabulary().get(0);
        ExerciseInterface custom = new ExerciseInterface() {
            public Word getWord() { return word; }
            public String getPrompt() { return "Custom exercise"; }
            public List<String> getOptions() { return List.of(); }
            public boolean checkAnswer(String answer) { checks[0]++; return "custom".equals(answer); }
        };
        ExerciseInterface wrapped = new HintDecorator(new TranslationDecorator(custom));
        Checks.that(wrapped.checkAnswer("custom"), "Works with a new product without changing decorators");
        Checks.equal(1, checks[0], "Delegates answer checking exactly once");
        Checks.throwsType(NullPointerException.class, () -> new HintDecorator(null), "Reject null hint component");
        Checks.throwsType(NullPointerException.class, () -> new TranslationDecorator(null), "Reject null translation component");
        ExerciseInterface missingHint = new FillBlankExercise(new Word("English", "hello", "привет", " ", "Say ___."));
        Checks.throwsType(IllegalArgumentException.class, () -> new HintDecorator(missingHint), "Reject missing hint explicitly");
        Checks.that(new TranslationDecorator(missingHint).checkAnswer("hello"), "Hint is optional for other exercises");
        Checks.report("DecoratorTest");
    }
}
