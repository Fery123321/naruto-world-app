package com.naruto.world.app.di

import androidx.room.Room
import com.naruto.world.app.data.api.NarutoApiService
import com.naruto.world.app.data.local.database.NarutoDatabase
import com.naruto.world.app.data.local.datasource.CharacterLocalDataSource
import com.naruto.world.app.data.repository.CharacterRepository
import com.naruto.world.app.viewmodel.CharacterDetailViewModel
import com.naruto.world.app.viewmodel.CharacterListViewModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://dattebayo-api.onrender.com/"

/**
 * Custom JsonAdapter.Factory that makes JSON parsing lenient
 */
class LenientJsonAdapterFactory : JsonAdapter.Factory {
    override fun create(type: Type, annotations: Set<Annotation>, moshi: Moshi): JsonAdapter<*>? {
        val delegate = moshi.nextAdapter<Any>(this, type, annotations)
        return object : JsonAdapter<Any>() {
            override fun fromJson(reader: JsonReader): Any? {
                return try {
                    reader.isLenient = true
                    delegate.fromJson(reader)
                } catch (e: Exception) {
                    // If lenient parsing fails, try to skip the problematic content
                    reader.skipValue()
                    null
                }
            }

            override fun toJson(writer: JsonWriter, value: Any?) {
                delegate.toJson(writer, value)
            }
        }
    }
}

val networkModule = module {

    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("User-Agent", "NarutoWorldApp/1.0")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single {
        Moshi.Builder()
            .add(LenientJsonAdapterFactory())
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single {
        val moshi = get<Moshi>()
        MoshiConverterFactory.create(moshi)
    }

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
    }

    single {
        get<Retrofit>().create(NarutoApiService::class.java)
    }

    // Database
    single {
        Room.databaseBuilder(
            androidContext(),
            NarutoDatabase::class.java,
            NarutoDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    // DAOs
    single { get<NarutoDatabase>().characterDao() }

    // Data Sources
    single { CharacterLocalDataSource(get()) }

    // Repositories
    single { CharacterRepository() }

    // ViewModels
    viewModel { CharacterListViewModel() }
    viewModel { (characterId: Long) -> CharacterDetailViewModel(characterId) }
}