/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.security.history;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.security.entity.ScreenHistoryEntity;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author kovalenko
 * @version $Id$
 */
public class ScreenHistoryDatasource extends CollectionDatasourceImpl<ScreenHistoryEntity, UUID> {

    @Override
    protected void loadData(Map<String, Object> params) {
        UserSession userSession = AppBeans.get(UserSessionSource.class).getUserSession();
        User user = null;
        if(userSession.getSubstitutedUser() != null) {
            user = userSession.getSubstitutedUser();
        } else {
            user = userSession.getUser();
        }
        Map<String, Object> modifiedParams = new HashMap<>();
        modifiedParams.putAll(params);
        modifiedParams.put("userId", user);
        super.loadData(modifiedParams);
    }
}
