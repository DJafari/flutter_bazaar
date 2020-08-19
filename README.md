<a href="https://github.com/DJafari/flutter_bazaar/README_FA.md">SHOW PERSIAN README</a>

# flutter_bazaar
CafeBazaar API (In-App Purchase, Intents, UpdateChecker, LoginChecker) in flutter, works only in Android platform

## Getting Started
first, add `flutter_bazaar` to your pubspec dependencies.

```yml
flutter_bazaar: <LAST VERSION>
```

To import `flutter_bazaar`:

```dart
import 'package:flutter_bazaar/flutter_bazaar.dart';
```

Done!


## API
- <a href="#in-app-purchase">In-App purchase</a>
    - <a href="#initialization">Initialization</a>
    - <a href="#purchase-a-product">Purchase a product</a>
    - <a href="#subscribe-to-product">Subscribe to product</a>
    - <a href="#consume-a-purchase">Consume a purchase</a>
    - <a href="#disconnect">Disconnect</a>
- <a href="#intents">Intents</a>
    - <a href="#open-application-detail-page">Open application detail page</a>
    - <a href="#open-developer-applications-list-page">Open Developer applications List</a>
    - <a href="#open-comment-form">Open comment form</a>
    - <a href="#open-login-page">Open login page</a>
- <a href="#update-checker">Update Checker</a>
- <a href="#update-checker">Login Checker</a>



## In-App purchase
InAppPurchase of this library build by <a href="https://github.com/cafebazaar/Poolakey">Poolakey</a>, new cafebazaar IAP library

### Initialization
first you must initialized iap :

```dart
final _bazaar = FlutterBazaar.instance;
final iap = _bazaar.inAppPurchase(PUBLIC_KEY);
```

**PUBLIC_KEY** is your public RSA key from cafebazaar control panel


### Purchase a product

```dart
final PurchaseInfo purchaseInfo = await iap.purchase("productId", payLoad: "Your payload");
if(purchaseInfo != null) {
  print('success: $purchaseResult');
}
```

if `purchaseInfo != null` purchase is successful 

`purchaseInfo` is full detail of purchase :

```dart
class PurchaseInfo {
  final String orderId;
  final String purchaseToken;
  final String payload;
  final String packageName;
  final String purchaseState;
  final int purchaseTime;
  final String productId;
  final String dataSignature;
}
```



### Subscribe to product

```dart
final PurchaseInfo purchaseInfo = await iap.subscribe("productId", payLoad: "Your payload");
if(purchaseInfo != null) {
  print('success: $subscribeResult');
}
```

if `purchaseInfo != null` subscription is successful 


### Consume a purchase

```dart
final bool consumeResult = await iap.consume("PURCHASE TOKEN"); //IN PurchaseInfo.purchaseToken
```

**note**: if you need `PurchaseToken` after `purchase` you can getting it from `purchaseInfo.purchaseToken`


### User purchases
for getting all purchases of user :

```dart
final List<PurchaseInfo> purchasedProducts = await iap.getPurchasedProducts();
print(purchasedProducts);
```


### User Subscriptions
for getting all subscriptions of user :

```dart
final List<PurchaseInfo> subscribedProducts = await iap.getSubscribedProducts();
print(subscribedProducts);
```


### Disconnect
for disconnect connection of app and cafebazaar :

```dart
await iap.disconnect();
```


## Intents

### Open application detail page
for open details page of your app ( or another application ) you can use this method :

```dart
final _bazaar = FlutterBazaar.instance;
await _bazaar.openDetail([String packageName]);
print('USER BACK TO YOUR APP');
```

if `packageName == null` this library open details page of current packageName

**note**: this method is Future, and you can found when user back to app


### Open Developer applications List
for open list of all applications of developer, you can use this method :

```dart
final _bazaar = FlutterBazaar.instance;
await _bazaar.openDeveloperPage(String developerId);
print('USER BACK TO YOUR APP');
```

**note**: this method is Future, and you can found when user back to app


### Open comment form
for open comment form of this app ( or given packageName app ) use this method :

```dart
final _bazaar = FlutterBazaar.instance;
await _bazaar.openCommentForm([String packageName]);
print('USER BACK TO YOUR APP');
```

if `packageName == null` this library open comment form of current packageName

**note**: this method is Future, and you can found when user back to app


## Update Checker
for getting current version of your app in cafebazaar market :

```dart
final _bazaar = FlutterBazaar.instance;
final int versionCode = await _bazaar.getLatestVersion();
```



## Login Checker
for getting current status of logged user in cafebazaar, you can use this method :

```dart
final _bazaar = FlutterBazaar.instance;
final bool isLoggedIn = await _bazaar.isLoggedIn();
```

for more info check <a href="https://github.com/DJafari/flutter_bazaar/example">Example</a>