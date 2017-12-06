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

package com.haulmont.cuba.gui.app.security.session.browse;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.data.impl.GroupDatasourceImpl;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.app.UserSessionService.Filter;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.UserSessionEntity;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class UserSessionsDatasource extends GroupDatasourceImpl<UserSessionEntity, UUID> {

    protected Date updateTs;

    protected Predicate<UserSessionEntity> sessionFilter = e -> true;

    @Override
    protected void loadData(Map<String, Object> params) {
        TimeSource timeSource = AppBeans.get(TimeSource.NAME);
        updateTs = timeSource.currentTimestamp();

        data.clear();
        if (!AppBeans.get(Security.class).isEntityOpPermitted(UserSessionEntity.class, EntityOp.READ))
            return;

        UserSessionService uss = AppBeans.get(UserSessionService.NAME);
        Collection<UserSessionEntity> userSessionList = uss.loadUserSessionEntities(createFilter(params));
        for (UserSessionEntity entity : userSessionList) {
            if (!sessionFilter.test(entity)) {
                continue;
            }
            data.put(entity.getId(), entity);
        }
    }

    protected Filter createFilter(Map<String, Object> params) {
        Filter context = Filter.create();

        Object userLoginObj = params.get("userLogin");
        if (userLoginObj != null)
            context.setUserLogin(userLoginObj.toString());

        Object userNameObj = params.get("userName");
        if (userNameObj != null)
            context.setUserName(userNameObj.toString());

        Object userAddressObj = params.get("userAddress");
        if (userAddressObj != null)
            context.setAddress(userAddressObj.toString());

        Object userClientInfoObj = params.get("userInfo");
        if (userClientInfoObj != null)
            context.setClientInfo(userClientInfoObj.toString());

        return context;
    }

    public Date getUpdateTs() {
        return updateTs;
    }

    public void setSessionFilter(Predicate<UserSessionEntity> sessionFilter) {
        this.sessionFilter = sessionFilter;
    }
}