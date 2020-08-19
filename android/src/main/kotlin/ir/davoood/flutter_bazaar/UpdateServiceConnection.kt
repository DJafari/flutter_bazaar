package ir.davoood.flutter_bazaar

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import com.farsitel.bazaar.IUpdateCheckService

class UpdateServiceConnection(var result: BgResult, var context: Context) : ServiceConnection {
  lateinit var service : IUpdateCheckService

  override fun onServiceConnected(name: ComponentName?, boundService: IBinder?) {
    service = IUpdateCheckService.Stub.asInterface(boundService)
    try {
      val vCode = service.getVersionCode(context.packageName)
      result.success(vCode)
    } catch (e: Exception) {
      result.error("getLatestVersion", e.message, e)
    }
    context.unbindService(this)
  }

  override fun onServiceDisconnected(name: ComponentName?) {

  }

}