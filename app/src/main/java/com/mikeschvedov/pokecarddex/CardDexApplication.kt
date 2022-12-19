package com.mikeschvedov.pokecarddex

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CardDexApplication : Application() {

    override fun onCreate() {
        super.onCreate()

    }
}