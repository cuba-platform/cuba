/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
