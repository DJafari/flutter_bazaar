package ir.davoood.flutter_bazaar

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.annotation.NonNull
import com.phelat.poolakey.Connection
import com.phelat.poolakey.Payment
import com.phelat.poolakey.config.PaymentConfiguration
import com.phelat.poolakey.config.SecurityCheck
import com.phelat.poolakey.entity.PurchaseInfo
import com.phelat.poolakey.request.PurchaseRequest
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.ActivityResultListener

fun PurchaseInfo.toMap() : Map<String, Any> {
  val map = HashMap<String, Any>()
  map["orderId"] = orderId
  map["purchaseToken"] = purchaseToken
  map["payload"] = payload
  map["packageName"] = packageName
  map["purchaseState"] = purchaseState.name
  map["purchaseTime"] = purchaseTime
  map["productId"] = productId
  map["originalJson"] = originalJson
  map["dataSignature"] = dataSignature

  return map
}

/** FlutterBazaarPlugin */
class FlutterBazaarPlugin: FlutterPlugin, MethodCallHandler, ActivityAware, ActivityResultListener {
  companion object {
    private const val IAP_REQUEST_CODE = 23452362
    private const val MARKET_REQUEST_CODE = 36345
    private const val NAMESPACE = "davoood_flutter_bazaar"
  }

  private lateinit var channel : MethodChannel
  private lateinit var iapEventChannel : EventChannel
  private var activity : Activity? = null
  private var payment : Payment? = null
  private var paymentConnection : Connection? = null
  private var pendingResult : Result? = null
  private var iapEventSink : EventChannel.EventSink? = null

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "$NAMESPACE/methods")
    channel.setMethodCallHandler(this)
    iapEventChannel = EventChannel(flutterPluginBinding.binaryMessenger, "$NAMESPACE/iap_events")
    iapEventChannel.setStreamHandler(
      object : EventChannel.StreamHandler {
        override fun onListen(arguments: Any?, events: EventChannel.EventSink) {
          iapEventSink = events
        }

        override fun onCancel(arguments: Any?) {
          iapEventSink = null
        }
      }
    )
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
    binding.addActivityResultListener(this)
  }

  override fun onDetachedFromActivity() {
    activity = null
    if(payment != null && paymentConnection != null) {
      paymentConnection!!.disconnect()
      payment = null
      paymentConnection = null
    }
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    activity = binding.activity
  }

  override fun onDetachedFromActivityForConfigChanges() {

  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when (call.method) {
      "openLogin" -> openLogin(result)
      "openDetail" -> openDetail(call, result)
      "openCommentForm" -> openCommentForm(call, result)
      "openDeveloperPage" -> openDeveloperPage(call, result)
      "isLoggedIn" -> isLoggedIn(result)
      "getLatestVersion" -> getLatestVersion(result)
      "iapConnect" -> iapConnect(call, result)
      "iapDisconnect" -> iapDisconnect(result)
      "iapPurchase" -> iapPurchase(call, result)
      "iapSubscribe" -> iapSubscribe(call, result)
      "iapConsume" -> iapConsume(call, result)
      "iapGetPurchasedProducts" -> iapGetPurchasedProducts(result)
      "iapGetSubscribedProducts" -> iapGetSubscribedProducts(result)
      else -> result.notImplemented()
    }
  }

  private fun openDetail(@NonNull call: MethodCall, @NonNull result: Result) {
    if(activity == null) {
      result.error("openDetail", "activity == null", null)
      return
    }
    try {
      val intent = Intent(Intent.ACTION_VIEW)
      var packageName = call.argument<String>("packageName")
      if(packageName == null) {
        packageName = activity!!.packageName
      }

      intent.data = Uri.parse("bazaar://details?id=$packageName")
      intent.setPackage("com.farsitel.bazaar")
      activity!!.startActivityForResult(intent, MARKET_REQUEST_CODE)
      pendingResult = result
    } catch (e: Exception) {
      result.error("openDetail", "CafeBazaar not installed!", e.message)
    }
  }

  private fun openCommentForm(@NonNull call: MethodCall, @NonNull result: Result) {
    if(activity == null) {
      result.error("openComments", "activity == null", null)
      return
    }
    try {
      val intent = Intent(Intent.ACTION_EDIT)
      var packageName = call.argument<String>("packageName")
      if(packageName == null) {
        packageName = activity!!.packageName
      }
      intent.data = Uri.parse("bazaar://details?id=$packageName")
      intent.setPackage("com.farsitel.bazaar")
      activity!!.startActivityForResult(intent, MARKET_REQUEST_CODE)
      pendingResult = result
    } catch (e: Exception) {
      result.error("error", "CafeBazaar not installed!", e.message)
    }
  }

  private fun openDeveloperPage(@NonNull call: MethodCall, @NonNull result: Result) {
    if(activity == null) {
      result.error("openDeveloperPage", "activity == null", null)
      return
    }
    val developerId = call.argument<String>("developerId")
    if(developerId == null) {
      result.error("openDeveloperPage", "developerId is required", null)
      return
    }
    try {
      val intent = Intent(Intent.ACTION_VIEW)
      intent.data = Uri.parse("bazaar://collection?slug=by_author&aid=$developerId")
      intent.setPackage("com.farsitel.bazaar")
      activity!!.startActivityForResult(intent, MARKET_REQUEST_CODE)
      pendingResult = result
    } catch (e: Exception) {
      result.error("openDeveloperPage", "CafeBazaar not installed!", e.message)
    }
  }

  private fun openLogin(@NonNull result: Result) {
    if(activity == null) {
      result.error("openLogin", "activity == null", null)
      return
    }
    try {
      val intent = Intent(Intent.ACTION_VIEW)
      intent.data = Uri.parse("bazaar://login")
      intent.setPackage("com.farsitel.bazaar")
      activity!!.startActivityForResult(intent, MARKET_REQUEST_CODE)
      pendingResult = result
    } catch (e: Exception) {
      result.error("openLogin", "CafeBazaar not installed!", e.message)
    }
  }

  private fun isLoggedIn(@NonNull result: Result) {
    if(activity == null) {
      result.error("isLoggedIn", "activity == null", null)
      return
    }
    val bgResult = BgResult(result)
    val service = LoginServiceConnection(bgResult, activity!!)
    val intent = Intent("com.farsitel.bazaar.service.LoginCheckService.BIND")
    intent.setPackage("com.farsitel.bazaar")
    activity!!.bindService(intent, service, Context.BIND_AUTO_CREATE)
  }

  private fun getLatestVersion(@NonNull result: Result) {
    if(activity == null) {
      result.error("getLatestVersion", "activity == null", null)
      return
    }
    val bgResult = BgResult(result)
    val service = UpdateServiceConnection(bgResult, activity!!)
    val intent = Intent("com.farsitel.bazaar.service.UpdateCheckService.BIND")
    intent.setPackage("com.farsitel.bazaar")
    activity!!.bindService(intent, service, Context.BIND_AUTO_CREATE)
  }

  private fun iapConnect(@NonNull call: MethodCall, @NonNull result: Result) {
    val localSecurityCheck = SecurityCheck.Enable(
      call.argument<String>("key") ?: ""
    )
    val paymentConfiguration = PaymentConfiguration(localSecurityCheck)
    payment = Payment(activity!!, paymentConfiguration)
    paymentConnection = payment!!.connect {
      connectionSucceed {
        result.success(true)
        if(iapEventSink != null) {
          iapEventSink!!.success(true)
        }
      }
      connectionFailed { throwable ->
        result.error("iapConnect", "connectionFailed", throwable)
        iapEventSink!!.success(false)
      }
      disconnected {
        paymentConnection = null
        payment = null
        iapEventSink!!.success(false)
      }
    }
  }

  private fun iapDisconnect(@NonNull result: Result) {
    if(paymentConnection == null) {
      result.error("iapDisconnect", "paymentConnection == null", null)
      return
    }
    paymentConnection!!.disconnect()
    result.success(true)
  }

  private fun iapPurchase(@NonNull call: MethodCall, @NonNull result: Result) {
    if(paymentConnection == null) {
      result.error("iapPurchase", "paymentConnection == null", null)
      return
    }
    if(activity == null) {
      result.error("iapPurchase", "activity == null", null)
      return
    }
    val productId = call.argument<String>("productId")
    if(productId == null) {
      result.error("iapPurchase", "productId == null", null)
      return
    }
    val payLoad = call.argument<String>("payLoad") ?: ""
    val purchaseRequest = PurchaseRequest(productId, IAP_REQUEST_CODE, payLoad)

    pendingResult = result
    payment!!.purchaseProduct(activity!!, purchaseRequest) {
      purchaseFlowBegan {

      }
      failedToBeginFlow { throwable ->
        result.error("iapPurchase", "failedToBeginFlow", throwable)
      }
    }
  }

  private fun iapSubscribe(@NonNull call: MethodCall, @NonNull result: Result) {
    if(paymentConnection == null) {
      result.error("iapSubscribe", "paymentConnection == null", null)
      return
    }
    if(activity == null) {
      result.error("iapSubscribe", "activity == null", null)
      return
    }
    val productId = call.argument<String>("productId")
    if(productId == null) {
      result.error("iapSubscribe", "productId == null", null)
      return
    }
    val payLoad = call.argument<String>("payLoad") ?: ""
    val purchaseRequest = PurchaseRequest(productId, IAP_REQUEST_CODE, payLoad)

    pendingResult = result
    payment!!.subscribeProduct(activity!!, purchaseRequest) {
      purchaseFlowBegan {

      }
      failedToBeginFlow { throwable ->
        result.error("iapSubscribe", "failedToBeginFlow", throwable)
      }
    }
  }

  private fun iapConsume(@NonNull call: MethodCall, @NonNull result: Result) {
    if(paymentConnection == null) {
      result.error("iapConsume", "paymentConnection == null", null)
      return
    }
    if(activity == null) {
      result.error("iapConsume", "activity == null", null)
      return
    }
    val purchaseToken = call.argument<String>("purchaseToken")
    if(purchaseToken == null) {
      result.error("iapPurchase", "purchaseToken == null", null)
      return
    }

    payment!!.consumeProduct(purchaseToken) {
      consumeSucceed {
        result.success(true)
      }
      consumeFailed { throwable ->
        result.error("iapConsume", "consumeFailed", throwable)
      }
    }
  }

  private fun iapGetPurchasedProducts(@NonNull result: Result) {
    if(paymentConnection == null) {
      result.error("iapGetPurchasedProducts", "paymentConnection == null", null)
      return
    }
    if(activity == null) {
      result.error("iapGetPurchasedProducts", "activity == null", null)
      return
    }
    payment!!.getPurchasedProducts {
      querySucceed { purchasedProducts ->
        result.success(purchasedProducts.map { item -> item.toMap() }.toList())
      }
      queryFailed { e ->
        result.error("iapGetPurchasedProducts", "queryFailed", e)
      }
    }
  }

  private fun iapGetSubscribedProducts(@NonNull result: Result) {
    if(paymentConnection == null) {
      result.error("iapGetSubscribedProducts", "paymentConnection == null", null)
      return
    }
    if(activity == null) {
      result.error("iapGetSubscribedProducts", "activity == null", null)
      return
    }
    payment!!.getSubscribedProducts {
      querySucceed { purchasedProducts ->
        result.success(purchasedProducts.map { item -> item.toMap() }.toList())
      }
      queryFailed { e ->
        result.error("iapGetSubscribedProducts", "queryFailed", e)
      }
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
    if(pendingResult != null) {
      if(requestCode == MARKET_REQUEST_CODE) {
        pendingResult!!.success(true)
        return true
      }
      if(payment == null) return false

      payment!!.onActivityResult(requestCode, resultCode, data) {
        purchaseSucceed { purchaseEntity ->
          pendingResult!!.success(purchaseEntity.toMap())
        }
        purchaseCanceled {
          pendingResult!!.success(null)
        }
        purchaseFailed { throwable ->
          pendingResult!!.error("iapPurchase", "purchaseFailed", throwable)
        }
      }
    }
    return false
  }
}
