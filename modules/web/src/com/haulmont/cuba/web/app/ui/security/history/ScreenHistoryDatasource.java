/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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
 */
public class ScreenHistoryDatasource extends CollectionDatasourceImpl<ScreenHistoryEntity, UUID> {

    @Override
    protected void loadData(Map<String, Object> params) {
        UserSession userSession = AppBeans.get(UserSessionSource.class).getUserSession();
        User user;
        if (userSession.getSubstitutedUser() != null) {
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
