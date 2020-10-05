package com.sostrovsky.onlineshop

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.sostrovsky.onlineshop.network.connection.NetworkConnection
import timber.log.Timber

class OnlineShopApp : MultiDexApplication() {
    init {
        instance = this
    }

    companion object {
        private var instance: OnlineShopApp? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        NetworkConnection.create()
    }
}