package io.github.gusandrianos.foxforreddit

import android.app.Application
import com.jaredrummler.cyanea.Cyanea

class FoxForRedditApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Cyanea.init(this, resources)
    }
}