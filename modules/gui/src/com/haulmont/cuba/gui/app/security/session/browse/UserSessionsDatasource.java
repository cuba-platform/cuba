/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.session.browse;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.TimeSource;
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

    private Date updateTs;

    @Override
    protected void loadData(Map<String, Object> params) {
        TimeSource timeSource = AppBeans.get(TimeSource.NAME);
        updateTs = timeSource.currentTimestamp();

        data.clear();
        UserSessionService uss = AppBeans.get(UserSessionService.NAME);
        Collection<UserSessionEntity> userSessionList = uss.getUserSessionInfo();
        for (UserSessionEntity entity : userSessionList) {
            Object userLoginObj = params.get("userLogin");
            if (userLoginObj != null && (entity.getLogin() == null || !entity.getLogin().toLowerCase().contains(userLoginObj.toString())))
                continue;
            Object userNameObj = params.get("userName");
            if (userNameObj != null && (entity.getUserName() == null || !entity.getUserName().toLowerCase().contains(userNameObj.toString())))
                continue;
            Object userAddressObj = params.get("userAddress");
            if (userAddressObj != null && (entity.getAddress() == null || !entity.getAddress().toLowerCase().contains(userAddressObj.toString())))
                continue;
            Object userClientInfoObj = params.get("userInfo");
            if (userClientInfoObj != null && (entity.getClientInfo() == null || !entity.getClientInfo().toLowerCase().contains(userClientInfoObj.toString())))
                continue;
            data.put(entity.getId(), entity);
        }
    }

    public Date getUpdateTs() {
        return updateTs;
    }
}