package com.naruto.world.app

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.naruto.world.app.di.networkModule
import com.naruto.world.app.worker.CacheOptimizationWorker
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NarutoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Koin
        startKoin {
            androidContext(this@NarutoApplication)
            modules(networkModule)
        }

        // Initialize WorkManager
        val workManagerConfig = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
        WorkManager.initialize(this, workManagerConfig)

        // Schedule background cache optimization
        CacheOptimizationWorker.schedulePeriodicWork(this)
    }
}