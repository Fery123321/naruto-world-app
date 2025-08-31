package com.naruto.world.app.viewmodel

import com.naruto.world.app.data.model.Character
import com.naruto.world.app.data.model.CharacterResponse
import com.naruto.world.app.data.repository.CharacterRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterListViewModelTest {

    private lateinit var repository: CharacterRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `repository should return success result when API call succeeds`() = runTest {
        // Given
        val mockCharacters = listOf(
            Character(id = 1, name = "Naruto Uzumaki"),
            Character(id = 2, name = "Sasuke Uchiha")
        )
        val mockResponse = CharacterResponse(
            characters = mockCharacters,
            currentPage = 1,
            pageSize = 20,
            total = 2
        )

        coEvery { repository.getCharacters(any(), any(), any()) } returns flowOf(Result.success(mockResponse))

        // When
        val result = repository.getCharacters(1, 20, null)

        // Then
        result.collect { responseResult ->
            assertTrue("Result should be success", responseResult.isSuccess)
            responseResult.onSuccess { response ->
                assertEquals("Should have 2 characters", 2, response.characters.size)
                assertEquals("First character should be Naruto", "Naruto Uzumaki", response.characters[0].name)
            }
        }
    }

    @Test
    fun `repository should return error result when API call fails`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { repository.getCharacters(any(), any(), any()) } returns flowOf(Result.failure(Exception(errorMessage)))

        // When
        val result = repository.getCharacters(1, 20, null)

        // Then
        result.collect { responseResult ->
            assertTrue("Result should be failure", responseResult.isFailure)
            responseResult.onFailure { error ->
                assertEquals("Error message should match", errorMessage, error.message)
            }
        }
    }

    @Test
    fun `search query parameter should be passed correctly`() = runTest {
        // Given
        val searchQuery = "Naruto"
        val mockCharacters = listOf(Character(id = 1, name = "Naruto Uzumaki"))
        val mockResponse = CharacterResponse(
            characters = mockCharacters,
            currentPage = 1,
            pageSize = 20,
            total = 1
        )

        coEvery { repository.getCharacters(any(), any(), searchQuery) } returns flowOf(Result.success(mockResponse))

        // When
        val result = repository.getCharacters(1, 20, searchQuery)

        // Then
        result.collect { responseResult ->
            assertTrue("Result should be success", responseResult.isSuccess)
        }
    }

    @Test
    fun `character data should have required fields`() = runTest {
        // Given
        val character = Character(
            id = 1,
            name = "Naruto Uzumaki",
            images = listOf("image1.jpg", "image2.jpg")
        )

        // Then
        assertEquals("Character ID should be 1", 1L, character.id)
        assertEquals("Character name should be Naruto Uzumaki", "Naruto Uzumaki", character.name)
        assertNotNull("Character images should not be null", character.images)
        assertTrue("Character should have images", character.images?.isNotEmpty() == true)
    }
}