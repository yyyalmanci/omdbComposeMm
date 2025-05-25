package com.yyy.domain.usecase

import app.cash.turbine.test
import com.yyy.domain.repository.SettingsRepository
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
class GetLanguageUseCaseTest {

    @MockK
    private lateinit var mockSettingsRepository: SettingsRepository

    private lateinit var getLanguageUseCase: GetLanguageUseCase

    @BeforeEach
    fun setUp() {
        getLanguageUseCase = GetLanguageUseCase(mockSettingsRepository)
    }

    @Test
    fun `invoke - when repository's language flow emits a language string - should return the same language string`() =
        runTest {
            val expectedLanguage = "tr"
            every { mockSettingsRepository.language } returns flowOf(expectedLanguage)

            val resultFlow = getLanguageUseCase()

            resultFlow.test {
                val emittedLanguage = awaitItem()
                assertEquals(expectedLanguage, emittedLanguage)
                awaitComplete()
            }

            verify(exactly = 1) { mockSettingsRepository.language }
        }

    @Test
    fun `invoke - when repository's language flow is empty - should return an empty flow`() =
        runTest {
            every { mockSettingsRepository.language } returns flowOf() // Boş bir flow

            val resultFlow = getLanguageUseCase()

            resultFlow.test {
                awaitComplete()
            }
            verify(exactly = 1) { mockSettingsRepository.language }
        }

    @Test
    fun `invoke - when repository's language flow emits multiple values - use case flow emits same multiple values`() =
        runTest {
            val lang1 = "en"
            val lang2 = "tr"
            every { mockSettingsRepository.language } returns flowOf(lang1, lang2)

            val resultFlow = getLanguageUseCase()

            resultFlow.test {
                assertEquals(lang1, awaitItem())
                assertEquals(lang2, awaitItem())
                awaitComplete()
            }
            verify(exactly = 1) { mockSettingsRepository.language }
        }

    @Test
    fun `invoke - when repository's language flow emits error - use case flow should also emit error`() =
        runTest {
            val expectedException = RuntimeException("Settings read error")
            every { mockSettingsRepository.language } returns flow {
                throw expectedException
            }

            val resultFlow = getLanguageUseCase()

            resultFlow.test {
                val error = awaitError()
                assertEquals(expectedException, error)
            }
            verify(exactly = 1) { mockSettingsRepository.language }
        }
}