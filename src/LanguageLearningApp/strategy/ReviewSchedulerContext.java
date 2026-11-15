package LanguageLearningApp.strategy;

import LanguageLearningApp.models.Word;
import java.time.Clock;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReviewSchedulerContext {
    private final Map<Word, LocalDate> reviewDates = new LinkedHashMap<>();
    private final Clock clock;
    private StrategyInterface strategy;

    public ReviewSchedulerContext(StrategyInterface strategy) {
        this(strategy, Clock.systemDefaultZone());
    }

    public ReviewSchedulerContext(StrategyInterface strategy, Clock clock) {
        this.clock = Objects.requireNonNull(clock);
        setStrategy(strategy);
    }

    public void setStrategy(StrategyInterface strategy) {
        this.strategy = Objects.requireNonNull(strategy);
    }

    public boolean addWord(Word word) {
        return reviewDates.putIfAbsent(Objects.requireNonNull(word), LocalDate.now(clock)) == null;
    }

    public List<Word> dueWords() {
        LocalDate today = LocalDate.now(clock);
        return reviewDates.entrySet().stream()
                .filter(entry -> !entry.getValue().isAfter(today))
                .map(Map.Entry::getKey)
                .toList();
    }

    public LocalDate review(Word word, int quality) {
        LocalDate today = LocalDate.now(clock);
        LocalDate due = reviewDates.get(word);
        if (due == null || due.isAfter(today)) {
            throw new IllegalArgumentException("This word is not due for review.");
        }
        strategy.schedule(word, quality);
        LocalDate next = today.plusDays(word.getIntervalDays());
        reviewDates.put(word, next);
        return next;
    }
}
