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

package com.haulmont.cuba.web.gui.components.mainwindow;

import com.haulmont.cuba.core.entity.AbstractSearchFolder;
import com.haulmont.cuba.core.entity.AppFolder;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.mainwindow.FoldersPane;
import com.haulmont.cuba.security.entity.SearchFolder;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.app.folders.CubaFoldersPane;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.vaadin.server.Resource;
import com.vaadin.ui.IconGenerator;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Element;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

import static com.haulmont.cuba.web.app.folders.CubaFoldersPane.C_FOLDERS_PANE;

public class WebFoldersPane extends WebAbstractComponent<CubaFoldersPane> implements FoldersPane {

    protected static final IconGenerator<AbstractSearchFolder> NULL_ITEM_ICON_GENERATOR = item -> null;

    protected final IconGenerator<AbstractSearchFolder> ICON_GENERATOR = this::getFolderIcon;
    protected final IconGenerator<AbstractSearchFolder> DEFAULT_ICON_GENERATOR = this::getDefaultFolderIcon;

    protected Function<AbstractSearchFolder, String> iconProvider;

    protected boolean settingsEnabled = true;

    public WebFoldersPane() {
        component = createComponent();

        component.addRefreshFoldersListener(event -> setupIconGenerator());
    }

    protected CubaFoldersPane createComponent() {
        return new CubaFoldersPane();
    }

    @Override
    public void setStyleName(String styleName) {
        super.setStyleName(styleName);

        component.addStyleName(C_FOLDERS_PANE);
    }

    @Override
    public String getStyleName() {
        return StringUtils.normalizeSpace(super.getStyleName().replace(C_FOLDERS_PANE, ""));
    }

    @Override
    public void setFrame(Frame frame) {
        super.setFrame(frame);

        component.setFrame(frame);
    }

    @Override
    public void loadFolders() {
        component.loadFolders();
        setupIconGenerator();
    }

    protected void setupIconGenerator() {
        WebConfig webConfig = beanLocator.get(Configuration.class)
                .getConfig(WebConfig.class);

        IconGenerator<AbstractSearchFolder> iconGenerator = !webConfig.getShowFolderIcons()
                ? NULL_ITEM_ICON_GENERATOR
                : iconProvider == null
                    ? DEFAULT_ICON_GENERATOR
                    : ICON_GENERATOR;

        component.setIconGenerator(iconGenerator);
    }

    @Override
    public void refreshFolders() {
        component.refreshFolders();
    }

    @Override
    public void setFolderIconProvider(Function<AbstractSearchFolder, String> iconProvider) {
        if (this.iconProvider != iconProvider) {
            this.iconProvider = iconProvider;

            setupIconGenerator();
        }
    }

    @Override
    public Function<AbstractSearchFolder, String> getFolderIconProvider() {
        return iconProvider;
    }

    protected Resource getDefaultFolderIcon(AbstractSearchFolder item) {
        String resourceId;
        if (item instanceof AppFolder) {
            resourceId = "icons/app-folder-small.png";
        } else if (item instanceof SearchFolder) {
            SearchFolder folder = (SearchFolder) item;
            resourceId = BooleanUtils.isTrue(folder.getIsSet())
                    ? "icons/set-small.png"
                    : "icons/search-folder-small.png";
        } else {
            return null;
        }
        return getIconResource(resourceId);
    }

    protected Resource getFolderIcon(AbstractSearchFolder item) {
        String resourceId;
        try {
            resourceId = iconProvider.apply(item);
        } catch (Exception e) {
            LoggerFactory.getLogger(WebFoldersPane.class)
                    .warn("Error invoking iconProvider apply method", e);
            return null;
        }
        return getIconResource(resourceId);
    }

    @Override
    public void applySettings(Element element) {
        if (!isSettingsEnabled()) {
            return;
        }

        String verticalSplitPos = element.attributeValue("splitPosition");
        if (StringUtils.isNotEmpty(verticalSplitPos)
                && NumberUtils.isCreatable(verticalSplitPos)) {
            component.setVerticalSplitPosition(Float.parseFloat(verticalSplitPos));
        }
    }

    @Override
    public boolean saveSettings(Element element) {
        if (!isSettingsEnabled()) {
            return false;
        }

        float verticalSplitPos = component.getVerticalSplitPosition();
        element.addAttribute("splitPosition", String.valueOf(verticalSplitPos));
        return true;
    }

    @Override
    public boolean isSettingsEnabled() {
        return settingsEnabled;
    }

    @Override
    public void setSettingsEnabled(boolean settingsEnabled) {
        this.settingsEnabled = settingsEnabled;
    }
}