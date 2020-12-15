package io.github.gusandrianos.foxforreddit

import android.app.Application
import com.jaredrummler.cyanea.Cyanea
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FoxForRedditApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Cyanea.init(this, resources)
    }
}