# flutter_bazaar
کتابخانه جهت انجام API های اختصاصی اپلیکیشن کافه بازار در فلاتر و فقط پلت فرم اندروید!

## Getting Started

ابتدا پکیح `flutter_bazaar` را به فایل pubspec پروژه خود اضافه کنید.

```yml
flutter_bazaar: <LAST VERSION>
```

سپس با استفاده از دستور زیاد کلاس های این کتابخانه را به پروژه خود اضافه کنید.

```dart
import 'package:flutter_bazaar/flutter_bazaar.dart';
```

تمام!


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
اول می بایست پرداخت درون برنامه ای را فراخوانی کنید 

```dart
final _bazaar = FlutterBazaar.instance;
final iap = _bazaar.inAppPurchase(PUBLIC_KEY);
```

**PUBLIC_KEY** کلید عمومی اپلیکیشن شماست که از بخش پرداخت درون برنامه ای پنل توسعه دهندگان کافه بازار قابل دریافت است.


### Purchase a product
برای خرید یک محصول از متد زیر استفاده کنید :

```dart
final PurchaseInfo purchaseInfo = await iap.purchase("productId", payLoad: "Your payload");
if(purchaseInfo != null) {
  print('success: $purchaseResult');
}
```

در صورتی که `purchaseInfo != null` باشد خرید با موفقیت انجام گرفته است.

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
برای خرید اشتراک یک محصول از متد زیر استفاده کنید:

```dart
final PurchaseInfo purchaseInfo = await iap.subscribe("productId", payLoad: "Your payload");
if(purchaseInfo != null) {
  print('success: $subscribeResult');
}
```

در صورتی که `purchaseInfo != null` باشد خرید اشتراک با موفقیت انجام گرفته است.


### Consume a purchase
برای مصرف کردن یک خرید از متد زیر استفاده کنید :

```dart
final bool consumeResult = await iap.consume("PURCHASE TOKEN"); //IN PurchaseInfo.purchaseToken
```

**توجه**: در صورتی که تمایل دارید بعد از خرید، خرید را مصرف کنید و نیاز به `PurchaseToken` دارید، می توانید آن را از کلاسی که در جواب خرید به شما داده شده بردارید : `purchaseInfo.purchaseToken`


### User purchases
برای دریافت لیست تمام محصولات مصرف نشده کاربر :

```dart
final List<PurchaseInfo> purchasedProducts = await iap.getPurchasedProducts();
print(purchasedProducts);
```

### User Subscriptions
برای دریافت تمامی اشتراک های فعال کاربر :

```dart
final List<PurchaseInfo> subscribedProducts = await iap.getSubscribedProducts();
print(subscribedProducts);
```


### Disconnect
برای قطع ارتباط بین اپلیکیشن شما و کلاینت بازار :

```dart
await iap.disconnect();
```

## Intents

### Open application detail page
برای باز کردن صفحه توضیحات اپلیکیشن شما ( و یا هر اپلیکیشنی که شما از طریق نام بسته ارسال میکنید ) می توانید از متد زیر استفاده کنید :

```dart
final _bazaar = FlutterBazaar.instance;
await _bazaar.openDetail([String packageName]);
print('USER BACK TO YOUR APP');
```

اگر `packageName == null` این کتابخانه صفحه توضیحات اپلیکیشن شمارو به طور اتوماتیک باز می کند.

### Open Developer applications List
برای بازکردن لیست تمامی اپلیکیشن های یک توسعه دهنده در بازار می توانید از متد زیر استفاده کنید:

```dart
final _bazaar = FlutterBazaar.instance;
await _bazaar.openDeveloperPage(String developerId);
print('USER BACK TO YOUR APP');
```


### Open comment form
برای باز کردن فرم ثبت نظر به اپلیکیشن شما ( و یا هر اپلیکیشنی که شما از طریق نام بسته ارسال میکنید ) میتوانید از این متد استفاده کنید:

```dart
final _bazaar = FlutterBazaar.instance;
await _bazaar.openCommentForm([String packageName]);
print('USER BACK TO YOUR APP');
```

برای باز کردن صفحه توضیحات اپلیکیشن شما ( و یا هر اپلیکیشنی که شما از طریق نام بسته ارسال میکنید ) می توانید از متد زیر استفاده کنید :


## Update Checker
برای دریافت آخرین نسخه اپلیکیشن شما در کافه بازار می توانید از این متد استفاده کنید :

```dart
final _bazaar = FlutterBazaar.instance;
final int versionCode = await _bazaar.getLatestVersion();
```

## Login Checker
برای فهمیدن وضعیت اینکه کاربر در کافه بازار وارد حساب کاربری خود شده یا خیر می توانید از این متد استفاده کنید :

```dart
final _bazaar = FlutterBazaar.instance;
final bool isLoggedIn = await _bazaar.isLoggedIn();
```

برای توضیحات بیشتر <a href="https://github.com/DJafari/flutter_bazaar/example">Example</a> را مشاهده کنید. 