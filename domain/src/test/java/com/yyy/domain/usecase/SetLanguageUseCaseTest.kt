package com.yyy.domain.usecase

import com.yyy.domain.repository.SettingsRepository
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
class SetLanguageUseCaseTest {

    @MockK
    private lateinit var mockSettingsRepository: SettingsRepository

    private lateinit var setLanguageUseCase: SetLanguageUseCase

    @BeforeEach
    fun setUp() {
        setLanguageUseCase = SetLanguageUseCase(mockSettingsRepository)
    }

    @Test
    fun `invoke - should call setLanguage on repository with correct language string`() = runTest {
        val testLanguage = "tr"
        coEvery { mockSettingsRepository.setLanguage(testLanguage) } coAnswers {}

        setLanguageUseCase(testLanguage)

        coVerify(exactly = 1) { mockSettingsRepository.setLanguage(testLanguage) }
    }

    @Test
    fun `invoke - when repository setLanguage throws exception - use case should propagate exception`() =
        runTest {
            val testLanguage = "en"
            val expectedException = RuntimeException("Failed to save language setting")
            coEvery { mockSettingsRepository.setLanguage(testLanguage) } throws expectedException

            var thrownException: Throwable? = null
            try {
                setLanguageUseCase(testLanguage)
            } catch (e: Throwable) {
                thrownException = e
            }
            assertEquals(expectedException, thrownException)
            coVerify(exactly = 1) { mockSettingsRepository.setLanguage(testLanguage) }
        }
}