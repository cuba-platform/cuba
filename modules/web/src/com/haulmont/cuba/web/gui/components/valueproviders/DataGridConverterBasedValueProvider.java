package com.haulmont.cuba.web.gui.components.valueproviders;

import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.gui.components.Formatter;
import com.vaadin.data.ValueProvider;

public class DataGridConverterBasedValueProvider implements ValueProvider {

    protected DataGrid.Converter converter;

    public DataGridConverterBasedValueProvider(DataGrid.Converter converter) {
        this.converter = converter;
    }

    @Override
    public Object apply(Object value) {
        //noinspection unchecked
        return converter.convertToPresentation(value, null, null);
    }

    public DataGrid.Converter getConverter() {
        return converter;
    }
}
