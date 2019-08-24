package project.logic

import android.app.Application

class G :Application() {
    companion object{
        val handler=android.os.Handler()
    }
    override fun onCreate() {
        super.onCreate()
    }
}