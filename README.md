# تطبيق تعليمي — EduApp

Offline-first Android educational app (Arabic, RTL) built with Jetpack Compose,
Clean Architecture, Hilt, and Room.

## Features
- **Lessons** — categories → chapters → lessons, Arabic Markdown content, read offline.
- **Exam Simulation** — timed, random questions, distraction-free mode, results analysis.
- **Flashcards** — SM-2 spaced repetition, daily revision mode.
- **Search** — Room FTS4 full-text search across lessons, questions, flashcards.
- **Progress** — completed lessons, average exam score, review stats.
- Branded splash screen, light/dark Material 3 theme, full RTL.
- **Cairo** Arabic font bundled (OFL 1.1) — see `docs/FONTS.md`.

## Architecture
Single `:app` module organized by clean-architecture packages:
- `domain/` — pure Kotlin models, use cases, repository interfaces (no Android deps)
- `data/`   — Room (entities, DAOs, FTS), mappers, seeders, repository impls
- `feature/`— Compose screens + ViewModels per feature
- `di/`     — Hilt modules wiring everything together
- `core/`   — theme, shared UI, common utils

## Requirements
- Android Studio (Koala or newer), JDK 17, Android SDK 34

## Build & Run
1. Open the project in Android Studio; let Gradle sync.
2. Run on a device/emulator (▶), or build an APK:

```bash
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk
```

## Notes
- DB seeded with sample Arabic content on first launch; FTS index built automatically.
- `fallbackToDestructiveMigration()` is enabled for development.
- Gradle wrapper JAR not bundled; Android Studio regenerates it on first sync,
  or run `gradle wrapper` once if you have Gradle installed.

## Continuous Integration
A GitHub Actions workflow at `.github/workflows/android.yml` runs on every push
and pull request to `main`/`master` (and via manual dispatch):
- Builds the debug APK (`assembleDebug`)
- Runs Android Lint (`lintDebug`)
- Uploads the APK and the lint report as downloadable build artifacts

It auto-generates the Gradle wrapper JAR if it's missing, so the repo builds on
CI even though the wrapper binary isn't committed.
