/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys.vcl;

/**
 * @author artamonov
 */
public interface UserSelectionHandler {

    void addUserSelectionListener(UserSelectionListener listener);
    void removeUserSelectionListener(UserSelectionListener listener);

    interface UserSelectionListener {
        void userSelectionApplied(UserSelectionHandler handler);
    }
}