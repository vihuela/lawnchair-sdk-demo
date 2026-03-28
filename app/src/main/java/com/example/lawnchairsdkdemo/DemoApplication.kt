package com.example.lawnchairsdkdemo

import android.app.Application
import app.lawnchair.sdk.LawnchairSdkHost

class DemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LawnchairSdkHost.initialize(this)
    }
}
