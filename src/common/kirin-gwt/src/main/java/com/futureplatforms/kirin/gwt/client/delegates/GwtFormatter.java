package com.futureplatforms.kirin.gwt.client.delegates;

import java.math.BigDecimal;
import java.util.Date;

import com.futureplatforms.kirin.dependencies.Formatter;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.NumberFormat;

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
}
