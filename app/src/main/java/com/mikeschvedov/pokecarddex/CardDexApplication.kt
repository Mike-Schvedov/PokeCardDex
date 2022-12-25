package com.mikeschvedov.pokecarddex

import android.app.Application
import com.mikeschvedov.ultimate_utility_box.logger.LoggerOptions
import com.mikeschvedov.ultimate_utility_box.logger.LoggerService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CardDexApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        LoggerService.initialize("",LoggerOptions.LogLevel.VERBOSE)
    }
}