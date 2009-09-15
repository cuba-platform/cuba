/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: DEGTYARJOV EUGENIY
 * Created: 14.09.2009 12:36:59
 *
 * $Id$
 */

package com.haulmont.cuba.web.app.ui.security.session.browse;

import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.security.entity.UserSessionEntity;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.chile.core.model.MetaClass;

import java.util.*;

public class SessionsCollectionDataSouce extends CollectionDatasourceImpl<UserSessionEntity, UUID> {
    public SessionsCollectionDataSouce(DsContext dsContext, DataService dataservice, String id, MetaClass metaClass, String viewName) {
        super(dsContext, dataservice, id, metaClass, viewName);
    }

    protected void loadData(Map<String, Object> params) {
        data.clear();
        UserSessionService uss = Locator.lookupLocal(UserSessionService.JNDI_NAME);
        Collection<UserSessionEntity> userSessionList = uss.getUserSessionInfo();
        for (UserSessionEntity entity : userSessionList) {
            data.put(entity.getId(), entity);
        }
    }
}
