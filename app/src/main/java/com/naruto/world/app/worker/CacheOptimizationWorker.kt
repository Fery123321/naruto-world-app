package com.naruto.world.app.worker

import android.content.Context
import android.util.Log
import androidx.work.*
import com.naruto.world.app.data.repository.CharacterRepository
import com.naruto.world.app.data.repository.ClanRepository
import com.naruto.world.app.utils.PerformanceMonitor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

/**
 * WorkManager worker for background cache optimization
 */
class CacheOptimizationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KoinComponent {

    private val characterRepository: CharacterRepository by inject()
    private val clanRepository: ClanRepository by inject()

    companion object {
        private const val TAG = "CacheOptimizationWorker"
        const val WORK_NAME = "cache_optimization_work"

        fun schedulePeriodicWork(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED) // Can run without network
                .setRequiresBatteryNotLow(true) // Only when battery is not low
                .setRequiresDeviceIdle(true) // Only when device is idle
                .build()

            val workRequest = PeriodicWorkRequestBuilder<CacheOptimizationWorker>(
                repeatInterval = 6, // Every 6 hours
                repeatIntervalTimeUnit = TimeUnit.HOURS
            )
                .setConstraints(constraints)
                .setInitialDelay(1, TimeUnit.HOURS) // Start after 1 hour
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP, // Keep existing if already scheduled
                workRequest
            )

            Log.d(TAG, "Cache optimization work scheduled")
        }
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Starting cache optimization work")

            PerformanceMonitor.logMemoryUsage("Before cache optimization")

            // Optimize character cache
            val characterCacheSize = characterRepository.getCacheSize()
            Log.d(TAG, "Character cache size before optimization: $characterCacheSize")

            // Optimize clan cache
            val clanCacheSize = clanRepository.getCacheSize()
            Log.d(TAG, "Clan cache size before optimization: $clanCacheSize")

            // Perform cache optimization
            val characterOptimizationTime = kotlin.system.measureTimeMillis {
                characterRepository.optimizeCache()
            }

            val clanOptimizationTime = kotlin.system.measureTimeMillis {
                clanRepository.optimizeCache()
            }

            // Log optimization results
            val newCharacterCacheSize = characterRepository.getCacheSize()
            val newClanCacheSize = clanRepository.getCacheSize()

            Log.d(TAG, "Cache optimization completed:")
            Log.d(TAG, "  Characters: $characterCacheSize -> $newCharacterCacheSize (${characterOptimizationTime}ms)")
            Log.d(TAG, "  Clans: $clanCacheSize -> $newClanCacheSize (${clanOptimizationTime}ms)")

            PerformanceMonitor.logMemoryUsage("After cache optimization")

            // Report success with optimization metrics
            val outputData = Data.Builder()
                .putInt("characters_optimized", characterCacheSize - newCharacterCacheSize)
                .putInt("clans_optimized", clanCacheSize - newClanCacheSize)
                .putLong("optimization_time", characterOptimizationTime + clanOptimizationTime)
                .build()

            Result.success(outputData)

        } catch (e: Exception) {
            Log.e(TAG, "Error during cache optimization: ${e.message}", e)
            Result.failure()
        }
    }
}