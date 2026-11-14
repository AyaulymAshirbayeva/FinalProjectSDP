package LanguageLearningApp.factory.creators;

import LanguageLearningApp.factory.products.ExerciseInterface;
import LanguageLearningApp.factory.products.ExerciseText;
import LanguageLearningApp.factory.products.MultipleChoiceExercise;
import LanguageLearningApp.models.Word;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public final class MultipleChoiceCreator extends CreatorAbstract {
    private static final int MAX_OPTIONS = 4;
    private final Random random;

    public MultipleChoiceCreator() {
        this(new Random());
    }

    public MultipleChoiceCreator(Random random) {
        this.random = Objects.requireNonNull(random, "Random must not be null.");
    }

    @Override
    public ExerciseInterface createExercise(Word word, List<Word> vocabulary) {
        Objects.requireNonNull(word, "Word must not be null.");
        Objects.requireNonNull(vocabulary, "Vocabulary must not be null.");
        String term = ExerciseText.requireText(word.getTerm(), "Word term");
        String language = ExerciseText.normalize(ExerciseText.requireText(word.getLanguage(), "Word language"));
        String translation = ExerciseText.normalize(ExerciseText.requireText(word.getTranslation(), "Word translation"));
        Map<String, String> distractors = new LinkedHashMap<>();
        for (Word candidate : vocabulary) {
            Objects.requireNonNull(candidate, "Vocabulary must not contain null words.");
            if (!language.equals(ExerciseText.normalize(candidate.getLanguage()))
                    || translation.equals(ExerciseText.normalize(candidate.getTranslation()))) {
                continue;
            }
            String candidateTerm = ExerciseText.requireText(candidate.getTerm(), "Candidate term");
            ExerciseText.requireText(candidate.getTranslation(), "Candidate translation");
            String key = ExerciseText.normalize(candidateTerm);
            if (!key.equals(ExerciseText.normalize(term))) {
                distractors.putIfAbsent(key, candidateTerm);
            }
        }
        if (distractors.isEmpty()) {
            throw new IllegalArgumentException("Multiple choice needs another distinct word in the same language."
                    + " Add vocabulary or use fill-in-the-blank exercises.");
        }
        List<String> candidates = new ArrayList<>(distractors.values());
        Collections.shuffle(candidates, random);
        List<String> options = new ArrayList<>(candidates.subList(0, Math.min(MAX_OPTIONS - 1, candidates.size())));
        options.add(term);
        Collections.shuffle(options, random);
        return new MultipleChoiceExercise(word, options);
    }
}
