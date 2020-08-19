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

قسمت پرداخت درون برنامه این کتابخانه با استفاده از کتابخانه جدید کافه بازار به اسم <a href="https://github.com/cafebazaar/Poolakey">پولکی</a>  توسعه داده شده است.

### Initialization

first you must initialized iap :

اول می بایست پرداخت درون برنامه ای را فراخوانی کنید 

```dart
final _bazaar = FlutterBazaar.instance;
final iap = _bazaar.inAppPurchase(PUBLIC_KEY);
```

**PUBLIC_KEY** is your public RSA key from cafebazaar control panel

**PUBLIC_KEY** کلید عمومی اپلیکیشن شماست که از بخش پرداخت درون برنامه ای پنل توسعه دهندگان کافه بازار قابل دریافت است.

### Purchase a product

```dart
final PurchaseInfo purchaseInfo = await iap.purchase("productId", payLoad: "Your payload");
if(purchaseInfo != null) {
  print('success: $purchaseResult');
}
```

if `purchaseInfo != null` purchase is successful 

در صورتی که `purchaseInfo != null` باشد خرید با موفقیت انجام گرفته است.

`purchaseInfo` is full detail of purchase :

کلاس `PurchaseInfo` شامل اطلاعات کامل خرید انجام شده است.

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

در صورتی که `purchaseInfo != null` باشد اشتراک با موفقیت انجام گرفته است.

### Consume a purchase

```dart
final bool consumeResult = await iap.consume("PURCHASE TOKEN"); //IN PurchaseInfo.purchaseToken
```

**note**: if you need `PurchaseToken` after `purchase` you can getting it from `purchaseInfo.purchaseToken`

**توجه**: در صورتی که تمایل دارید بعد از خرید، خرید را مصرف کنید و نیاز به `PurchaseToken` دارید، می توانید آن را از کلاسی که در جواب خرید به شما داده شده بردارید : `purchaseInfo.purchaseToken`

### User purchases

for getting all purchases of user :

برای دریافت لیست تمام محصولات مصرف نشده کاربر :

```dart
final List<PurchaseInfo> purchasedProducts = await iap.getPurchasedProducts();
print(purchasedProducts);
```

### User Subscriptions

for getting all subscriptions of user :

برای دریافت تمامی اشتراک های فعال کاربر :

```dart
final List<PurchaseInfo> subscribedProducts = await iap.getSubscribedProducts();
print(subscribedProducts);
```

### Disconnect

for disconnect connection of app and cafebazaar :

برای قطع ارتباط بین اپلیکیشن شما و کلاینت بازار :

```dart
await iap.disconnect();
```

## Intents

### Open application detail page

for open details page of your app ( or another application ) you can use this method :

برای باز کردن صفحه توضیحات اپلیکیشن شما ( و یا هر اپلیکیشنی که شما از طریق نام بسته ارسال میکنید ) می توانید از متد زیر استفاده کنید :

```dart
final _bazaar = FlutterBazaar.instance;
await _bazaar.openDetail([String packageName]);
print('USER BACK TO YOUR APP');
```

if `packageName == null` this library open details page of current packageName

اگر `` این کتابخانه صفحه توضیحات اپلیکیشن شمارو به طور اتوماتیک باز می کند.

**note**: this method is Future, and you can found when user back to app

### Open Developer applications List

for open list of all applications of developer, you can use this method :

برای بازکردن لیست تمامی اپلیکیشن های یک توسعه دهنده در بازار می توانید از متد زیر استفاده کنید:

```dart
final _bazaar = FlutterBazaar.instance;
await _bazaar.openDeveloperPage(String developerId);
print('USER BACK TO YOUR APP');
```

**note**: this method is Future, and you can found when user back to app

### Open comment form

for open comment form of this app ( or given packageName app ) use this method :

برای باز کردن فرم ثبت نظر به اپلیکیشن شما ( و یا هر اپلیکیشنی که شما از طریق نام بسته ارسال میکنید ) میتوانید از این متد استفاده کنید:

```dart
final _bazaar = FlutterBazaar.instance;
await _bazaar.openCommentForm([String packageName]);
print('USER BACK TO YOUR APP');
```

if `packageName == null` this library open comment form of current packageName

برای باز کردن صفحه توضیحات اپلیکیشن شما ( و یا هر اپلیکیشنی که شما از طریق نام بسته ارسال میکنید ) می توانید از متد زیر استفاده کنید :

**note**: this method is Future, and you can found when user back to app

## Update Checker

for getting current version of your app in cafebazaar market :

برای دریافت آخرین نسخه اپلیکیشن شما در کافه بازار می توانید از این متد استفاده کنید :

```dart
final _bazaar = FlutterBazaar.instance;
final int versionCode = await _bazaar.getLatestVersion();
```

## Login Checker

for getting current status of logged user in cafebazaar, you can use this method :

برای فهمیدن وضعیت اینکه کاربر در کافه بازار وارد حساب کاربری خود شده یا خیر می توانید از این متد استفاده کنید :

```dart
final _bazaar = FlutterBazaar.instance;
final bool isLoggedIn = await _bazaar.isLoggedIn();
```

for more info check <a href="https://github.com/DJafari/flutter_bazaar/example">Example</a>

برای توضیحات بیشتر <a href="https://github.com/DJafari/flutter_bazaar/example">Example</a> را مشاهده کنید. 