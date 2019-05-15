package com.haulmont.chile.core.datatypes.impl;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.FormatStrings;
import com.haulmont.chile.core.datatypes.FormatStringsRegistry;
import com.haulmont.chile.core.datatypes.ParameterizedDatatype;
import com.haulmont.cuba.core.global.AppBeans;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;
import java.util.Locale;
import java.util.Map;

public abstract class AbstractTemporalDatatype<T extends Temporal> implements Datatype<T>, ParameterizedDatatype {
    protected String formatPattern;

    public AbstractTemporalDatatype(Element element) {
        this.formatPattern = element.attributeValue("format");
    }

    @Override
    public String format(Object value) {
        if (value == null) {
            return "";
        } else {
            DateTimeFormatter formatter;
            if (formatPattern != null) {
                formatter = DateTimeFormatter.ofPattern(formatPattern);
            } else {
                formatter = getDateTimeFormatter();
            }
            //noinspection unchecked
            return formatter.format((T) value);
        }
    }

    @Override
    public String format(@Nullable Object value, Locale locale) {
        if (value == null) {
            return "";
        }

        FormatStrings formatStrings = AppBeans.get(FormatStringsRegistry.class).getFormatStrings(locale);
        if (formatStrings == null) {
            return format(value);
        }

        DateTimeFormatter formatter = getDateTimeFormatter(formatStrings, locale);
        //noinspection unchecked
        return formatter.format((TemporalAccessor) value);
    }

    @Nullable
    @Override
    public T parse(@Nullable String value) throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        DateTimeFormatter formatter;
        if (formatPattern != null) {
            formatter = DateTimeFormatter.ofPattern(formatPattern);
        } else {
            formatter = getDateTimeFormatter();
        }
        try {
            return formatter.parse(value.trim(), newInstance());
        } catch (DateTimeParseException ex) {
            throw new ParseException(ex.getMessage(), 0);
        }
    }

    @Nullable
    @Override
    public T parse(@Nullable String value, Locale locale) throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        FormatStrings formatStrings = AppBeans.get(FormatStringsRegistry.class).getFormatStrings(locale);
        if (formatStrings == null) {
            return parse(value);
        }

        DateTimeFormatter formatter = getDateTimeFormatter(formatStrings, locale);
        try {
            return formatter.parse(value.trim(), newInstance());
        } catch (DateTimeParseException ex) {
            throw new ParseException(ex.getMessage(), 0);
        }
    }

    @Override
    public Map<String, Object> getParameters() {
        return ParamsMap.of("format", formatPattern);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    protected abstract DateTimeFormatter getDateTimeFormatter();

    protected abstract DateTimeFormatter getDateTimeFormatter(FormatStrings formatStrings, Locale locale);

    protected abstract TemporalQuery<T> newInstance();
}
