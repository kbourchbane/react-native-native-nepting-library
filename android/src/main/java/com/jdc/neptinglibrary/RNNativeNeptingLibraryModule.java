
package com.jdc.neptinglibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Promise;
//import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableNativeMap;

public class RNNativeNeptingLibraryModule extends ReactContextBaseJavaModule {

  private Promise promise;

  private final ReactApplicationContext reactContext;

  public RNNativeNeptingLibraryModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNNativeNeptingLibrary";
  }

  @ReactMethod
  public void show(final Promise p) {
    p.resolve("hi nepting");
  }

  @ReactMethod
  public void getString(final Promise p) {

    p.resolve("bonjour Promise bridge android !!");
  }

  /*private final ActivityEventListener eventListener = new BaseActivityEventListener() {
    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
      try {
        if(intent != null) {
          if (requestCode == 9803) {
            Bundle bundle = intent.getExtras();
            WritableNativeMap resultList = new WritableNativeMap();
            for (String key : bundle.keySet()) {
              Object value = bundle.get(key);

              resultList.putString(key, value.toString());
              Log.d(TAG, String.format("%s %s (%s)", key, value.toString(), value.getClass().getName()));
            }
            if (resultCode == Activity.RESULT_OK) {
              resultList.putString("RESULT_OK", "ok");
              // TRS OK
              promise.resolve(resultList);

            } else{
              resultList.putString("RESULT_OK", "ko");
              // TRS KO
              // Verifier POS_FINAL_AMOUNT
              promise.resolve(resultList);
              //promise.reject("ERROR " + TAG, "TRS KO");
            }
          } else {
            // Not Nepting
            promise.reject("ERROR " + TAG, "Not Nepting !!");
          }
        }else{
          promise.reject("ERROR " + TAG, "Could not receive data !!");
        }
      } catch (Exception ex) {
        ex.printStackTrace();
        promise.reject("ERROR " + TAG, ex.getMessage());
        promise = null;
      }
    }
  };

  @ReactMethod
  public void startPayement(final String messageType, final String amount, final String currencyCode, final String currencyFraction, final String currencyAlpha, final String merchantTrsId, final Promise p) {

    // Add the listener for `onActivityResult`
    reactContext.addActivityEventListener(eventListener);
    promise = p;

    Intent paymentIntent = new Intent("com.nepting.android.REQUEST");
    paymentIntent.putExtra("MESSAGE_TYPE", messageType);
    paymentIntent.putExtra("AMOUNT", amount);
    paymentIntent.putExtra("CURRENCY_CODE", currencyCode); // 826
    paymentIntent.putExtra("CURRENCY_FRACTION", currencyFraction); // 2
    paymentIntent.putExtra("CURRENCY_ALPHA", currencyAlpha); // GBP
    paymentIntent.putExtra("MERCHANT_TRS_ID", merchantTrsId);
    getCurrentActivity().startActivityForResult(paymentIntent, 9803);

  }*/
}