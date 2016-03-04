/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.ui.ListSelect;

/**
 * @author petunin
 */
public class CubaListSelect extends ListSelect {

    public CubaListSelect() {
        setValidationVisible(false);
        setShowBufferedSourceException(false);
    }
}