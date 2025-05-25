# OMDB Compose MM

A modern movie search and favorite listing application using OMDB API, built with modern Android
development practices.

## 🚀 Getting Started

1. Clone the project

```bash
git clone https://github.com/yourusername/omdbComposeMm.git
```

2. Add your API key

```kotlin
// local.properties
omdbApiKey=your_api_key_here
```

## 🏗️ Project Structure

The project is designed with a modular architecture following Clean Architecture principles:

```
app/
├── domain/           # Domain layer
│   ├── model/       # Domain models
│   ├── repository/  # Repository interfaces
│   └── usecase/     # Use cases
│
├── data/            # Data layer
│   ├── api/         # API services
│   ├── db/          # Room database
│   ├── repository/  # Repository implementations
│   └── source/      # Data sources
│
├── ui/              # UI layer
│   ├── components/  # Composable components
│   ├── screen/      # Screens
│   ├── theme/       # UI theme
│   └── viewmodel/   # ViewModels
│
└── theme/           # Shared UI theme module
```

## 🛠️ Technologies and Libraries

### Core

- Kotlin 2.0.21
- Coroutines 1.9.0
- Flow
- Hilt 2.56.2 (Dependency Injection)
- KSP 2.0.21-1.0.27

### UI

- Jetpack Compose 2025.05.01
- Material3
- Navigation Compose 2.9.0
- Coil 2.5.0 (Image loading)
- Compose Material Icons Extended 1.6.7

### Network

- Retrofit 3.0.0
- OkHttp 4.12.0
- Moshi 1.15.0 (JSON parsing)
- Kotlinx Serialization 1.8.0

### Database

- Room 2.7.1
- DataStore 1.1.7 (Preferences)

### Testing

- JUnit 4.13.2
- Mockk 1.13.8
- Turbine 1.0.0 (Flow testing)
- Coroutines Test 1.7.3
- Hilt Testing 2.48

## 📱 Features

- Movie search (OMDB API)
- Save favorite movies
- Create custom favorite lists
- Move movies between lists
- View movie details
- Theme support (Light/Dark/System)
- Language support (English/Turkish)
- Search history
- Filtering and sorting

## 🏗️ Modular Structure

### Domain Module

- Use cases
- Domain models
- Repository interfaces
- No dependencies

### Data Module

- API implementation
- Database implementation
- Repository implementations
- Depends on Domain module

### UI Module

- Compose UI
- ViewModels
- Navigation
- Depends on Domain and Data modules

### Theme Module

- Shared UI theme
- Material3
- Custom colors and typography
- No dependencies

## 🧪 Testing Strategy

- Unit Tests (JUnit, Mockk, Turbine)
- ViewModel Tests
- Use Case Tests
