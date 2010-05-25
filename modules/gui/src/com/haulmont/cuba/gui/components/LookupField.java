/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 16.01.2009 17:31:02
 * $Id$
 */
package com.haulmont.cuba.gui.components;

public interface LookupField extends OptionsField {
    Object getNullOption();
    void setNullOption(Object nullOption);

    FilterMode getFilterMode();
    void setFilterMode(FilterMode mode);

    boolean isNewOptionAllowed();
    void setNewOptionAllowed(boolean newOptionAllowed);

    NewOptionHandler getNewOptionHandler();
    void setNewOptionHandler(NewOptionHandler newOptionHandler);

    enum FilterMode {
            NO,
            STARTS_WITH,
            CONTAINS
    }

    public interface NewOptionHandler {
        void addNewOption(String caption);
    }

    void disablePaging();
}
