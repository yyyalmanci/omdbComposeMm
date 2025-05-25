package com.yyy.domain.usecase

import com.yyy.domain.repository.SettingsRepository
import com.yyy.theme.ThemeOption
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class SetThemeUseCaseTest {

    @MockK
    private lateinit var mockSettingsRepository: SettingsRepository

    private lateinit var setThemeUseCase: SetThemeUseCase

    @BeforeEach
    fun setUp() {
        setThemeUseCase = SetThemeUseCase(mockSettingsRepository)
    }

    @Test
    fun `invoke - should call setTheme on repository with correct theme option`() = runTest {
        val testTheme = ThemeOption.DARK
        coEvery { mockSettingsRepository.setTheme(testTheme) } coAnswers {}

        setThemeUseCase(testTheme)

        coVerify(exactly = 1) { mockSettingsRepository.setTheme(testTheme) }
    }

    @Test
    fun `invoke - when repository setTheme throws exception - use case should propagate exception`() =
        runTest {
            val testTheme = ThemeOption.LIGHT
            val expectedException = RuntimeException("Failed to save theme setting")
            coEvery { mockSettingsRepository.setTheme(testTheme) } throws expectedException

            var thrownException: Throwable? = null

            try {
                setThemeUseCase(testTheme)
            } catch (e: Throwable) {
                thrownException = e
            }

            assertEquals(expectedException, thrownException)
            coVerify(exactly = 1) { mockSettingsRepository.setTheme(testTheme) }
        }
}