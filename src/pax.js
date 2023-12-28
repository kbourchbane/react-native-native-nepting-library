import { NativeModules } from 'react-native';

class Pax{
  pax = NativeModules.RNNativeNeptingLibrary;

  // pax Payement
  startPayement = async (messageName, messageType, messageId, token, amount, currencyCode, currencyFraction, currencyAlpha, merchantTrsId, posNumber) => await this.pax.startPayement(messageName, messageType, messageId, token, amount, currencyCode, currencyFraction, currencyAlpha, merchantTrsId, posNumber);
  loginPayement = async (messageName, messageType, messageId, webServiceUrl, merchantCode, storeId, posNumber, cashierId) => await this.pax.loginPayement(messageName, messageType, messageId, webServiceUrl, merchantCode, storeId, posNumber, cashierId);
  // pax Printer
  initPrinter = async () => await this.pax.initPrinter();
  getStatusPrinter = async () => await this.pax.getStatusPrinter();
  startPrinter = async () => await this.pax.startPrinter();
  printStrPrinter = async (str, charset) => await this.pax.printStrPrinter(str, charset);
  printBitmapPrinter = async (bitmap) => await this.pax.printBitmapPrinter(bitmap);
  printQRcodePrinter = async (strQrcode) => await this.pax.printQRcodePrinter(strQrcode);
  startPrintQRcode = async (strQrcode) => await this.pax.startPrintQRcode(strQrcode);

  // Sunmi printer
  initSunmiPrinter = async () => await this.pax.initSunmiPrinter();
  printSunmiStrPrinter = async (str) => await this.pax.printSunmiStrPrinter(str);
  printSunmiQrcodePrinter = async (str) => await this.pax.printSunmiQrcodePrinter(str);
  printSunmiDisconnectionPrinter = async () => await this.pax.printSunmiDisconnectionPrinter();

  // fontSetPrinter = async (asciiFontTypeStr, cFontTypeStr) => await this.pax.fontSetPrinter(asciiFontTypeStr, cFontTypeStr);
  // spaceSetPrinter = async (wordSpace, lineSpace) => await this.pax.spaceSetPrinter(wordSpace, lineSpace);
  // stepPrinter = async (b) => await this.pax.stepPrinter(b);
  // leftIndentsPrinter = async (indent) => await this.pax.leftIndentsPrinter(indent);
  // getDotLinePrinter = async () => await this.pax.getDotLinePrinter();
  // setGrayPrinter = async (level) => await this.pax.setGrayPrinter(level);
  // setDoubleWidthPrinter = async (isAscDouble, isLocalDouble) => await this.pax.setDoubleWidthPrinter(isAscDouble, isLocalDouble);
  // setDoubleHeightPrinter = async (isAscDouble, isLocalDouble) => await this.pax.setDoubleHeightPrinter(isAscDouble, isLocalDouble);
  // setInvertPrinter = async (isInvert) => await this.pax.setInvertPrinter(isInvert);
  // cutPaperPrinter = async (mode) => await this.pax.cutPaperPrinter(mode);
  // getCutModePrinter = async () => await this.pax.getCutModePrinter();

  show = async () => await this.pax.show();
  getString = async () => await this.pax.getString();
  
}

const pax = new Pax();

export default {
  Payement: {
    startPayement: pax.startPayement,
    loginPayement: pax.loginPayement,
    show: pax.show,
    getString: pax.getString,
  },
  Printer: {
    // init: pax.initPrinter,
    // getStatus: pax.getStatusPrinter,
    // fontSet: pax.fontSetPrinter,
    // spaceSet: pax.spaceSetPrinter,
    // printStr: pax.printStrPrinter,
    // step: pax.stepPrinter,
    // printBitmap: pax.printBitmapPrinter,
    // start: pax.startPrinter,
    // leftIndents: pax.leftIndentsPrinter,
    // getDotLine: pax.getDotLinePrinter,
    // setGray: pax.setGrayPrinter,
    // setDoubleWidth: pax.setDoubleWidthPrinter,
    // setDoubleHeight: pax.setDoubleHeightPrinter,
    // setInvert: pax.setInvertPrinter,
    // cutPaper: pax.cutPaperPrinter,
    // getCutMode: pax.getCutModePrinter
  }
};
