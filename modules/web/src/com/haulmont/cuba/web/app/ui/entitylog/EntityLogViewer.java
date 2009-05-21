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

    private final String ENTITY_UUID = "parameter$entityUUID";

    public EntityLogViewer(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        checkParams(params);
        super.init(params);

        final CollectionDatasource datasource = getDsContext().get("events");
        datasource.refresh();
    }



    protected void checkParams(Map<String, Object> params) {
        final Object o = params.get(ENTITY_UUID);
        if (o == null) {
            throw new RuntimeException("param Entity UUID is null");
        }
    }
}
