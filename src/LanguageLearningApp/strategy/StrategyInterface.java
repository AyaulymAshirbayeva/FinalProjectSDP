package LanguageLearningApp.strategy;

import LanguageLearningApp.models.Word;

public interface StrategyInterface {
    void schedule(Word word, int quality);

    static void validateQuality(int quality) {
        if (quality < 0 || quality > 5) {
            throw new IllegalArgumentException("Review quality must be between 0 and 5.");
        }
    }
}
