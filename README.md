# Hangman 2025
Canvas-drawn gallows + XML keyboard, Firebase Auth, Firestore leaderboard.

## Setup
1. Create a Firebase project.
2. Enable Authentication → Email/Password.
3. Add Android app with package `com.example.hangman2025`; download `google-services.json` into `app/`.
4. Enable Firestore (production). Apply `firestore.rules`.
5. Build & run.

## Firebase configuration and Offline mode

- The build automatically applies the Google Services plugin if `app/google-services.json` exists. If it is missing, the build proceeds in offline mode.
- Offline mode behavior:
  - Auth screen is skipped and the username defaults to "Player" (stored in SharedPreferences).
  - Scores are not uploaded and the leaderboard will be empty.
  - Gameplay works fully locally.

To enable online features, place a valid `google-services.json` in `app/` and rebuild.

## Gameplay
- Tap letters (or use physical keyboard) to guess.
- 6 wrong attempts allowed. Score: 100 + 10 × remainingAttempts on a win; otherwise 0.
- Score auto-saves on game end with the secret word.
