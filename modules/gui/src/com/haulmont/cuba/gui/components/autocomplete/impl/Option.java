/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.autocomplete.impl;

/**
 * @author chevelev
 * @version $Id$
 */
public class Option {
    private String value;
    private String description;

    public Option(String value, String description) {
        if (value == null)
            throw new NullPointerException("No value passed");

        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Option option = (Option) o;

        if (description != null ? !description.equals(option.description) : option.description != null) return false;
        if (!value.equals(option.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
