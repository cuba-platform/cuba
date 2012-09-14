/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.session.browse;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.GroupDatasourceImpl;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.entity.UserSessionEntity;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class UserSessionsDatasource extends GroupDatasourceImpl<UserSessionEntity, UUID> {

    public UserSessionsDatasource(DsContext dsContext, DataService dataservice, String id,
                                  MetaClass metaClass, String viewName) {
        super(dsContext, dataservice, id, metaClass, viewName);
    }

    private Date updateTs;

    @Override
    protected void loadData(Map<String, Object> params) {
        TimeSource timeSource = AppBeans.get(TimeSource.NAME);
        updateTs = timeSource.currentTimestamp();

        data.clear();
        UserSessionService uss = AppBeans.get(UserSessionService.NAME);
        Collection<UserSessionEntity> userSessionList = uss.getUserSessionInfo();
        for (UserSessionEntity entity : userSessionList) {
            data.put(entity.getId(), entity);
        }
    }

    public Date getUpdateTs() {
        return updateTs;
    }
}