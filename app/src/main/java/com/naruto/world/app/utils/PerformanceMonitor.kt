package com.naruto.world.app.utils

import android.util.Log
import kotlin.system.measureTimeMillis

/**
 * Performance monitoring utility for tracking app performance metrics
 */
object PerformanceMonitor {

    private const val TAG = "PerformanceMonitor"

    /**
     * Measures execution time of a suspend function
     */
    suspend fun <T> measureTime(
        operationName: String,
        operation: suspend () -> T
    ): T {
        val startTime = System.currentTimeMillis()
        return try {
            val result = operation()
            val executionTime = System.currentTimeMillis() - startTime
            Log.d(TAG, "$operationName completed in ${executionTime}ms")
            result
        } catch (e: Exception) {
            val executionTime = System.currentTimeMillis() - startTime
            Log.e(TAG, "$operationName failed after ${executionTime}ms: ${e.message}")
            throw e
        }
    }

    /**
     * Measures execution time of a regular function
     */
    fun <T> measureTimeSync(
        operationName: String,
        operation: () -> T
    ): T {
        val startTime = System.currentTimeMillis()
        val result = operation()
        val executionTime = System.currentTimeMillis() - startTime
        Log.d(TAG, "$operationName completed in ${executionTime}ms")
        return result
    }

    /**
     * Logs performance metrics for database operations
     */
    fun logDatabaseOperation(
        operation: String,
        recordCount: Int,
        executionTimeMs: Long
    ) {
        val avgTimePerRecord = if (recordCount > 0) executionTimeMs / recordCount else 0
        Log.d(TAG, "DB $operation: ${recordCount} records in ${executionTimeMs}ms (${avgTimePerRecord}ms/record)")
    }

    /**
     * Logs performance metrics for network operations
     */
    fun logNetworkOperation(
        operation: String,
        responseSize: Int,
        executionTimeMs: Long,
        cacheHit: Boolean = false
    ) {
        val cacheStatus = if (cacheHit) "CACHE" else "NETWORK"
        Log.d(TAG, "NET $operation [$cacheStatus]: ${responseSize} bytes in ${executionTimeMs}ms")
    }

    /**
     * Logs memory usage information
     */
    fun logMemoryUsage(context: String) {
        val runtime = Runtime.getRuntime()
        val usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024
        val maxMemory = runtime.maxMemory() / 1024 / 1024
        val freeMemory = runtime.freeMemory() / 1024 / 1024

        Log.d(TAG, "MEM $context: Used=${usedMemory}MB, Free=${freeMemory}MB, Max=${maxMemory}MB")
    }

    /**
     * Performance thresholds for monitoring
     */
    object Thresholds {
        const val SLOW_DB_OPERATION_MS = 100L
        const val SLOW_NETWORK_OPERATION_MS = 2000L
        const val SLOW_UI_OPERATION_MS = 16L // 60 FPS threshold
        const val HIGH_MEMORY_USAGE_MB = 100L
    }

    /**
     * Checks if operation time exceeds threshold and logs warning
     */
    fun checkPerformanceThreshold(
        operationName: String,
        executionTimeMs: Long,
        thresholdMs: Long
    ) {
        if (executionTimeMs > thresholdMs) {
            Log.w(TAG, "PERF WARNING: $operationName took ${executionTimeMs}ms (threshold: ${thresholdMs}ms)")
        }
    }
}

/**
 * Extension function for easy performance monitoring
 */
suspend fun <T> measureTime(
    operationName: String,
    block: suspend () -> T
): T = PerformanceMonitor.measureTime(operationName, block)

/**
 * Extension function for synchronous performance monitoring
 */
fun <T> measureTimeSync(
    operationName: String,
    block: () -> T
): T = PerformanceMonitor.measureTimeSync(operationName, block)