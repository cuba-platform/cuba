/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maksim Tulupov
 * Created: 19.05.2009 16:53:54
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.entitylog;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.EntityLogItem;

import java.util.*;
import java.util.List;

public class EntityLogViewer extends AbstractWindow {

    public EntityLogViewer(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        super.init(params);

        final CollectionDatasource datasource = getDsContext().get("events");
        datasource.refresh();
    }
}
