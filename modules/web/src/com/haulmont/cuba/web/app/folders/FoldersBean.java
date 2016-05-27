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

package com.haulmont.cuba.web.app.folders;

import com.haulmont.cuba.core.entity.AbstractSearchFolder;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.ValuePathHelper;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.impl.DsContextImplementation;
import com.haulmont.cuba.security.entity.FilterEntity;
import com.haulmont.cuba.security.entity.SearchFolder;
import com.haulmont.cuba.web.App;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component(Folders.NAME)
public class FoldersBean implements Folders {

    private static Logger log = LoggerFactory.getLogger(FoldersBean.class);

    @Inject
    protected Messages messages;

    @Inject
    protected Metadata metadata;

    @Override
    public void openFolder(AbstractSearchFolder folder) {
        if (StringUtils.isBlank(folder.getFilterComponentId())) {
            log.warn("Unable to open folder: componentId is blank");
            return;
        }

        String[] strings = ValuePathHelper.parse(folder.getFilterComponentId());
        String screenId = strings[0];

        WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
        WindowInfo windowInfo = windowConfig.getWindowInfo(screenId);

        Map<String, Object> params = new HashMap<>();

        WindowParams.DISABLE_AUTO_REFRESH.set(params, true);
        WindowParams.DISABLE_RESUME_SUSPENDED.set(params, true);

        if (!StringUtils.isBlank(folder.getTabName())) {
            WindowParams.DESCRIPTION.set(params, messages.getMainMessage(folder.getTabName()));
        } else {
            WindowParams.DESCRIPTION.set(params, messages.getMainMessage(folder.getName()));
        }

        WindowParams.FOLDER_ID.set(params, folder.getId());

        com.haulmont.cuba.gui.components.Window window = App.getInstance().getWindowManager().openWindow(windowInfo,
                OpenType.NEW_TAB, params);

        Filter filterComponent = null;

        if (strings.length > 1) {
            String filterComponentId = StringUtils.join(Arrays.copyOfRange(strings, 1, strings.length), '.');

            filterComponent = (Filter) window.getComponentNN(filterComponentId);

            FilterEntity filterEntity = metadata.create(FilterEntity.class);
            filterEntity.setFolder(folder);
            filterEntity.setComponentId(folder.getFilterComponentId());
            filterEntity.setName(folder.getLocName());

            filterEntity.setXml(folder.getFilterXml());
            filterEntity.setApplyDefault(BooleanUtils.isNotFalse(folder.getApplyDefault()));
            if (folder instanceof SearchFolder) {
                filterEntity.setIsSet(((SearchFolder) folder).getIsSet());
            }
            filterComponent.setFilterEntity(filterEntity);
        }

        if (filterComponent != null && folder instanceof SearchFolder) {
            final SearchFolder searchFolder = (SearchFolder) folder;
            if (searchFolder.getPresentation() != null) {
                ((com.haulmont.cuba.gui.components.Component.HasPresentations) filterComponent.getApplyTo())
                        .applyPresentation(searchFolder.getPresentation().getId());
            }
        }

        ((DsContextImplementation) window.getDsContext()).resumeSuspended();
    }
}