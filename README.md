# Habit-Wallet AI

**Build better money habits. Make healthier choices. Stay accountable.**

Habit-Wallet AI is an Android fintech and habit-accountability app that brings personal budgeting and dietary discipline together. It helps users understand spending patterns, record transactions, make more intentional choices, and explore AI-powered insights through a polished, dark-mode interface.

> Built for **HACK-A-NIGHT 2026**.

## ✨ What it does

- **Financial dashboard** — get an at-a-glance view of your money habits and activity.
- **Budgeting** — review spending and manage your budget.
- **Transaction logging** — record expenses manually or start a log by scanning an image.
- **Spending insights** — surface patterns and AI-assisted insights to support better decisions.
- **Runway XAI** — explore financial runway and a rescue-plan experience designed to make financial outcomes easier to understand.
- **Health and food choices** — connect financial discipline with health goals and discover suggested swaps.
- **Social accountability** — view friend-related activity, reminders, and settlement interactions.
- **History** — revisit recorded activity.
- **Onboarding** — tailor the experience around an initial user survey.

Features and integrations may depend on configured API credentials and the current implementation.

## 🛠️ Tech stack

| Area | Technology |
| --- | --- |
| Platform | Android |
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| App architecture | ViewModel, lifecycle-aware state with Kotlin Flow |
| AI | Gemini via Firebase AI |
| Networking | Retrofit, OkHttp, Moshi |
| Backend integration | Supabase configuration |
| Image loading | Coil |
| Build system | Gradle with Kotlin DSL and version catalog |
| Testing | JUnit, AndroidX test, Compose UI testing, Robolectric |

## 🚀 Getting started

### Prerequisites

- Android Studio with a compatible Android SDK
- JDK 11 or a compatible JDK supported by your installed Android Gradle Plugin
- Access to the required Gemini and Supabase project credentials

### 1. Clone the repository

```bash
git clone https://github.com/gayatori-san/HACK-A-NIGHT.git
cd HACK-A-NIGHT
```

### 2. Configure environment values

Create an `.env` file in the `app/` directory, alongside `app/build.gradle.kts`. Use `app/.env.example` as the reference if you keep a copy there, or copy the root example and adjust the path:

```bash
cp .env.example app/.env
```

Set the values for your own projects:

```dotenv
GEMINI_API_KEY=your_gemini_api_key
SUPABASE_URL=https://your-project.supabase.co
SUPABASE_KEY=your_supabase_anon_key
```

Do not commit real API keys or other secrets. The repository's `.env.example` contains placeholders only.

If your local setup uses Firebase services, configure the corresponding Firebase project and add its Android configuration file as required by your setup.

### 3. Open and run

1. Open the cloned folder in Android Studio.
2. Allow Gradle to sync and install any requested SDK components.
3. Select the app run configuration and an Android emulator or connected device.
4. Run the app.

You can also build a debug APK from the project root:

```bash
./gradlew assembleDebug
```

The generated APK is normally located at `app/build/outputs/apk/debug/app-debug.apk`.

## 📁 Project structure

```text
HACK-A-NIGHT/
├── app/
│   ├── src/main/
│   │   ├── assets/
│   │   │   └── runway_engine.html
│   │   ├── java/com/example/
│   │   │   ├── data/
│   │   │   ├── ui/
│   │   │   ├── viewmodel/
│   │   │   └── MainActivity.kt
│   │   └── res/
│   └── build.gradle.kts
├── gradle/
│   └── libs.versions.toml
├── .env.example
├── build.gradle.kts
└── settings.gradle.kts
```

- **`ui/`** — Compose screens, reusable components, and theme.
- **`viewmodel/`** — UI state and user actions.
- **`data/`** — data-related models and logic.
- **`assets/runway_engine.html`** — the Runway finance and explainability interface asset.

## 🔐 Configuration and security

- Keep `.env`, service-account files, signing keys, and other credentials out of version control.
- Use only credentials intended for client-side use in an Android app. A public Supabase anon key must be paired with appropriate Row Level Security policies.
- Never embed privileged service-role keys or unrestricted server-side secrets in the mobile app.
- For production Gemini usage, prefer a trusted server-side integration where secrets and access can be controlled.
- Configure Firebase App Check and the relevant Firebase project settings when using those services.

## 🧪 Tests

Run the available local unit tests from the repository root:

```bash
./gradlew test
```

Run Android instrumented tests on a configured emulator or device:

```bash
./gradlew connectedAndroidTest
```

Some tests and integrations may require a configured Android SDK, emulator/device, or external service credentials.

## 👥 Team

HACK-A-NIGHT 2026 team:

- Gayatri Giri
- Samyak Gawale
- Satyam Musmade
- Sneha Gade
- Gayatri Karale

## 🗺️ Roadmap

Potential next steps include strengthening transaction categorization, improving the explainability of AI-generated insights, expanding budget and health-goal personalization, and hardening backend security and automated tests.

## ⚠️ Disclaimer

Habit-Wallet AI is a project intended to support awareness and habit-building. Its AI-generated suggestions are informational and should not be treated as professional financial, investment, medical, or nutritional advice.

## 📄 License

No license has been specified yet. Unless a license is added to this repository, all rights remain with the copyright holder and reuse is not automatically permitted.
