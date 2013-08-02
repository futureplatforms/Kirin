package com.futureplatforms.kirin.android;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;

import org.apache.http.client.utils.URLEncodedUtils;

import com.futureplatforms.kirin.dependencies.Formatter;

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

	@Override
	public String urlDecode(String toDecode) {
		return URLDecoder.decode(toDecode);
	}

	@Override
	public String encodeURIComponent(String str) {
		return URLEncoder.encode(str);
	}

}
