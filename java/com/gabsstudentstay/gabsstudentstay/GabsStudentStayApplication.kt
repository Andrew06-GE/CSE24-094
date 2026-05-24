package com.gabsstudentstay.gabsstudentstay

import android.app.Application
import androidx.work.Configuration

class GabsStudentStayApplication : Application(), Configuration.Provider {

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
}