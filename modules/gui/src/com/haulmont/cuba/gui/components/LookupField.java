/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components;

/**
 * @author abramov
 * @version $Id$
 */
public interface LookupField extends OptionsField {

    String NAME = "lookupField";

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