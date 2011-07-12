/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class ItemWrapper<T> {
    private T item;
    private String caption;

    public ItemWrapper(T item, String caption) {
        this.item = item;
        this.caption = caption;
    }

    public T getItem() {
        return item;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String toString() {
        return caption;
    }
}
