/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Gorodnov
 * Created: 25.03.2009 18:07:01
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.itmill.toolkit.ui.AbstractSelect;
import com.itmill.toolkit.ui.Component;
import com.itmill.toolkit.data.Container;

import java.util.Collection;

public abstract class TableSupport
        extends AbstractSelect
{
    protected TableSupport() {
    }

    protected TableSupport(String caption) {
        super(caption);
    }

    protected TableSupport(String caption, Container dataSource) {
        super(caption, dataSource);
    }

    protected TableSupport(String caption, Collection options) {
        super(caption, options);
    }

    public interface ColumnGenerator
    {
        public Component generateCell(
                TableSupport source,
                Object itemId,
                Object columnId
        );
    }
}
