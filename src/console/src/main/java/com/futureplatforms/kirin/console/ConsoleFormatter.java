package com.futureplatforms.kirin.console;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.futureplatforms.kirin.dependencies.Formatter;
import com.google.common.io.BaseEncoding;

public class ConsoleFormatter extends Formatter {

	@Override
	public Date parseDate(String format, String text) {
		try {
			return new SimpleDateFormat(format).parse(text);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String formatDate(String format, Date date) {
		return new SimpleDateFormat(format).format(date);
	}

	@Override
	public String formatLocalisedShortTime(Date date) {
		return SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(
				date);
	}

	@Override
	public String formatLocalisedLongDateAndTime(Date date) {
		return SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.LONG,
				SimpleDateFormat.LONG).format(date);
	}

	@Override
	public String formatNumber(String format, Number number) {
		return new DecimalFormat(format).format(number);
	}

	@Override
	public String formatCurrency(String currency, BigDecimal amount) {
		NumberFormat f = NumberFormat.getCurrencyInstance();
		f.setCurrency(Currency.getInstance(currency));

		return f.format(amount);
	}

	@Override
	public String urlDecode(String toDecode) {
		return URLDecoder.decode(toDecode);
	}

	@Override
	public String encodeURIComponent(String str) {
		try {
			return URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

    @Override
    public String urlEncode(String toEncode) {
        // MAKESHIFT IMPLEMENTATION, PLEASE TEST
        return encodeURIComponent(toEncode);
    }
 
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    
	@Override
	public byte[] hmacSHA1(String message, String passphrase) {
		try {
			SecretKeySpec signingKey = new SecretKeySpec(passphrase.getBytes(), "HmacSHA1");
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(signingKey);
			byte[] bytes = mac.doFinal(message.getBytes());
			return bytes;
		} catch (Throwable t) {
			t.printStackTrace();
			return null;
		}
	}
}
