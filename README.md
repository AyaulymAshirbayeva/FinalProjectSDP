# FinalProjectSDP
## Language Learning App
### SE-2507
### Team

| Member | Patterns |
| --- | --- |
| Arnur Marat | Factory Method, Decorator |
| Ayaulym Ashirbayeva | Facade, Observer |
| Kuttibay Tangatar | Builder, Strategy |


A console application for learning vocabulary in a foreign language. It was built as the final project for the **Software Design Patterns** course and demonstrates six GoF design patterns working together in one program.

## What the app does

The learner picks:

1. a language to study: **Russian**, **German** or **Chinese**;
2. a lesson: **Greetings** or **Travel**;
3. an exercise type: **multiple choice** or **fill in the blank**;
4. a review algorithm: **SM-2** or **basic intervals**.

The app then runs the lesson, checks the answers, tracks progress and schedules each word for later review.

During an exercise the learner can type `?` to reveal the English translation. After the lesson the app shows the words that are due for review and asks how well each one was remembered (0-5). Finally it prints the progress summary and the list of scheduled reviews.

## Design patterns used

| Pattern | Type | Role in the app | Package |
| --- | --- | --- | --- |
| Factory Method | Creational | Creates exercises (multiple choice / fill in the blank) | `factory` |
| Builder | Creational | Builds a lesson step by step | `builder` |
| Decorator | Structural | Adds a hint and a translation to an exercise at runtime | `decorator` |
| Facade | Structural | One simple entry point for a whole learning session | `facade` |
| Strategy | Behavioral | Chooses how the next review date is calculated | `strategy` |
| Observer | Behavioral | Updates progress and reminders when events happen | `observer` |

## Project structure

```
src/language_learning_app/
├── Main.java                  # console menu and program entry point
├── models/                    # Word, User, Course
├── builder/                   # BuilderInterface, StandardLessonBuilder, LessonDirector, LessonProduct
├── factory/
│   ├── creators/              # CreatorAbstract, MultipleChoiceCreator, FillBlankCreator
│   └── products/              # ExerciseInterface, ExerciseAbstract, MultipleChoiceExercise,
│                              # FillBlankExercise, ExerciseText
├── decorator/                 # DecoratorAbstract, HintDecorator, TranslationDecorator
├── strategy/                  # StrategyInterface, SM2Strategy, BasicIntervalStrategy,
│                              # ReviewSchedulerContext
├── observer/
│   ├── subject/               # SubjectInterface, LearningEventPublisher
│   └── concrete_observers/    # ObserverInterface, ProgressTracker, ReminderService
└── facade/                    # LearningSessionFacade
```

### Data models (`models`)

- **Word**: language, term, translation, hint, example sentence, plus review data (repetitions, interval in days, easiness factor).
- **User**: name, languages being studied, learned words.
- **Course**: a language and its lessons; checks that a lesson matches the course language.

## Requirements
- JDK 17 or newer
- UTF-8 is needed to display Russian and Chinese text correctly in the console.
