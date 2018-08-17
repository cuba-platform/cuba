/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.gui.app.security.ds;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.sys.ScreensHelper;
import com.haulmont.cuba.gui.components.ScreenComponentDescriptor;
import com.haulmont.cuba.gui.data.impl.CustomHierarchicalDatasource;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class ScreenComponentsTreeDatasource extends CustomHierarchicalDatasource<ScreenComponentDescriptor, UUID> {

    protected ScreensHelper screensHelper = AppBeans.get(ScreensHelper.class);

    protected String screenId;

    public ScreenComponentsTreeDatasource() {
        this.allowCommit = false;
        this.hierarchyPropertyName = "parent";
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    protected Collection<ScreenComponentDescriptor> getEntities(Map<String, Object> params) {
        return Strings.isNullOrEmpty(screenId)
                ? Collections.emptyList()
                : screensHelper.getScreenComponents(screenId);
    }

    public String getScreenId() {
        return screenId;
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }
}
