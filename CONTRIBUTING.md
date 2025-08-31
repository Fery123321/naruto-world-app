# 🤝 Contributing to Naruto World Android App

Thank you for your interest in contributing to the Naruto World Android App! We welcome contributions from developers of all skill levels. This document provides guidelines and information to help you contribute effectively.

## 📋 Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Development Setup](#development-setup)
- [Project Structure](#project-structure)
- [Contributing Guidelines](#contributing-guidelines)
- [Testing](#testing)
- [Pull Request Process](#pull-request-process)
- [Issue Reporting](#issue-reporting)
- [Documentation](#documentation)

## 🤟 Code of Conduct

This project follows a code of conduct to ensure a welcoming environment for all contributors. By participating, you agree to:

- **Be respectful** and inclusive in all interactions
- **Be collaborative** and help fellow contributors
- **Be patient** with newcomers and different skill levels
- **Be constructive** in feedback and code reviews
- **Follow the guidelines** outlined in this document

## 🚀 Getting Started

### Prerequisites

Before you begin, ensure you have:

- **Android Studio**: Arctic Fox (2020.3.1) or later
- **JDK**: Version 11 or later
- **Git**: Version control system
- **Android SDK**: API level 24+ (Android 7.0+)

### Quick Start

1. **Fork the repository** on GitHub
2. **Clone your fork** locally:
   ```bash
   git clone https://github.com/your-username/naruto-world-app.git
   cd naruto-world-app
   ```
3. **Set up the project** in Android Studio
4. **Create a feature branch**:
   ```bash
   git checkout -b feature/your-feature-name
   ```
5. **Make your changes** and test thoroughly
6. **Submit a pull request**

## 🛠️ Development Setup

### 1. Environment Configuration

1. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the cloned directory

2. **Configure SDK**
   - Ensure Android SDK 24+ is installed
   - Set JDK 11 as the project JDK

3. **Sync Project**
   - Let Android Studio sync all dependencies
   - Build the project to ensure everything works

### 2. API Configuration

The app uses the Naruto API (https://api-dattebayo.vercel.app). No additional configuration is needed for basic development.

### 3. Testing Setup

```bash
# Run unit tests
./gradlew testDebugUnitTest

# Run integration tests (requires device/emulator)
./gradlew connectedDebugAndroidTest

# Run all tests
./gradlew test
```

## 🏗️ Project Structure

```
com.naruto.world.app/
├── data/
│   ├── api/          # API interfaces and services
│   ├── model/        # Data models and DTOs
│   ├── repository/   # Repository implementations
│   └── local/        # Local database (future)
├── di/               # Dependency injection modules
├── ui/
│   ├── components/   # Reusable UI components
│   ├── screens/      # Screen composables
│   ├── navigation/   # Navigation setup
│   └── theme/        # App theming
├── utils/            # Utility classes
└── viewmodel/        # ViewModels
```

### Key Files

- `MainActivity.kt`: App entry point
- `NarutoApplication.kt`: Application class with Koin setup
- `NetworkModule.kt`: Dependency injection configuration
- `NarutoApiService.kt`: API service interface
- `CharacterRepository.kt`: Data repository

## 📝 Contributing Guidelines

### Code Style

#### Kotlin Style
- Follow the [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use 4 spaces for indentation
- Maximum line length: 120 characters
- Use meaningful variable and function names

#### Compose Style
- Follow [Compose Best Practices](https://developer.android.com/jetpack/compose/performance/bestpractices)
- Use descriptive parameter names
- Prefer `Modifier` chaining over nested layouts
- Use `remember` appropriately for performance

#### Example:
```kotlin
@Composable
fun CharacterCard(
    character: Character,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        // Implementation
    }
}
```

### Commit Messages

Use clear, descriptive commit messages:

```
feat: add character search functionality
fix: resolve crash on empty character list
docs: update API documentation
style: format code according to Kotlin conventions
test: add unit tests for CharacterRepository
```

### Branch Naming

Use descriptive branch names:

```
feature/add-character-search
bugfix/fix-crash-on-empty-list
hotfix/urgent-api-fix
docs/update-readme
```

## 🧪 Testing

### Unit Tests

Write unit tests for:

- **ViewModels**: State management and business logic
- **Repositories**: Data fetching and error handling
- **Utilities**: Helper functions and extensions
- **Models**: Data validation and transformation

```kotlin
@Test
fun `should return success when API call succeeds`() = runTest {
    // Given
    val mockResponse = CharacterResponse(...)
    coEvery { apiService.getCharacters() } returns Response.success(mockResponse)

    // When
    val result = repository.getCharacters()

    // Then
    result.collect { responseResult ->
        assertTrue(responseResult.isSuccess)
        // Additional assertions
    }
}
```

### Integration Tests

Test real API interactions:

```kotlin
@Test
fun `should fetch characters from real API`() = runBlocking {
    // Given
    val apiService = createNarutoApiService()

    // When
    val response = apiService.getCharacters()

    // Then
    assertTrue(response.isSuccessful)
    assertNotNull(response.body())
}
```

### UI Tests (Future)

```kotlin
@Test
fun `should display character list correctly`() {
    // Compose UI testing
    composeTestRule.setContent {
        CharacterListScreen(...)
    }

    composeTestRule.onNodeWithText("Naruto").assertIsDisplayed()
}
```

## 🔄 Pull Request Process

### 1. Before Submitting

- ✅ **Test your changes** thoroughly
- ✅ **Update documentation** if needed
- ✅ **Add tests** for new features
- ✅ **Follow code style** guidelines
- ✅ **Resolve all warnings** and errors

### 2. Pull Request Template

Use this template for your PR:

```markdown
## Description
Brief description of the changes made.

## Type of Change
- [ ] Bug fix (non-breaking change)
- [ ] New feature (non-breaking change)
- [ ] Breaking change
- [ ] Documentation update
- [ ] Code style update

## Testing
- [ ] Unit tests added/updated
- [ ] Integration tests added/updated
- [ ] Manual testing completed
- [ ] All tests pass

## Screenshots (if applicable)
Add screenshots of UI changes.

## Checklist
- [ ] My code follows the project's style guidelines
- [ ] I have performed a self-review of my own code
- [ ] I have commented my code, particularly in hard-to-understand areas
- [ ] I have made corresponding changes to the documentation
- [ ] My changes generate no new warnings
- [ ] I have added tests that prove my fix is effective or that my feature works
```

### 3. Review Process

1. **Automated Checks**: CI/CD pipeline runs tests
2. **Code Review**: At least one maintainer reviews
3. **Feedback**: Address any requested changes
4. **Approval**: PR approved and merged

## 🐛 Issue Reporting

### Bug Reports

Use the bug report template:

```markdown
**Describe the bug**
A clear description of what the bug is.

**To Reproduce**
Steps to reproduce the behavior:
1. Go to '...'
2. Click on '....'
3. Scroll down to '....'
4. See error

**Expected behavior**
A clear description of what you expected to happen.

**Screenshots**
If applicable, add screenshots to help explain your problem.

**Environment:**
- Device: [e.g., Pixel 5]
- OS: [e.g., Android 13]
- App Version: [e.g., 1.0.0]

**Additional context**
Add any other context about the problem here.
```

### Feature Requests

Use the feature request template:

```markdown
**Is your feature request related to a problem?**
A clear description of what the problem is.

**Describe the solution you'd like**
A clear description of what you want to happen.

**Describe alternatives you've considered**
A clear description of any alternative solutions.

**Additional context**
Add any other context or screenshots about the feature request here.
```

## 📚 Documentation

### Code Documentation

Add KDoc comments for:

- **Public functions** and classes
- **Complex logic** that needs explanation
- **API endpoints** and their usage
- **Data models** and their purpose

```kotlin
/**
 * Fetches characters from the Naruto API with optional pagination.
 *
 * @param page The page number to fetch (1-based)
 * @param limit The number of characters per page
 * @return Flow of Result containing CharacterResponse
 */
fun getCharacters(page: Int? = null, limit: Int? = null): Flow<Result<CharacterResponse>>
```

### README Updates

Update the README.md for:

- **New features** added
- **API changes** made
- **Setup instructions** modified
- **Screenshots** updated

## 🎯 Areas for Contribution

### High Priority
- [ ] **Offline Caching**: Room database integration
- [ ] **Favorites System**: Save favorite characters
- [ ] **Advanced Search**: Multi-criteria filtering
- [ ] **UI Polish**: Animations and transitions

### Medium Priority
- [ ] **Dark Mode Toggle**: Manual theme switching
- [ ] **Push Notifications**: New content updates
- [ ] **Multi-language Support**: Localization
- [ ] **Performance Monitoring**: Analytics integration

### Future Enhancements
- [ ] **Character Comparison**: Side-by-side comparison
- [ ] **Quiz Mode**: Test Naruto knowledge
- [ ] **Social Features**: Share character info
- [ ] **Widgets**: Home screen widgets

## 💡 Tips for Contributors

### Getting Help
- **Check existing issues** before creating new ones
- **Search the documentation** for answers
- **Ask in discussions** for clarification
- **Review open PRs** to understand current work

### Best Practices
- **Start small**: Begin with simple bug fixes or documentation
- **Test thoroughly**: Ensure your changes don't break existing functionality
- **Follow conventions**: Maintain consistency with existing code
- **Communicate**: Keep stakeholders informed of your progress

### Development Workflow
1. **Choose an issue** or create one
2. **Discuss approach** if needed
3. **Create a branch** for your work
4. **Write tests first** (TDD approach)
5. **Implement the feature** or fix
6. **Test manually** and run automated tests
7. **Update documentation** as needed
8. **Submit a PR** with clear description

## 🙏 Recognition

Contributors will be:
- **Acknowledged** in release notes
- **Listed** in CONTRIBUTORS.md
- **Featured** in the app's about section
- **Invited** to become maintainers

## 📞 Contact

- **Issues**: GitHub Issues
- **Discussions**: GitHub Discussions
- **Email**: [your-email@example.com]

---

**Thank you for contributing to Naruto World! 🌀**

*Together, we're building something amazing for Naruto fans worldwide.*