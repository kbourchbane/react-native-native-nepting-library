
package com.jdc.neptinglibrary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableNativeMap;
import com.pax.dal.IPrinter;
import com.pax.dal.exceptions.PrinterDevException;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.InnerResultCallback;
import com.sunmi.peripheral.printer.SunmiPrinterService;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class RNNativeNeptingLibraryModule extends ReactContextBaseJavaModule {

  private Promise promise;
  private final String TAG = "pax";
  private final ReactApplicationContext reactContext;
  private IPrinter printer;
  SunmiPrinterService sunmiPrinterService;

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

  private final ActivityEventListener eventLoginListener = new BaseActivityEventListener() {
    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
      try {
        if(intent != null) {
          if (requestCode == 9804) {
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

  private final ActivityEventListener eventPayementListener = new BaseActivityEventListener() {
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
              resultList.putString("RESULT_TRS_OK", "ok");
              // TRS OK
              promise.resolve(resultList);

            } else{
              resultList.putString("RESULT_TRS_OK", "ko");
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
    public void startPayement(final String messageName, final String messageType, final String messageId, final String token, final String amount, final String currencyCode, final String currencyFraction, final String currencyAlpha, final String merchantTrsId, final String posNumber,  final Promise p) {
    // Add the listener for `onActivityResult`
    reactContext.addActivityEventListener(eventPayementListener);
    promise = p;

    Intent paymentIntent = new Intent("com.nepting.android.REQUEST");
    paymentIntent.putExtra("MESSAGE_NAME", messageName);
    paymentIntent.putExtra("MESSAGE_TYPE", messageType);
    paymentIntent.putExtra("MESSAGE_ID", messageId);
    if(!token.equalsIgnoreCase("no_token")) paymentIntent.putExtra("TOKEN", token);
    paymentIntent.putExtra("AMOUNT", amount);
    paymentIntent.putExtra("CURRENCY_CODE", currencyCode); // 826
    paymentIntent.putExtra("CURRENCY_FRACTION", currencyFraction); // 2
    paymentIntent.putExtra("CURRENCY_ALPHA", currencyAlpha); // GBP
    paymentIntent.putExtra("MERCHANT_TRS_ID", merchantTrsId);
    paymentIntent.putExtra("POS_NUMBER", posNumber);
    getCurrentActivity().startActivityForResult(paymentIntent, 9803);

  }

  @ReactMethod
  public void loginPayement(final String messageName, final String messageType, final String messageId, final String webServiceUrl, final String merchantCode, final String storeId, final String posNumber, final String cashierId, final Promise p) {
    // Add the listener for `onActivityResult`
    reactContext.addActivityEventListener(eventLoginListener);
    promise = p;

    Intent paymentIntent = new Intent("com.nepting.android.REQUEST");
    paymentIntent.putExtra("MESSAGE_NAME", messageName);
    paymentIntent.putExtra("MESSAGE_TYPE", messageType);
    paymentIntent.putExtra("MESSAGE_ID", messageId);
    paymentIntent.putExtra("WEB_SERVICE_URL", webServiceUrl);
    paymentIntent.putExtra("MERCHANT_CODE", merchantCode);
    paymentIntent.putExtra("STORE_ID", storeId);
    paymentIntent.putExtra("POS_NUMBER", posNumber);
    paymentIntent.putExtra("CASHIER_ID", cashierId);
    getCurrentActivity().startActivityForResult(paymentIntent, 9804);

  }

  @ReactMethod
  public void initPrinter(final Promise p) {
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {

        if (printer == null) {
          try {
            printer = InitDal.getDal(getReactApplicationContext()).getPrinter();
            printer.init();
            p.resolve("init ok !!");
          } catch (PrinterDevException e) {
            e.printStackTrace();
            //throw new Exception(e.getMessage());
            //Thread t = Thread.currentThread();
            //t.getUncaughtExceptionHandler().uncaughtException(t, e);
            p.reject("ERROR pax init", e.getMessage());
          }
        }
      }
    });
  }

  @ReactMethod
  public void startPrinter(final Promise p) {

    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {

        try {
          int res = printer.start();
          p.resolve(statusCode2Str(res));
        } catch (PrinterDevException e) {
          e.printStackTrace();
          p.reject("ERROR pax start", e.getMessage());
        }
      }
    });
  }

  @ReactMethod
  public void getStatusPrinter(final Promise p) {
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          int status = printer.getStatus();
          p.resolve(statusCode2Str(status));
        } catch (PrinterDevException e) {
          e.printStackTrace();
          p.reject("ERROR pax getStatusPrinter", e.getMessage());
        }
      }
    });
  }

  @ReactMethod
  public void printStrPrinter(final String str, final String charset, final Promise p) {
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try {
          printer.printStr(str, charset);
          p.resolve("printStr ok !!");
        } catch (PrinterDevException e) {
          e.printStackTrace();
          p.reject("ERROR pax printStr", e.getMessage());
        }
      }
    });
  }

  @ReactMethod
  public void printBitmapPrinter(final String bitmap, final Promise p) {

    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {

        try {
          printer.printBitmap(getBitmapFromStringBase64(bitmap));
          p.resolve("printBitmap ok !!");
        } catch (PrinterDevException e) {
          e.printStackTrace();
          p.reject("ERROR pax printBitmap", e.getMessage());
        }
      }
    });
  }

  @ReactMethod
  public void printQRcodePrinter(final String str, final Promise p) {
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        QRGEncoder qrgEncoder = new QRGEncoder(str, null, QRGContents.Type.TEXT, 200);
        qrgEncoder.setColorBlack(Color.BLACK);
        qrgEncoder.setColorWhite(Color.WHITE);

        try {
          Bitmap bitmap = qrgEncoder.getBitmap();
          printer.printBitmap(bitmap);
          p.resolve("print QRcode ok !!");
        } catch (PrinterDevException e) {
          e.printStackTrace();
          p.reject("ERROR pax print QRcode", e.getMessage());
        }
      }
    });
  }

  @ReactMethod
  public void startPrintQRcode(final String reward,  final Promise p) {
    promise = p;
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        initPrinter(p);
        printQRcodePrinter(reward, p);
        startPrinter(p);
      }
    });
  }

  InnerPrinterCallback innerPrinterCallback = new InnerPrinterCallback() {
    @Override
    protected void onConnected(SunmiPrinterService service) {
      //Here is the remote service interface handle after thebindingservice has been successfully connected
      //Supported print methods can be called through service}
      sunmiPrinterService = service;
    }

    @Override
    protected void onDisconnected() {
      //The method will be called back after the service is disconnected.A reconnection strategy is recommended here
      sunmiPrinterService = null;
    }
  };

  // SUNMI
  @ReactMethod
  public void initSunmiPrinter(final Promise p) {
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {


        try {
          boolean result = InnerPrinterManager.getInstance().bindService(getReactApplicationContext(),
                  innerPrinterCallback);
          p.resolve(result);
        } catch (InnerPrinterException e) {
          e.printStackTrace();
          p.reject("ERROR pax init", e.getMessage());
        }
      }
    });
  }

  @ReactMethod
  public void printSunmiStrPrinter(final String str, final Promise p) {
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try{
          sunmiPrinterService.printText(str, new InnerResultCallback() {
            @Override
            public void onRunResult(boolean isSuccess) throws
                    RemoteException {
                    //Return the execution result (not a real printing): succeeded or failed
                    p.resolve("isSuccess : " + isSuccess);
            }
            @Override
            public void onReturnString(String result) throws
                    RemoteException {
                    //Some interfaces return inquired data asynchronously
                    p.resolve(result);
            }
            @Override
            public void onRaiseException(int code, String msg) throws RemoteException {
              //The exception returned when the interfacefailedtoexecute
              p.reject("ERROR Sunmi print text", msg);
            }
            @Override
            public void onPrintResult(int code, String msg) throws RemoteException {
              //The real printing result returned in transactionprinting mode
              p.resolve("onPrintResult : " + msg);
            };
          });
        } catch (RemoteException e) {
          e.printStackTrace();
          //If some interfaces can only be used for specified devicemodels, thecall error reminder will pop out. For example, the interface of a cashdrawercanonly be used for a desktop device.
          p.reject("ERROR Sunmi print text catch", e.getMessage());
        }
      }
    });
  }

  @ReactMethod
  public void printSunmiQrcodePrinter(final String str, final Promise p) {
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try{
          sunmiPrinterService.printQRCode(str, 8, 1, new InnerResultCallback() {
            @Override
            public void onRunResult(boolean isSuccess) throws
                    RemoteException {
              //Return the execution result (not a real printing): succeeded or failed
              p.resolve("isSuccess : " + isSuccess);
            }
            @Override
            public void onReturnString(String result) throws
                    RemoteException {
              //Some interfaces return inquired data asynchronously
              p.resolve(result);
            }
            @Override
            public void onRaiseException(int code, String msg) throws RemoteException {
              //The exception returned when the interfacefailedtoexecute
              p.reject("ERROR Sunmi print QRCode", msg);
            }
            @Override
            public void onPrintResult(int code, String msg) throws RemoteException {
              //The real printing result returned in transactionprinting mode
              p.resolve("onPrintResult : " + msg);
            };
          });
        } catch (RemoteException e) {
          e.printStackTrace();
          //If some interfaces can only be used for specified devicemodels, thecall error reminder will pop out. For example, the interface of a cashdrawercanonly be used for a desktop device.
          p.reject("ERROR Sunmi print QRCode catch", e.getMessage());
        }
      }
    });
  }

  @ReactMethod
  public void printSunmiBitmapPrinter(final String bitmap, final Promise p) {
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try{
          sunmiPrinterService.printBitmap(getBitmapFromStringBase64(bitmap), new InnerResultCallback() {
            @Override
            public void onRunResult(boolean isSuccess) throws
                    RemoteException {
              //Return the execution result (not a real printing): succeeded or failed
              p.resolve("isSuccess : " + isSuccess);
            }
            @Override
            public void onReturnString(String result) throws
                    RemoteException {
              //Some interfaces return inquired data asynchronously
              p.resolve(result);
            }
            @Override
            public void onRaiseException(int code, String msg) throws RemoteException {
              //The exception returned when the interfacefailedtoexecute
              p.reject("ERROR Sunmi print bitmap", msg);
            }
            @Override
            public void onPrintResult(int code, String msg) throws RemoteException {
              //The real printing result returned in transactionprinting mode
              p.resolve("onPrintResult : " + msg);
            };
          });
        } catch (RemoteException e) {
          e.printStackTrace();
          //If some interfaces can only be used for specified devicemodels, thecall error reminder will pop out. For example, the interface of a cashdrawercanonly be used for a desktop device.
          p.reject("ERROR Sunmi print bitmap catch", e.getMessage());
        }
      }
    });
  }

  @ReactMethod
  public void printSunmiDisconnectionPrinter(final Promise p) {
    ThreadPoolManager.getInstance().executeTask(new Runnable() {
      @Override
      public void run() {
        try{
          InnerPrinterManager.getInstance().unBindService(getReactApplicationContext(), innerPrinterCallback);
        } catch (RemoteException e) {
          e.printStackTrace();
          //If some interfaces can only be used for specified devicemodels, thecall error reminder will pop out. For example, the interface of a cashdrawercanonly be used for a desktop device.
          p.reject("ERROR Sunmi print QRCode catch", e.getMessage());
        }
      }
    });
  }

  public Bitmap getBitmapFromStringBase64(String encodedImage){
    byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

    return decodedByte;
  }

  public String statusCode2Str(int status) {
    String res = "";
    switch (status) {
      case 0:
        res = "Success ";
        break;
      case 1:
        res = "Printer is busy ";
        break;
      case 2:
        res = "Out of paper ";
        break;
      case 3:
        res = "The format of print data packet error ";
        break;
      case 4:
        res = "Printer malfunctions ";
        break;
      case 8:
        res = "Printer over heats ";
        break;
      case 9:
        res = "Printer voltage is too low";
        break;
      case -16:
        res = "Printing is unfinished ";
        break;
      case -4:
        res = " The printer has not installed font library ";
        break;
      case -2:
        res = "Data package is too long ";
        break;
      default:
        break;
    }
    return res;
  }
}