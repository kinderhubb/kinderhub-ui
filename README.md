# KinderHub UI

A Kotlin Compose Multiplatform application for discovering, booking, and managing children's activities. Built with a single codebase targeting Android, iOS, and Web (WebAssembly).

> **AI-Driven Development:** This repository is configured for autonomous AI development. See [CLAUDE.md](CLAUDE.md) for AI agent instructions and [docs/](docs/) for product documentation.

## Quick Links

- [Vision & Mission](docs/vision.md)
- [Roadmap](docs/roadmap.md)
- [Architecture](architecture/system-overview.md)
- [Current Backlog](backlog/now.md)
- [AI Agent Instructions](.github/agent-instructions.md)

## Tech Stack

- **Kotlin** 2.1.0
- **Compose Multiplatform** 1.7.1
- **Kotlin Coroutines** 1.9.0
- **Ktor Client** 3.0.2 (networking)
- **Koin** 4.0.0 (dependency injection)
- **Kotlinx Serialization** 1.7.3
- **Navigation Compose** 2.8.0-alpha10

## Prerequisites

### All Platforms

- **JDK 17** or higher
- **Gradle 8.10** (included via wrapper)

### Android

- **Android Studio** Ladybug (2024.2.1) or higher
- **Android SDK** with:
  - Compile SDK: 34
  - Min SDK: 24
  - Target SDK: 34

### iOS

- **macOS** Sonoma 14.0 or higher
- **Xcode** 15.0 or higher
- **CocoaPods** (optional, for dependency management)

### Web

- Modern browser with WebAssembly support:
  - Chrome 119+
  - Firefox 120+
  - Safari 17+
  - Edge 119+

---

## Quick Start

Clone the repository and navigate to the project root:

```bash
cd kinderhub-ui
```

---

## Android

### Run on Emulator or Device

```bash
# List available devices
./gradlew composeApp:connectedDevices

# Run debug build on connected device/emulator
./gradlew composeApp:installDebug

# Or use Android Studio: Open project → Run 'composeApp'
```

### Build APK

```bash
# Debug APK
./gradlew composeApp:assembleDebug

# Release APK (requires signing configuration)
./gradlew composeApp:assembleRelease
```

**Output location:** `composeApp/build/outputs/apk/`

### Using Android Studio

1. Open Android Studio
2. Select **File → Open** and choose the `kinderhub-ui` directory
3. Wait for Gradle sync to complete
4. Select **composeApp** configuration
5. Choose an emulator or connected device
6. Click **Run** (▶️)

---

## iOS

### Prerequisites Setup

1. Install Xcode from the Mac App Store
2. Install Xcode Command Line Tools:
   ```bash
   xcode-select --install
   ```
3. Accept Xcode license:
   ```bash
   sudo xcodebuild -license accept
   ```

### Generate Xcode Project

The iOS app requires the Kotlin framework to be built first:

```bash
# Build the shared framework for iOS Simulator (ARM64 - Apple Silicon Macs)
./gradlew composeApp:linkDebugFrameworkIosSimulatorArm64

# Or for Intel Macs
./gradlew composeApp:linkDebugFrameworkIosX64

# Or for physical device (ARM64)
./gradlew composeApp:linkDebugFrameworkIosArm64
```

### Open in Xcode

```bash
# Open the iOS project in Xcode
open iosApp/iosApp.xcodeproj
```

If the Xcode project doesn't exist, create it using the KMP Xcode plugin or manually configure:

1. Open Xcode
2. Create a new iOS App project in `iosApp/` directory
3. Add the framework dependency from `composeApp/build/bin/`

### Run on Simulator

```bash
# Using Gradle (builds and runs)
./gradlew composeApp:iosSimulatorArm64Test

# Or from Xcode:
# 1. Select target device (e.g., iPhone 15 Pro)
# 2. Click Run (▶️)
```

### Build for Release

```bash
# Release framework for device
./gradlew composeApp:linkReleaseFrameworkIosArm64
```

### Troubleshooting iOS

If you encounter framework linking issues:

```bash
# Clean and rebuild
./gradlew clean
./gradlew composeApp:linkDebugFrameworkIosSimulatorArm64

# Verify framework was created
ls -la composeApp/build/bin/iosSimulatorArm64/debugFramework/
```

---

## Web (WebAssembly)

### Development Server

```bash
# Start development server with hot reload
./gradlew composeApp:wasmJsBrowserDevelopmentRun
```

Open your browser at: **http://localhost:8080**

The development server supports hot reload - changes to Kotlin code will automatically rebuild and refresh the browser.

### Production Build

```bash
# Build optimized production bundle
./gradlew composeApp:wasmJsBrowserProductionWebpack
```

**Output location:** `composeApp/build/dist/wasmJs/productionExecutable/`

### Serve Production Build Locally

```bash
# Using Python 3
cd composeApp/build/dist/wasmJs/productionExecutable/
python3 -m http.server 8080

# Using Node.js (npx)
npx serve composeApp/build/dist/wasmJs/productionExecutable/
```

### Deploy to Static Hosting

The production build generates static files that can be deployed to any static hosting service:

- **Vercel**: Drop the `productionExecutable` folder
- **Netlify**: Set build command and publish directory
- **GitHub Pages**: Copy files to `docs/` or `gh-pages` branch
- **AWS S3 + CloudFront**: Upload to S3 bucket with CloudFront distribution

**Required files for deployment:**
- `index.html`
- `kinderhubApp.js`
- `kinderhubApp.wasm`
- `*.js` (supporting scripts)

---

## All Platforms - Common Commands

```bash
# Clean build artifacts
./gradlew clean

# Check for dependency updates
./gradlew dependencyUpdates

# Run all checks
./gradlew check

# View all available tasks
./gradlew tasks

# Build all targets
./gradlew build
```

---

## Project Structure

```
kinderhub-ui/
├── composeApp/
│   ├── src/
│   │   ├── commonMain/          # Shared Kotlin code
│   │   │   └── kotlin/com/kinderhub/ui/
│   │   │       ├── components/  # Reusable UI components
│   │   │       ├── data/        # Models & repositories
│   │   │       ├── di/          # Koin modules
│   │   │       ├── navigation/  # Navigation routes
│   │   │       ├── screens/     # Screen composables
│   │   │       └── theme/       # Colors, typography, spacing
│   │   ├── androidMain/         # Android-specific code
│   │   ├── iosMain/             # iOS-specific code
│   │   └── wasmJsMain/          # Web-specific code
│   └── build.gradle.kts
├── iosApp/                      # iOS Xcode project
├── gradle/
│   └── libs.versions.toml       # Version catalog
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

---

## Responsive Layout

The app uses an adaptive layout system:

| Screen Width | Layout Mode | Navigation |
|-------------|-------------|------------|
| ≥1024dp | Desktop | Collapsible sidebar (240dp/76dp) |
| <768dp | Mobile | Bottom navigation bar |

---

## Theming

Four color palettes are available:

1. **Dusky Rose** (default) - Warm, sophisticated pink tones
2. **Warm English** - Traditional warm neutrals
3. **Heritage** - Classic heritage colors
4. **Trust Blue** - Professional blue palette

Change theme in code:
```kotlin
KinderHubTheme(palette = Palette.DuskyRose) {
    // App content
}
```

---

## Troubleshooting

### Gradle Issues

```bash
# Clear Gradle cache
./gradlew --stop
rm -rf ~/.gradle/caches/
./gradlew clean build
```

### Android Emulator Not Starting

```bash
# Check emulator availability
emulator -list-avds

# Create new AVD via Android Studio:
# Tools → Device Manager → Create Device
```

### iOS Simulator Issues

```bash
# List available simulators
xcrun simctl list devices

# Boot a specific simulator
xcrun simctl boot "iPhone 15 Pro"

# Open Simulator app
open -a Simulator
```

### WASM Build Failures

```bash
# Ensure Kotlin/WASM experimental features are enabled
# Check gradle.properties contains:
org.jetbrains.compose.experimental.wasm.enabled=true
```

---

## Environment Variables

For production deployments, configure these environment variables:

| Variable | Description | Default |
|----------|-------------|---------|
| `AUTH0_DOMAIN` | Auth0 tenant domain | - |
| `AUTH0_CLIENT_ID` | Auth0 client ID | - |
| `API_BASE_URL` | Backend API URL | - |

---

## License

Proprietary - KinderHub Ltd. All rights reserved.
