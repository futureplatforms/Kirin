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
        return DateTimeFormat.getFormat(format).parse(text);
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
    
    @Override
    public native String encodeURIComponent(String key) /*-{
        return encodeURIComponent(key);
    }-*/;
}
