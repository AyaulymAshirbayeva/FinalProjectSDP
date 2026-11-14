package LanguageLearningApp.tests;

import LanguageLearningApp.builder.LessonDirector;
import LanguageLearningApp.builder.LessonProduct;
import LanguageLearningApp.builder.StandardLessonBuilder;
import LanguageLearningApp.factory.creators.CreatorAbstract;
import LanguageLearningApp.factory.creators.FillBlankCreator;
import LanguageLearningApp.factory.creators.MultipleChoiceCreator;
import LanguageLearningApp.factory.products.ExerciseInterface;
import LanguageLearningApp.factory.products.FillBlankExercise;
import LanguageLearningApp.factory.products.MultipleChoiceExercise;
import LanguageLearningApp.models.Word;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class FactoryTest {
    public static void main(String[] args) {
        LessonProduct lesson = new LessonDirector(new StandardLessonBuilder()).greetings();
        Word hello = lesson.getVocabulary().get(0);
        CreatorAbstract creator = new MultipleChoiceCreator(new Random(7));
        List<ExerciseInterface> exercises = creator.createExercises(lesson);
        Checks.equal(2, exercises.size(), "One exercise per lesson word");
        ExerciseInterface exercise = exercises.get(0);
        Checks.that(exercise instanceof MultipleChoiceExercise, "Concrete creator chooses MC product");
        Checks.that(exercise.getWord() == hello, "Keep original Word for review scheduler identity");
        Checks.equal(2, exercise.getOptions().size(), "Small lessons use two real choices");
        Checks.that(exercise.checkAnswer("  HELLO  "), "Case and outer whitespace do not matter");
        for (int i = 0; i < exercise.getOptions().size(); i++) {
            Checks.equal(exercise.getOptions().get(i).equals("hello"), exercise.checkAnswer("" + (i + 1)),
                    "Number selects the displayed option");
        }
        for (String invalid : Arrays.asList(null, "", "wrong", "0", "-1", "3", "9999999999999999999999")) {
            Checks.that(!exercise.checkAnswer(invalid), "Invalid MC answer is incorrect: " + invalid);
        }
        Checks.throwsType(UnsupportedOperationException.class, () -> exercise.getOptions().add("bad"),
                "Options are immutable");
        Checks.throwsType(UnsupportedOperationException.class, exercises::clear, "Exercise list is immutable");

        List<Word> vocabulary = new ArrayList<>(lesson.getVocabulary());
        vocabulary.add(new Word("English", "THANKS", "another translation", "", "___"));
        vocabulary.add(new Word("English", "hi", hello.getTranslation(), "", "___"));
        vocabulary.add(new Word("German", "Hallo", "hello", "", "___"));
        ExerciseInterface filtered = creator.createExercise(hello, vocabulary);
        Checks.equal(2, filtered.getOptions().size(), "Exclude duplicate terms, synonyms and other languages");
        Checks.that(!filtered.getOptions().contains("hi") && !filtered.getOptions().contains("Hallo"),
                "No ambiguous or wrong-language distractors");
        for (int i = 0; i < 10; i++) {
            vocabulary.add(new Word("English", "term" + i, "meaning" + i, "", "___"));
        }
        Checks.equal(4, creator.createExercise(hello, vocabulary).getOptions().size(), "At most four choices");
        Checks.throwsType(IllegalArgumentException.class, () -> creator.createExercise(hello, List.of(hello)),
                "Insufficient vocabulary must not fabricate distractors");
        Checks.throwsType(IllegalArgumentException.class,
                () -> new MultipleChoiceExercise(hello, List.of("hello", " HELLO ")), "Reject duplicate choices");
        Checks.throwsType(IllegalArgumentException.class,
                () -> new MultipleChoiceExercise(hello, List.of("one", "two")), "Require correct choice");
        Checks.throwsType(IllegalArgumentException.class,
                () -> new MultipleChoiceExercise(hello, List.of("hello")), "Require a distractor");
        Checks.throwsType(IllegalArgumentException.class,
                () -> new MultipleChoiceExercise(hello, List.of("hello", " ")), "Reject blank option");
        Checks.throwsType(NullPointerException.class, () -> creator.createExercise(null, vocabulary), "Reject null word");
        Checks.throwsType(NullPointerException.class, () -> creator.createExercise(hello, null), "Reject null vocabulary");
        Checks.throwsType(NullPointerException.class,
                () -> creator.createExercise(hello, Arrays.asList(hello, null)), "Reject null candidate");
        Checks.throwsType(IllegalArgumentException.class,
                () -> creator.createExercises(new LessonProduct(4, "Empty", List.of())), "Reject empty lesson");

        CreatorAbstract fillCreator = new FillBlankCreator();
        ExerciseInterface fill = fillCreator.createExercises(lesson).get(0);
        Checks.that(fill instanceof FillBlankExercise, "Concrete creator chooses fill-blank product");
        Checks.that(fill.getPrompt().contains("Say ___ to your friend."), "Use team's example sentence");
        Checks.equal(List.of(), fill.getOptions(), "Fill-blank has no choices");
        Checks.that(fill.checkAnswer("Hello") && !fill.checkAnswer("thanks") && !fill.checkAnswer(null), "Check typed word");
        Checks.that(fillCreator.createExercise(hello, List.of(hello)).checkAnswer("hello"), "Single-word lessons support blanks");
        for (String sentence : Arrays.asList(null, " ", "No gap", "___ and ___", "____")) {
            Checks.throwsType(IllegalArgumentException.class,
                    () -> new FillBlankExercise(new Word("English", "hello", "hi", "h", sentence)), "Require one gap");
        }
        Checks.throwsType(IllegalArgumentException.class,
                () -> new FillBlankExercise(new Word("English", " ", "hi", "h", "___")), "Reject blank term");
        ExerciseInterface french = new FillBlankExercise(new Word("French", "café", "coffee", "c", "Un ___."));
        Checks.that(french.checkAnswer("cafe\u0301"), "Normalize canonically equivalent Unicode");
        Checks.that(!french.checkAnswer("cafe"), "Do not discard accents in language learning");
        ExerciseInterface russian = new FillBlankExercise(new Word("Russian", "привет", "hello", "п", "Скажи ___."));
        Checks.that(russian.checkAnswer(" ПРИВЕТ "), "Support Cyrillic answers");
        Locale previous = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("tr"));
            Checks.that(new FillBlankExercise(new Word("English", "ticket", "билет", "t", "A ___"))
                    .checkAnswer("TICKET"), "Answer checking is independent of system locale");
        } finally {
            Locale.setDefault(previous);
        }
        Checks.report("FactoryTest");
    }
}
