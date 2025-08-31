package com.naruto.world.app

import android.app.Application
import com.naruto.world.app.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NarutoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NarutoApplication)
            modules(networkModule)
        }
    }
}