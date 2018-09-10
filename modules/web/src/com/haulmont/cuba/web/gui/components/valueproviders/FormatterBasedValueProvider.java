package com.haulmont.cuba.web.gui.components.valueproviders;

import com.vaadin.data.ValueProvider;

import java.util.function.Function;

public class FormatterBasedValueProvider<T> implements ValueProvider<T, String> {

    protected Function<? super T, String> formatter;

    public FormatterBasedValueProvider(Function<? super T, String> formatter) {
        this.formatter = formatter;
    }

    @Override
    public String apply(T value) {
        return formatter.apply(value);
    }

    @SuppressWarnings("unchecked")
    public Function<T, String> getFormatter() {
        return (Function<T, String>) formatter;
    }
}
