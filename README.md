# Tatkal Assistant (starter project)

A dark-themed Android companion for preparing journey and passenger details before using the official IRCTC Rail Connect app.

## Important scope
This is a companion utility, not a booking bot. It does not log in to IRCTC, interact with its screens, bypass CAPTCHA, submit bookings, or make payments. Complete all booking steps and security checks yourself in the official app.

## Build without installing Android Studio
1. Create/sign in to a GitHub account.
2. Create a new repository and upload the contents of this folder.
3. Open the repository's **Actions** tab.
4. Select **Build Android APK**, then **Run workflow** (or push to `main`).
5. When the run finishes, open it and download the `TatkalAssistant-debug-apk` artifact.
6. Extract the ZIP on your Android phone and install `app-debug.apk`. Android may ask you to allow installation from that source.

## Build locally (optional)
Open this folder in Android Studio and run the app, or run `gradle assembleDebug` with Android SDK and Java 17 configured.

## Notes
- The starter stores journey and passenger fields in app-private SharedPreferences on the phone. It is not encrypted storage; avoid entering highly sensitive information.
- Do not save IRCTC passwords, OTPs, card details, or UPI PINs.
- This project is an initial prototype. Test it with dummy information before using real passenger details.
