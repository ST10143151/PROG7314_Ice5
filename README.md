# Hangman 2025
Canvas-drawn gallows + XML keyboard, Firebase Auth, Firestore leaderboard.

## Setup
1. Create a Firebase project.
2. Enable **Authentication → Email/Password**.
3. Add Android app with package `com.example.hangman2025`; download **google-services.json** into `app/`.
4. Enable Firestore (production). Apply `firestore.rules`.
5. Build & run.

## Gameplay
- Tap letters (or use physical keyboard) to guess.
- 6 wrong attempts allowed. Score: `100 + 10 * remainingAttempts` on a win; otherwise 0.
- Score auto-saves on game end with the secret word.
