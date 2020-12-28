# @juspay/ec-react-native-library integration

This is react-native library for Juspay's Express Checkout SDK. For installing it to your project. Follow the below steps.

## Installation

```sh
npm install @juspay/ec-react-native-library --save

// Required only if react-native version is below 0.60.0
react-native link @juspay/ec-react-native-library
```

## Android

Add below maven url to your app/build.gradle

```groovy
    maven {
        url "https://maven.juspay.in/jp-build-packages/hyper-sdk/"
    }
```

**(Optional)** Add the following ext properties in root `build.gradle` if you want to override either of the base SDK versions present in plugin:

```groovy
buildscript {
  ....
   ext {
       ....
       hyperSDKVersion = "2.0.1-rc.40"
       hyperWrapperVersion = "2.0.0-13"
       ....
   }
   ....
}
```

Note: These versions are just for explanatory purposes and may change in future. Contact Juspay support team for the latest SDK versions.

## iOS

Run the following command inside the ios folder of your react native project:

```sh
pod install
```

## Usage

```sh
import EcReactNativeLibrary from '@juspay/ec-react-native-library';

var nbPayload = {
                  opName: "nbTxn",
                  paymentMethodType: "NB",
                  paymentMethod: "enter bank code", // 
                  redirectAfterPayment: "true",
                  format: "json"
                }
//Here payload format is specified for netbanking transaction. For different types of payload types 
//for other operations, refer https://developer.juspay.in/docs/expresscheckout-sdk


var requestPayload = {
                baseParams: {
                  merchant_id: "pass merchant id",
                  client_id: "pass client id",
                  transaction_id: "pass transaction id", //optional
                  order_id: "pass order id",
                  amount: "amount", //eg: "1.00"
                  customer_id : "pass customer id",
                  customer_email : "pass email",
                  customer_phone_number : "pass phone number",
                  environment: "pass environment" eg: "sandbox" or "prod"
                },
                serviceParams: {
                  service: "in.juspay.ec",
                  session_token: "pass client auth token",
                  endUrls: [], //eg: ["https://www.reload.in/recharge/", ".*www.reload.in/payment/f.*"]
                  payload: JSON.stringify(nbPayload) 
                },
                customParams: {} //customParams are optional key value pairs. 
              }
              EcReactNativeLibrary.startPayment( 
                  JSON.stringify(requestPayload), 
                  (successResponse) => {
                    console.log(successResponse);
                  },
                  (errorResponse) => {
                    console.log(errorResponse);
                  } 
              );

```
