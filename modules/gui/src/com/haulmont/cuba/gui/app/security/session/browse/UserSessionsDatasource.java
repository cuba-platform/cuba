/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.session.browse;

import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.security.entity.UserSessionEntity;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.chile.core.model.MetaClass;

import java.util.*;

public class UserSessionsDatasource extends CollectionDatasourceImpl<UserSessionEntity, UUID> {

    public UserSessionsDatasource(DsContext dsContext, DataService dataservice, String id, MetaClass metaClass, String viewName) {
        super(dsContext, dataservice, id, metaClass, viewName);
    }

    protected void loadData(Map<String, Object> params) {
        data.clear();
        UserSessionService uss = ServiceLocator.lookup(UserSessionService.NAME);
        Collection<UserSessionEntity> userSessionList = uss.getUserSessionInfo();
        for (UserSessionEntity entity : userSessionList) {
            data.put(entity.getId(), entity);
        }
    }
}
