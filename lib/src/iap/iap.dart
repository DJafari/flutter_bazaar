part of flutter_bazaar;

class IAP {
  final String _key;

  static const EventChannel _eventChannel =
    const EventChannel('${FlutterBazaar._NAMESPACE}/iap_events');

  static const MethodChannel _methodChannel =
    const MethodChannel('${FlutterBazaar._NAMESPACE}/methods');

  StreamSubscription _subscription;

  IAP._(this._key);

  bool _connected = false;
  
  Future<bool> _connect() async {
    this._connected = await _methodChannel.invokeMethod('iapConnect', {
      "key": _key,
    });
    _subscription = _eventChannel.receiveBroadcastStream().listen((event) {
      _connected = event;
      if(!_connected) {
        _subscription.cancel();
      }
    });
    return this._connected;
  }

  Future<PurchaseInfo> purchase(String productId, {String payLoad = ""}) async {
    if(!_connected) {
      await _connect();
    }
    final result = await _methodChannel.invokeMethod('iapPurchase', {
      'productId': productId,
      'payLoad': payLoad,
    });

    if(result != null) {
      return PurchaseInfo.fromMap(Map<String, dynamic>.from(result));
    }
    return null;
  }

  Future<PurchaseInfo> subscribe(String productId, {String payLoad = ""}) async {
    if(!_connected) {
      await _connect();
    }
    final result = await _methodChannel.invokeMethod('iapSubscribe', {
      'productId': productId,
      'payLoad': payLoad,
    });

    if(result != null) {
      return PurchaseInfo.fromMap(Map<String, dynamic>.from(result));
    }
    return null;
  }

  Future<List<PurchaseInfo>> getPurchasedProducts() async {
    if(!_connected) {
      await _connect();
    }
    final result = await _methodChannel.invokeMethod('iapGetPurchasedProducts');

    return result.map<PurchaseInfo>((item) {
      return PurchaseInfo.fromMap(Map<String, dynamic>.from(item));
    }).toList();
  }

  Future<List<PurchaseInfo>> getSubscribedProducts() async {
    if(!_connected) {
      await _connect();
    }
    final result = await _methodChannel.invokeMethod('iapGetSubscribedProducts');

    return result.map<PurchaseInfo>((item) {
      return PurchaseInfo.fromMap(Map<String, dynamic>.from(item));
    }).toList();
  }

  Future<bool> consume(String purchaseToken) async {
    if(!_connected) {
      await _connect();
    }
    return await _methodChannel.invokeMethod('iapConsume', {
      "purchaseToken": purchaseToken,
    });
  }

  Future<bool> disconnect() {
    if(_connected) {
      _subscription.cancel();
      return _methodChannel.invokeMethod('iapDisconnect');
    }
    return Future.value(true);
  }
}