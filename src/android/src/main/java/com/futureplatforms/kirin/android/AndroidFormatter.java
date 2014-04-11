package com.futureplatforms.kirin.android;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.futureplatforms.kirin.dependencies.AsyncCallback.AsyncCallback1;
import com.futureplatforms.kirin.dependencies.Formatter;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;

public class AndroidFormatter extends Formatter {

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

	@SuppressWarnings("deprecation")
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

	@Override
	public String format(String string, Object... args) {
		return String.format(string, args);
	}

	@Override
	public String sha512B64(String toEncode) {
		return BaseEncoding.base64().encode(Hashing.sha512().hashString(toEncode, Charset.defaultCharset()).asBytes());
	}

	public static String decrypt(String encodedB64, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		HashCode code = Hashing.sha512().hashBytes(password.getBytes("UTF8"));
		byte[] hash64 = code.asBytes();
		
		byte[] decoded = BaseEncoding.base64().decode(encodedB64);
				
		// The password can only be 128 bits so take the first 16 bytes of the hash
		byte[] first16 = new byte[16];
		System.arraycopy(hash64, 0, first16, 0, 16);
		SecretKeySpec sks = new SecretKeySpec(first16, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, sks, new IvParameterSpec(new byte[16]));
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);
	}
	
	@Override
	public String decryptAES(String encodedB64, String password) {
		try {
			return decrypt(encodedB64, password);
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
		return "";
	}

	@Override
	public void pbkdf2(String plaintext, String salt, int iterations,
			int keyLenBytes, AsyncCallback1<byte[]> cb) {
		try {
			PBEKeySpec spec = new PBEKeySpec(plaintext.toCharArray(), salt.getBytes(), iterations, keyLenBytes * 8);
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] hash = skf.generateSecret(spec).getEncoded();
			cb.onSuccess(hash);
		} catch (Exception e) {
			cb.onFailure();
		}
	}
}
