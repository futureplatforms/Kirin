package com.futureplatforms.kirin.dependencies;

import java.math.BigDecimal;
import java.util.Date;

import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;
import com.google.common.io.BaseEncoding;

public abstract class Formatter {
	
	/**
	 * 
	 * @param format
	 * @param text
	 * @return Date object with values parsed from text, with default values 00:00 1 Jan 1970
	 */
	
    public abstract Date parseDate(String format, String text);
    public abstract String format(String string, Object... args);
    public abstract String formatDate(String format, Date date);
    public abstract String formatLocalisedShortTime(Date date);
    public abstract String formatLocalisedLongDateAndTime(Date date);
    public abstract String formatNumber(String format, Number number);
    public abstract String formatCurrency(String currency, BigDecimal amount);
    public abstract String urlDecode(String toDecode);
    public abstract String urlEncode(String toEncode);
    public abstract String encodeURIComponent(String str);
    public abstract byte[] hmacSHA1(String message, String passphrase);
    public abstract String sha512B64(String toEncode);
    public abstract String decryptAES(String encodedB64, String password);
    
    /**
     * Returns BASE64-Encoded PBKDF2 HMAC-SHA1
     * @param plaintext
     * @param salt
     * @param iterations
     * @param keyLenBytes
     * @param cb
     */
	public abstract void pbkdf2(String plaintext, String salt, int iterations, int keyLenBytes,
			AsyncCallback1<byte[]> cb);
}
