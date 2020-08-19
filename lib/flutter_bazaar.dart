library flutter_bazaar;

import 'dart:async';
import 'package:flutter/services.dart';
import 'src/iap/purchase_info.dart';

part 'src/iap/iap.dart';

class FlutterBazaar {
  static const String _NAMESPACE = 'davoood_flutter_bazaar';

  static const MethodChannel _methodChannel = const MethodChannel(
    '${FlutterBazaar._NAMESPACE}/methods'
  );

  FlutterBazaar._();

  static FlutterBazaar _instance = FlutterBazaar._();
  static FlutterBazaar get instance => _instance;

  IAP inAppPurchase(String publicKey) => IAP._(publicKey);

  Future<int> getLatestVersion() =>
    _methodChannel.invokeMethod('getLatestVersion');

  Future<bool> isLoggedIn() =>
    _methodChannel.invokeMethod('isLoggedIn');

  Future openLogin() =>
    _methodChannel.invokeMethod('openLogin');

  Future openDetail([String packageName]) =>
    _methodChannel.invokeMethod('openDetail', {
      'packageName': packageName,
    });

  Future openCommentForm([String packageName]) =>
    _methodChannel.invokeMethod('openCommentForm', {
      'packageName': packageName,
    });

  Future openDeveloperPage(String developerId) =>
    _methodChannel.invokeMethod('openDeveloperPage', {
      'developerId': developerId,
    });
}
