package com.basiatish.biatestapp

import android.app.Application
import com.basiatish.biatestapp.di.AppComponent
import com.basiatish.biatestapp.di.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(applicationContext)
    }

}