package com.haulmont.cuba.web.gui.components.valueproviders;

import com.haulmont.cuba.gui.components.Formatter;
import com.vaadin.data.ValueProvider;

public class FormatterBasedValueProvider<T> implements ValueProvider<T, String> {

    protected Formatter<T> formatter;

    public FormatterBasedValueProvider(Formatter<T> formatter) {
        this.formatter = formatter;
    }

    @Override
    public String apply(T value) {
        return formatter.format(value);
    }

    public Formatter<T> getFormatter() {
        return formatter;
    }
}
