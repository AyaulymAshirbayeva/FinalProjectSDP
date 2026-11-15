package language_learning_app.strategy;

import language_learning_app.models.Word;

public class SM2Strategy implements StrategyInterface {
    @Override
    public void schedule(Word word, int quality) {
        StrategyInterface.validateQuality(quality);
        int repetitions = quality < 3 ? 0 : word.getRepetitions() + 1;
        int interval = switch (repetitions) {
            case 0, 1 -> 1;
            case 2 -> 6;
            default -> (int) Math.min(Integer.MAX_VALUE,
                    Math.ceil(word.getIntervalDays() * word.getEasinessFactor()));
        };
        int missed = 5 - quality;
        double ease = word.getEasinessFactor() + 0.1 - missed * (0.08 + missed * 0.02);
        word.setRepetitions(repetitions);
        word.setIntervalDays(interval);
        word.setEasinessFactor(ease);
    }
}
