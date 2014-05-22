package com.futureplatforms.kirin.gwt.client.delegates;

import java.math.BigDecimal;
import java.util.Date;

import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;
import com.futureplatforms.kirin.dependencies.Formatter;
import com.futureplatforms.kirin.gwt.client.services.CryptoService;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.regexp.shared.SplitResult;

public class GwtFormatter extends Formatter {

    @Override
    public Date parseDate(String format, String text) {
    	Date date = new Date(0);
    	int success = DateTimeFormat.getFormat(format).parse(text, 0, date);
    	
    	if (success == 0) return null;
    	return date;
    }

    @Override
    public String formatDate(String format, Date date) {
        return DateTimeFormat.getFormat(format).format(date);
    }

    @Override
    public String formatLocalisedShortTime(final Date date) {
        return DateTimeFormat.getFormat(PredefinedFormat.TIME_SHORT).format(date);
    }
    
    @Override
    public String formatLocalisedLongDateAndTime(final Date date) {
        return DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_LONG).format(date);
    }
    
    @Override
    public String formatNumber(String format, Number number) {
        return NumberFormat.getFormat(format).format(number);
    }

    @Override
    public String formatCurrency(String currency, BigDecimal price) {
        return NumberFormat.getCurrencyFormat(currency).format(price);
    }

    @Override
    public String urlDecode(String toDecode) {
        return URL.decode(toDecode);
    }
    
    public String urlEncode(String toEncode) {
        return URL.encode(toEncode);
    }
    
    @Override
    public native String encodeURIComponent(String key) /*-{
        return encodeURIComponent(key);
    }-*/;

	@Override
	public byte[] hmacSHA1(String message, String passphrase) {
		String str = _hmacSHA1(message, passphrase);
		int numBytes = str.length() / 2;
		byte[] bytes = new byte[numBytes];
		for (int i=0; i<numBytes; i++) {
			int start = i*2;
			int end = start + 2;
			String byteStr;
			if (end < str.length()) {
				byteStr = str.substring(start, end);
			} else {
				byteStr = str.substring(start);
			}
			
			bytes[i] = (byte)Integer.parseInt(byteStr, 16);
		}
		return bytes;
	}
	
	private native String _hmacSHA1(String message, String passphrase) /*-{
		var res = $wnd.CryptoJS.HmacSHA1(message, passphrase);
		return res.toString($wnd.CryptoJS.enc.Hex);
	}-*/;

	@Override
	public String format(final String format, final Object... args) {
		  final RegExp regex = RegExp.compile("%[a-z]");
		  final SplitResult split = regex.split(format);
		  final StringBuffer msg = new StringBuffer();
		  for (int pos = 0; pos < split.length() - 1; ++pos) {
		    msg.append(split.get(pos));
		    msg.append(args[pos].toString());
		  }
		  msg.append(split.get(split.length() - 1));
		  return msg.toString();
		}

	@Override
	public String sha512B64(String toEncode) {
		return _sha512B64(toEncode);
	}
	
	private native static final String _sha512B64(String toEncode) /*-{
		var sha = new $wnd.jsSHA(toEncode, 'TEXT')
		var hash = sha.getHash('SHA-512', 'B64')
		return hash
	}-*/;

	@Override
	public void pbkdf2(String plaintext, String salt, int iterations, int keyLenBytes, AsyncCallback1<byte[]> cb) {
		CryptoService.BACKDOOR()._pbkdf2(plaintext, salt, iterations, keyLenBytes, cb);
	}
	
	@Override
	public String decryptAES(String encodedB64, String password) {
		return _decryptAES(encodedB64, password);
	}
	
	private native final String _decryptAES(String encodedB64, String password) /*-{
		var cryptoJS = $wnd.CryptoJS;
		var first16OfHash = function(phrase) {
			var sha512ed = cryptoJS.SHA512(phrase);
			var asHex = sha512ed.toString(cryptoJS.enc.Hex);
			return asHex.substring(0, 32);
		};
		
		var iv = cryptoJS.enc.Hex.parse('00000000000000000000000000000000');
		var first16pass = first16OfHash(password);
		var passAsHex = cryptoJS.enc.Hex.parse(first16pass);

		var bSuccess = false;
		var result;
		// attempt to decrypt until successful, loop needed due to iOS bug see https://code.google.com/p/crypto-js/issues/detail?id=80
		while (!bSuccess) {
			try {
	    		var decrypted = cryptoJS.AES.decrypt(encodedB64, passAsHex, { iv: iv, mode: cryptoJS.mode.CBC });
			    result = decrypted.toString(cryptoJS.enc.Utf8);
			    bSuccess = true;
			} catch(e) {
		      //bSuccess=false;
			}
		  }
    	return result;
	}-*/;

}
