package com.app.msm

import android.app.Application
import com.app.msm.api.FirebaseProvider
import com.app.msm.helper.Helper
import com.google.firebase.database.Logger
import timber.log.Timber

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initFirebase()
        initTimber()
    }

    private fun initTimber() {
        if (Helper.isDebugBuild()) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initFirebase() {
        FirebaseProvider.firebaseDatabase.apply {
            setPersistenceEnabled(true)
            if (Helper.isDebugBuild()) {
                setLogLevel(Logger.Level.DEBUG) }
        }
    }
}
