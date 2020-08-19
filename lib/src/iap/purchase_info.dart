
class PurchaseInfo {
  final String orderId;
  final String purchaseToken;
  final String payload;
  final String packageName;
  final String purchaseState;
  final int purchaseTime;
  final String productId;
  final String dataSignature;

  PurchaseInfo({
    this.orderId,
    this.purchaseToken,
    this.payload,
    this.packageName,
    this.purchaseState,
    this.purchaseTime,
    this.productId,
    this.dataSignature
  });

  factory PurchaseInfo.fromMap(Map<String, dynamic> map) =>
    PurchaseInfo(
      orderId: map['orderId'],
      purchaseToken: map['purchaseToken'],
      payload: map['payload'],
      packageName: map['packageName'],
      purchaseState: map['purchaseState'],
      purchaseTime: map['purchaseTime'] as int,
      productId: map['productId'],
      dataSignature: map['dataSignature'],
    );

  Map<String, dynamic> toMap() {
    return {
      'orderId': orderId,
      'purchaseToken': purchaseToken,
      'payload': payload,
      'packageName': packageName,
      'purchaseState': purchaseState,
      'purchaseTime': purchaseTime,
      'productId': productId,
      'dataSignature': dataSignature,
    };
  }

  @override
  String toString() => toMap().toString();
}