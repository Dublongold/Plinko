package one.two.plinko.app

import android.app.Application
import android.util.Log
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        val tag = "AppsFlyer TEST"
        AppsFlyerLib.getInstance().run {
            init("2KTn2FFy4YwGYoQEgmsDCg", object: AppsFlyerConversionListener {

                override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
                    Log.i(tag, p0.toString())
                }
                override fun onConversionDataFail(p0: String?) {
                    Log.w(tag, p0.toString())
                }
                ///////////////////////////////////////////////////////////////////
                override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
                    TODO("Not yet implemented")
                }

                override fun onAttributionFailure(p0: String?) {
                    TODO("Not yet implemented")
                }
            }, this@App)
            start(this@App)
        }
    }
}