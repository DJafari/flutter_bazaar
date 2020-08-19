package ir.davoood.flutter_bazaar

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import com.farsitel.bazaar.ILoginCheckService

class LoginServiceConnection(var result: BgResult, var context: Context) : ServiceConnection {
  lateinit var service : ILoginCheckService

  override fun onServiceConnected(name: ComponentName?, boundService: IBinder?) {
    service = ILoginCheckService.Stub.asInterface(boundService)
    try {
      val isLoggedIn = service.isLoggedIn
      result.success(isLoggedIn)
    } catch (e: Exception) {
      result.error("isLoggedIn", e.message, e)
    }
    context.unbindService(this)
  }

  override fun onServiceDisconnected(name: ComponentName?) {

  }

}