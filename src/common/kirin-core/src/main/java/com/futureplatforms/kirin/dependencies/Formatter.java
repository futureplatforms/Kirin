package com.futureplatforms.kirin.dependencies;

import java.math.BigDecimal;
import java.util.Date;

public abstract class Formatter {
	
	/**
	 * 
	 * @param format
	 * @param text
	 * @return Date object with values parsed from text, with default values 00:00 1 Jan 1970
	 */
	
    public abstract Date parseDate(String format, String text);
    public abstract String formatDate(String format, Date date);
    public abstract String formatLocalisedShortTime(Date date);
    public abstract String formatLocalisedLongDateAndTime(Date date);
    public abstract String formatNumber(String format, Number number);
    public abstract String formatCurrency(String currency, BigDecimal amount);
    public abstract String urlDecode(String toDecode);
    public abstract String urlEncode(String toEncode);
    public abstract String encodeURIComponent(String str);
}
