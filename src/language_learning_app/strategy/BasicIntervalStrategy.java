package language_learning_app.strategy;

import language_learning_app.models.Word;

public class BasicIntervalStrategy implements StrategyInterface {
    @Override
    public void schedule(Word word, int quality) {
        StrategyInterface.validateQuality(quality);
        int interval = quality < 3 ? 1 : quality < 5 ? 3 : 7;
        word.setIntervalDays(interval);
        word.setRepetitions(quality < 3 ? 0 : word.getRepetitions() + 1);
    }
}
