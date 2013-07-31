/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.gui.components.presentations.TablePresentations;

/**
 * Interface to generalize additional functionality in {@link CubaTable}, {@link CubaGroupTable} and {@link CubaTreeTable}
 *
 * @author artamonov
 * @version $Id$
 */
public interface CubaEnhancedTable {
    TablePresentations getPresentations();

    void setPresentations(TablePresentations presentations);

    void hidePresentationsPopup();
}