/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.web.widgets.client.aggregation;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.TableCellElement;

public class AggregationInputFieldInfo {

    protected String oldValue;
    protected String columnKey;
    protected InputElement inputElement;
    protected boolean isFocused = false;

    protected TableCellElement td;

    public AggregationInputFieldInfo(String oldValue, String columnKey, InputElement inputElement, TableCellElement td) {
        this(oldValue, columnKey, inputElement);
        this.td = td;
    }

    public AggregationInputFieldInfo(String oldValue, String columnKey, InputElement inputElement) {
        this.oldValue = oldValue;
        this.inputElement = inputElement;
        this.columnKey = columnKey;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public InputElement getInputElement() {
        return inputElement;
    }

    public void setInputElement(InputElement inputElement) {
        this.inputElement = inputElement;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public boolean isFocused() {
        return isFocused;
    }

    public void setFocused(boolean focused) {
        this.isFocused = focused;
    }

    public TableCellElement getTd() {
        return td;
    }

    public void setTd(TableCellElement td) {
        this.td = td;
    }
}