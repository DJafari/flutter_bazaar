package ir.davoood.flutter_bazaar

import android.os.Handler
import io.flutter.plugin.common.MethodChannel.Result

class BgResult(private var result: Result) : Result {
  private var handler : Handler = Handler()

  override fun success(results: Any?) {
    handler.post { result.success(results) }
  }

  override fun error(errorCode: String?, errorMessage: String?, errorDetails: Any?) {
    handler.post { result.error(errorCode, errorMessage, errorDetails) }
  }

  override fun notImplemented() {
    handler.post { result.notImplemented() }
  }
}