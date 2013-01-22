/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.app.ui.entitylog;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.security.entity.EntityLogAttr;
import com.haulmont.cuba.security.entity.EntityLogAttrWrapper;
import com.haulmont.cuba.security.entity.EntityLogItem;

import java.util.Map;
import java.util.UUID;

/**
 * @author Tulupov
 * @version $Id$
 */
public class EntityLogDatasource extends CollectionDatasourceImpl {

    @Override
    protected void loadData(Map params) {
        for (Object entity : data.values()) {
            detachListener((Instance) entity);
        }
        data.clear();

        CollectionDatasource eventsDs = getDsContext().get("events");
        EntityLogItem logItem = (EntityLogItem) eventsDs.getItem();
        CollectionDatasource ds = getDsContext().get("values");
        EntityLogAttrWrapper wr;
        for (Object id : ds.getItemIds()) {
            EntityLogAttr attr = (EntityLogAttr) ds.getItem(id);
            if (attr.getValueId() != null && logItem != null) {
                try {
                    wr = new EntityLogAttrWrapper(attr, loadEntity(Class.forName(logItem.getEntity()), attr.getName(), attr.getValueId()));
                } catch (ClassNotFoundException e) {
                    wr = new EntityLogAttrWrapper(attr);
                }
            } else {
                wr = new EntityLogAttrWrapper(attr);
            }
            data.put(wr.getId(), wr);
        }
    }

    protected BaseUuidEntity loadEntity(Class parentClass, String property, UUID id) {
        MetaClass aClass = metadata.getSession().getClassNN(parentClass).getPropertyNN(property).getRange().asClass();
        LoadContext lc = new LoadContext(aClass).setId(id);
        lc.setSoftDeletion(false);
        return getDataSupplier().load(lc);
    }
}
