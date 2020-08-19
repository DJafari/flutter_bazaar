import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter_bazaar/flutter_bazaar.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final _bazaar = FlutterBazaar.instance;

  IAP _iap;
  IAP get iap {
    if (_iap == null) {
      _iap = _bazaar.inAppPurchase(PUBLIC_KEY);
    }
    return _iap;
  }

  static const String PUBLIC_KEY = "";

  bool isLoggedIn = false;
  int appVersionCode = -1;

  @override
  void initState() {
    _bazaar.isLoggedIn().then((value) {
      setState(() {
        isLoggedIn = value;
      });
    });
    _bazaar.getLatestVersion().then((value) {
      setState(() {
        appVersionCode = value;
      });
    });
    super.initState();
  }

  @override
  void dispose() {
    _iap.disconnect();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('CafeBazaar Plugin API Example'),
        ),
        body: Directionality(
          textDirection: TextDirection.rtl,
          child: SingleChildScrollView(
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  Text("آخرین نسخه برنامه : $appVersionCode"),
                  const SizedBox(
                    height: 12,
                  ),
                  if (!isLoggedIn)
                    FlatButton(
                      color: Colors.green,
                      textColor: Colors.white,
                      height: 50,
                      onPressed: _login,
                      child: Text("ورود به بازار"),
                    ),
                  if (!isLoggedIn)
                    const SizedBox(
                      height: 12,
                    ),
                  if (isLoggedIn)
                    FlatButton(
                      color: Colors.blue,
                      textColor: Colors.white,
                      height: 50,
                      onPressed: () => _purchase(
                        consume: true,
                      ),
                      child: Text("خرید محصول"),
                    ),
                  if (isLoggedIn)
                    const SizedBox(
                      height: 12,
                    ),
                  if (isLoggedIn)
                    FlatButton(
                      color: Colors.blue,
                      textColor: Colors.white,
                      height: 50,
                      onPressed: _subscribe,
                      child: Text("اشتراک محصول"),
                    ),
                  if (isLoggedIn)
                    const SizedBox(
                      height: 12,
                    ),
                  if (isLoggedIn)
                    FlatButton(
                      color: Colors.brown,
                      textColor: Colors.white,
                      height: 50,
                      onPressed: _getPurchasedProducts,
                      child: Text("لیست محصولات خریداری شده"),
                    ),
                  if (isLoggedIn)
                    const SizedBox(
                      height: 12,
                    ),
                  if (isLoggedIn)
                    FlatButton(
                      color: Colors.brown,
                      textColor: Colors.white,
                      height: 50,
                      onPressed: _getSubscribedProducts,
                      child: Text("لیست اشتراک های خریداری شده"),
                    ),
                  const SizedBox(
                    height: 12,
                  ),
                  FlatButton(
                    color: Colors.purple,
                    textColor: Colors.white,
                    height: 50,
                    onPressed: _bazaar.openDetail,
                    child: Text("مشاهده صفحه برنامه در بازار"),
                  ),
                  const SizedBox(
                    height: 12,
                  ),
                  FlatButton(
                    color: Colors.purple,
                    textColor: Colors.white,
                    height: 50,
                    onPressed: _bazaar.openCommentForm,
                    child: Text("ثبت نظر در بازار"),
                  ),
                  const SizedBox(
                    height: 12,
                  ),
                  FlatButton(
                    color: Colors.purple,
                    textColor: Colors.white,
                    height: 50,
                    onPressed: () => _bazaar.openDeveloperPage("google-llc"),
                    child: Text("مشاهده اپلیکیشن های توسعه دهنده"),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  Future _login() async {
    await _bazaar.openLogin();
    isLoggedIn = await _bazaar.isLoggedIn();
    setState(() {});
  }

  Future _purchase({bool consume = false}) async {
    final purchaseResult = await iap.purchase("productId");
    if (purchaseResult != null) {
      print('purchaseResult: $purchaseResult');
      if (consume) {
        iap.consume(purchaseResult.purchaseToken);
      }
    }
  }

  Future _subscribe() async {
    final subscribeResult = await iap.subscribe("productId");
    if (subscribeResult != null) {
      print('subscribeResult: $subscribeResult');
    }
  }

  Future _getPurchasedProducts() async {
    final purchasedProducts = await iap.getPurchasedProducts();
    print(purchasedProducts);
  }

  Future _getSubscribedProducts() async {
    final subscribedProducts = await iap.getSubscribedProducts();
    print(subscribedProducts);
  }
}
