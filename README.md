# Habit-Wallet AI

**Build better money habits. Make healthier choices. Stay accountable.**

Habit-Wallet AI is an Android fintech and habit-accountability application that combines personal budgeting with dietary discipline. It helps users understand spending patterns, record transactions, make intentional financial decisions, and explore AI-powered insights.

> Built for **HACK-A-NIGHT 2026**.

## ✨ Features

- **Financial Dashboard** — View financial activity and money habits at a glance.
- **Budget Management** — Track spending and manage your budget.
- **Transaction Logging** — Record expenses manually or initiate logging by scanning an image.
- **AI-Powered Insights** — Explore spending patterns and receive AI-assisted insights.
- **Runway XAI Engine** — Understand financial runway and explore potential rescue plans through an explainability-focused interface.
- **Health & Food Choices** — Connect financial discipline with health goals and discover suggested alternatives.
- **Social Accountability** — Access friend-related activity, reminders, and settlement interactions.
- **Transaction History** — Revisit previously recorded activity.
- **Personalized Onboarding** — Tailor the experience through an initial user survey.

*Feature availability may depend on the current implementation and configured API credentials.*

## 🛠️ Tech Stack

| Component | Technology |
|---|---|
| Platform | Android |
| Programming Language | Kotlin |
| UI Framework | Jetpack Compose |
| UI Design | Material 3 |
| Architecture | ViewModel, lifecycle-aware state, Kotlin Flow |
| AI Integration | Gemini via Firebase AI |
| Networking | Retrofit, OkHttp, Moshi |
| Backend Configuration | Supabase |
| Image Loading | Coil |
| Build System | Gradle with Kotlin DSL |
| Testing | JUnit, AndroidX Test, Compose UI Testing, Robolectric |

## 🚀 Getting Started

### Prerequisites

- Android Studio
- Compatible Android SDK
- JDK version supported by the project's Android Gradle Plugin
- Gemini and Supabase project credentials, where required

### 1. Clone the Repository

```bash
git clone https://github.com/gayatori-san/HACK-A-NIGHT.git
cd HACK-A-NIGHT
```

### 2. Configure Environment Variables

Copy the root environment example into the app directory:

```bash
cp .env.example app/.env
```

Configure your own credentials:

```dotenv
GEMINI_API_KEY=your_gemini_api_key
SUPABASE_URL=https://your-project.supabase.co
SUPABASE_KEY=your_supabase_anon_key
```

**Security notes:**
- Never commit real API keys, passwords, or private credentials.
- Use a Supabase public/anon key only with properly configured Row Level Security policies.
- Never expose a Supabase service-role key in the Android application.
- Configure Firebase and its Android configuration file if required by your local setup.

### 3. Open the Project

1. Open the cloned repository in Android Studio.
2. Allow Gradle to sync.
3. Install any required SDK components.
4. Start an Android emulator or connect a device.
5. Select the app configuration and click **Run**.

### 4. Build the APK

From the project root, run:

```bash
./gradlew assembleDebug
```

The debug APK is normally generated at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## 📂 Project Structure

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

### Key Directories

- **`data/`** — Data-related models and application logic.
- **`ui/`** — Jetpack Compose screens, reusable components, and theme.
- **`viewmodel/`** — UI state management and user interactions.
- **`assets/runway_engine.html`** — Runway finance and explainability interface.
- **`gradle/libs.versions.toml`** — Centralized dependency and plugin versions.

## 🧪 Running Tests

Run local unit tests:

```bash
./gradlew test
```

Run instrumented Android tests on a configured emulator or device:

```bash
./gradlew connectedAndroidTest
```

Tests involving Android devices or external services may require additional configuration.

## 🔐 Security & Privacy

- Keep `.env` files and sensitive credentials out of version control.
- Apply appropriate authentication and authorization rules to backend resources.
- Protect user financial information and minimize unnecessary data collection.
- Use secure backend integrations for sensitive AI operations.
- Configure Firebase App Check when using the corresponding Firebase services.

## 👥 Team

**HACK-A-NIGHT 2026**

- Gayatri Giri
- Samyak Gawale
- Satyam Musmade
- Sneha Gade
- Gayatri Karale

## 🗺️ Future Improvements

- Improve automatic transaction categorization.
- Expand personalized budgeting and financial insights.
- Improve explainability of AI-generated recommendations.
- Enhance health-goal personalization.
- Strengthen backend security and automated testing.
- Improve reliability, accessibility, and overall user experience.

## ⚠️ Disclaimer

Habit-Wallet AI is a project designed to support financial awareness and habit-building. AI-generated suggestions are informational and should not be treated as professional financial, investment, medical, or nutritional advice.

## 📄 License

No license has been specified for this repository yet. Unless a license is added, the project remains subject to the copyright holder's rights, and reuse is not automatically permitted.
