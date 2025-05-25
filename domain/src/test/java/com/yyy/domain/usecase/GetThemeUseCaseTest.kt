package com.yyy.domain.usecase

import app.cash.turbine.test
import com.yyy.domain.repository.SettingsRepository
import com.yyy.theme.ThemeOption
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class GetThemeUseCaseTest {

    @MockK
    private lateinit var mockSettingsRepository: SettingsRepository

    private lateinit var getThemeUseCase: GetThemeUseCase

    @BeforeEach
    fun setUp() {
        getThemeUseCase = GetThemeUseCase(mockSettingsRepository)
    }

    @Test
    fun `invoke - when repository's theme flow emits a theme - should return the same theme`() =
        runTest {
            val expectedTheme = ThemeOption.DARK
            every { mockSettingsRepository.theme } returns flowOf(expectedTheme)

            val resultFlow = getThemeUseCase()

            resultFlow.test {
                val emittedTheme = awaitItem()
                assertEquals(expectedTheme, emittedTheme)
                awaitComplete()
            }
            verify(exactly = 1) { mockSettingsRepository.theme }
        }

    @Test
    fun `invoke - when repository's theme flow emits SYSTEM by default - should return SYSTEM`() =
        runTest {
            val expectedTheme = ThemeOption.SYSTEM
            every { mockSettingsRepository.theme } returns flowOf(expectedTheme)

            val resultFlow = getThemeUseCase()

            resultFlow.test {
                assertEquals(expectedTheme, awaitItem())
                awaitComplete()
            }
            verify(exactly = 1) { mockSettingsRepository.theme }
        }

    @Test
    fun `invoke - when repository's theme flow emits error - use case flow should also emit error`() =
        runTest {
            val expectedException = RuntimeException("Settings theme read error")
            every { mockSettingsRepository.theme } returns flow {
                throw expectedException
            }

            val resultFlow = getThemeUseCase()

            resultFlow.test {
                val error = awaitError()
                assertEquals(expectedException, error)
            }
            verify(exactly = 1) { mockSettingsRepository.theme }
        }
}