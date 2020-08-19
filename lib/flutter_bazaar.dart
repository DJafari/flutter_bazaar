library flutter_bazaar;

import 'dart:async';
import 'package:flutter/services.dart';
import 'src/iap/purchase_info.dart';

part 'src/iap/iap.dart';

class FlutterBazaar {
  static const String _NAMESPACE = 'davoood_flutter_bazaar';

  static const MethodChannel _methodChannel =
      const MethodChannel('${FlutterBazaar._NAMESPACE}/methods');

  FlutterBazaar._();

  static FlutterBazaar _instance = FlutterBazaar._();
  static FlutterBazaar get instance => _instance;

  /// Initialization of InAppPurchase
  IAP inAppPurchase(String publicKey) => IAP._(publicKey);

  /// Get latest version of app in cafeBazaar
  Future<int> getLatestVersion() =>
      _methodChannel.invokeMethod('getLatestVersion');

  /// Check user isLoggedIn cafeBazaar
  Future<bool> isLoggedIn() => _methodChannel.invokeMethod('isLoggedIn');

  /// Open cafeBazaar login page
  Future openLogin() => _methodChannel.invokeMethod('openLogin');

  /// Open detail page of app in cafeBazaar
  Future openDetail([String packageName]) =>
      _methodChannel.invokeMethod('openDetail', {
        'packageName': packageName,
      });

  /// Open comment form of app in cafeBazaar
  Future openCommentForm([String packageName]) =>
      _methodChannel.invokeMethod('openCommentForm', {
        'packageName': packageName,
      });

  /// Open developer app list In cafeBazaar
  Future openDeveloperPage(String developerId) =>
      _methodChannel.invokeMethod('openDeveloperPage', {
        'developerId': developerId,
      });
}
