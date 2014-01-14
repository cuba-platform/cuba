/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.folders;

import com.haulmont.cuba.core.entity.AbstractSearchFolder;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.ValuePathHelper;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.impl.DsContextImplementation;
import com.haulmont.cuba.gui.settings.SettingsImpl;
import com.haulmont.cuba.security.entity.FilterEntity;
import com.haulmont.cuba.security.entity.SearchFolder;
import com.haulmont.cuba.web.App;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gorbunkov
 * @version $Id$
 */
@ManagedBean(Folders.NAME)
public class FoldersBean implements Folders {

    private static Log log = LogFactory.getLog(FoldersBean.class);

    @Inject
    protected Messages messages;

    @Override
    public void openFolder(AbstractSearchFolder folder) {
        if (StringUtils.isBlank(folder.getFilterComponentId())) {
            log.warn("Unable to open folder: componentId is blank");
            return;
        }

        String[] strings = ValuePathHelper.parse(folder.getFilterComponentId());
        String screenId = strings[0];

        WindowInfo windowInfo = AppBeans.get(WindowConfig.class).getWindowInfo(screenId);

        Map<String, Object> params = new HashMap<>();

        WindowParams.DISABLE_AUTO_REFRESH.set(params, true);
        WindowParams.DISABLE_APPLY_SETTINGS.set(params, true);
        WindowParams.DISABLE_RESUME_SUSPENDED.set(params, true);

        if (!StringUtils.isBlank(folder.getTabName())) {
            WindowParams.DESCRIPTION.set(params, messages.getMainMessage(folder.getTabName()));
        } else {
            WindowParams.DESCRIPTION.set(params, messages.getMainMessage(folder.getName()));
        }

        WindowParams.FOLDER_ID.set(params, folder.getId());

        com.haulmont.cuba.gui.components.Window window = App.getInstance().getWindowManager().openWindow(windowInfo,
                WindowManager.OpenType.NEW_TAB, params);

        Filter filterComponent = null;

        if (strings.length > 1) {
            String filterComponentId = StringUtils.join(Arrays.copyOfRange(strings, 1, strings.length), '.');

            filterComponent = window.getComponent(filterComponentId);

            FilterEntity filterEntity = new FilterEntity();
            filterEntity.setFolder(folder);
            filterEntity.setComponentId(folder.getFilterComponentId());
            filterEntity.setName(folder.getLocName());
            filterEntity.setCode(folder.getName());

            filterEntity.setXml(folder.getFilterXml());
            filterEntity.setApplyDefault(BooleanUtils.isNotFalse(folder.getApplyDefault()));
            if (folder instanceof SearchFolder) {
                filterEntity.setIsSet(((SearchFolder) folder).getIsSet());
            }
            filterComponent.setFilterEntity(filterEntity);
        }
        window.applySettings(new SettingsImpl(window.getId()));

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
