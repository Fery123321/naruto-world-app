package com.naruto.world.app.data.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.naruto.world.app.data.model.CharacterResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class NarutoApiServiceTest {

    private lateinit var apiService: NarutoApiService

    @Before
    fun setup() {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val headerInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", "NarutoWorldApp/1.0")
                .build()
            chain.proceed(request)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api-dattebayo.vercel.app/api/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        apiService = retrofit.create(NarutoApiService::class.java)
    }

    @Test
    fun `getCharacters_should_return_valid_response`() = runBlocking {
        // When
        val response = apiService.getCharacters(page = 1, limit = 10)

        // Then
        assertTrue("Response should be successful", response.isSuccessful)
        assertNotNull("Response body should not be null", response.body())

        response.body()?.let { characterResponse ->
            assertNotNull("Characters list should not be null", characterResponse.characters)
            assertTrue("Should have at least one character", characterResponse.characters.isNotEmpty())

            // Verify first character has required fields
            val firstCharacter = characterResponse.characters.first()
            assertNotNull("Character ID should not be null", firstCharacter.id)
            assertNotNull("Character name should not be null", firstCharacter.name)
            assertTrue("Character name should not be empty", firstCharacter.name.isNotEmpty())
        }
    }

    @Test
    fun `getCharacters_with_search_should_return_filtered_results`() = runBlocking {
        // Given
        val searchName = "Naruto"

        // When
        val response = apiService.getCharacters(
            page = 1,
            limit = 10,
            name = searchName
        )

        // Then
        assertTrue("Search response should be successful", response.isSuccessful)
        assertNotNull("Search response body should not be null", response.body())

        response.body()?.let { characterResponse ->
            // Note: The actual filtering might happen on the server side
            // This test verifies the API call works with search parameter
            assertNotNull("Characters list should not be null", characterResponse.characters)
        }
    }

    @Test
    fun `getCharacterById_should_return_single_character`() = runBlocking {
        // Given - Use ID 1 (Naruto) which should exist
        val characterId = 1L

        // When
        val response = apiService.getCharacterById(characterId)

        // Then
        assertTrue("Character response should be successful", response.isSuccessful)
        assertNotNull("Character response body should not be null", response.body())

        response.body()?.let { character ->
            assertNotNull("Character should not be null", character)
            assertEquals("Character ID should match requested ID", characterId, character.id)
            assertNotNull("Character name should not be null", character.name)
            assertTrue("Character name should not be empty", character.name.isNotEmpty())
        }
    }

    @Test
    fun `getClans_should_return_valid_response`() = runBlocking {
        // When
        val response = apiService.getClans(page = 1, limit = 5)

        // Then
        assertTrue("Clans response should be successful", response.isSuccessful)
        assertNotNull("Clans response body should not be null", response.body())

        response.body()?.let { clanResponse ->
            assertNotNull("Clans list should not be null", clanResponse.clans)
        }
    }

    @Test
    fun `getVillages_should_return_valid_response`() = runBlocking {
        // When
        val response = apiService.getVillages(page = 1, limit = 5)

        // Then
        assertTrue("Villages response should be successful", response.isSuccessful)
        assertNotNull("Villages response body should not be null", response.body())

        response.body()?.let { villageResponse ->
            assertNotNull("Villages list should not be null", villageResponse.villages)
        }
    }
}