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

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.mainwindow.FtsField;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.App;
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
import org.apache.commons.lang.StringUtils;

public class WebFtsField extends WebAbstractComponent<CssLayout> implements FtsField {

    public static final String FTS_FIELD_STYLENAME = "c-ftsfield";

    protected CubaTextField searchField;
    protected Button searchBtn;

    public WebFtsField() {
        component = new CssLayout();
        component.setPrimaryStyleName(FTS_FIELD_STYLENAME);

        ComponentsFactory cf = AppBeans.get(ComponentsFactory.NAME);
        com.haulmont.cuba.gui.components.TextField searchFieldComponent =
                cf.createComponent(com.haulmont.cuba.gui.components.TextField.class);
        searchField = searchFieldComponent.unwrap(CubaTextField.class);
        searchField.setStyleName("c-ftsfield-text");

        AppUI ui = AppUI.getCurrent();
        if (ui.isTestMode()) {
            searchField.setCubaId("ftsField");
            searchField.setId(ui.getTestIdManager().reserveId("ftsField"));
        }
        searchField.addShortcutListener(
                new ShortcutListenerDelegate("fts", KeyCode.ENTER, null)
                        .withHandler((sender, target) ->
                                openSearchWindow()
                        ));

        searchBtn = new CubaButton();
        searchBtn.setStyleName("c-ftsfield-button");
        searchBtn.setIcon(AppBeans.get(IconResolver.class).getIconResource("app/images/fts-button.png"));
        searchBtn.addClickListener(event ->
                openSearchWindow()
        );

        component.addComponent(searchField);
        component.addComponent(searchBtn);

        adjustHeight();
        adjustWidth();
    }

    protected void openSearchWindow() {
        String searchTerm = searchField.getValue();
        if (StringUtils.isBlank(searchTerm)) {
            return;
        }

        getFrame().openWindow("ftsSearch", OpenType.NEW_TAB,
                ParamsMap.of("searchTerm", searchTerm)
        );
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
            // todo rework - use CSS class
            ThemeConstants theme = App.getInstance().getThemeConstants();
            searchField.setWidth(theme.get("cuba.web.AppWindow.searchField.width"));
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