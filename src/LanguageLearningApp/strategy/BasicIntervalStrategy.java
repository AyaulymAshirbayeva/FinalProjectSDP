package LanguageLearningApp.strategy;

import LanguageLearningApp.models.Word;

public class BasicIntervalStrategy implements StrategyInterface {
    @Override
    public void schedule(Word word, int quality) {
        StrategyInterface.validateQuality(quality);
        int interval = quality < 3 ? 1 : quality < 5 ? 3 : 7;
        word.setIntervalDays(interval);
        word.setRepetitions(quality < 3 ? 0 : word.getRepetitions() + 1);
    }
}
