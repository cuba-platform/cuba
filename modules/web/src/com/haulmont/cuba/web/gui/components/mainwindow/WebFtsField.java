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

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.mainwindow.FtsField;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.haulmont.cuba.web.gui.components.util.ShortcutListenerDelegate;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.widgets.CubaButton;
import com.haulmont.cuba.web.widgets.CubaTextField;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;

public class WebFtsField extends WebAbstractComponent<CssLayout> implements FtsField {

    public static final String FTS_FIELD_STYLENAME = "c-ftsfield";

    protected CubaTextField searchField;
    protected Button searchBtn;

    public WebFtsField() {
        component = new CssLayout();
        component.setPrimaryStyleName(FTS_FIELD_STYLENAME);

        searchField = new CubaTextField();
        searchField.setStyleName("c-ftsfield-text");
        searchField.addShortcutListener(
                new ShortcutListenerDelegate("fts", KeyCode.ENTER, null)
                        .withHandler((sender, target) ->
                                openSearchWindow()
                        ));

        searchBtn = new CubaButton();
        searchBtn.setStyleName("c-ftsfield-button");
        searchBtn.addClickListener(event ->
                openSearchWindow()
        );

        component.addComponent(searchField);
        component.addComponent(searchBtn);

        adjustHeight();
        adjustWidth();
    }

    @Override
    public void setFrame(Frame frame) {
        super.setFrame(frame);

        AppUI ui = AppUI.getCurrent();
        if (ui != null) {
            if (ui.isTestMode()) {
                searchField.setCubaId("ftsField");
                searchBtn.setCubaId("ftsSearchBtn");
            }
            if (ui.isPerformanceTestMode()) {
                searchField.setId(ui.getTestIdManager().reserveId("ftsField"));
            }
        }
    }

    @Inject
    public void setIconResolver(IconResolver iconResolver) {
        searchBtn.setIcon(iconResolver.getIconResource("app/images/fts-button.png"));
    }

    protected void openSearchWindow() {
        String searchTerm = searchField.getValue();
        if (StringUtils.isBlank(searchTerm)) {
            return;
        }

        FtsSearchLauncher searchLauncher = beanLocator.get(FtsSearchLauncher.NAME);

        Screen frameOwner = ComponentsHelper.getWindowNN(this).getFrameOwner();
        searchLauncher.search(frameOwner, searchTerm);
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);

        adjustWidth();
    }

    @Override
    public void setHeight(String height) {
        super.setHeight(height);

        adjustHeight();
    }

    protected void adjustWidth() {
        if (getWidth() < 0) {
            searchField.setWidthUndefined();
        } else {
            searchField.setWidth(100, Sizeable.Unit.PERCENTAGE);
        }
    }

    protected void adjustHeight() {
        if (getHeight() < 0) {
            searchField.setHeightUndefined();
        } else {
            searchField.setHeight(100, Sizeable.Unit.PERCENTAGE);
        }
    }

    @Override
    public String getStyleName() {
        return StringUtils.normalizeSpace(super.getStyleName().replace(FTS_FIELD_STYLENAME, ""));
    }
}