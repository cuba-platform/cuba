/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components.mainwindow;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.mainwindow.FtsField;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.toolkit.ui.CubaButton;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public class WebFtsField extends WebAbstractComponent<HorizontalLayout> implements FtsField {

    public static final String FTS_FIELD_STYLENAME = "cuba-fts-field-wrap";

    protected TextField searchField;
    protected Button searchBtn;

    public WebFtsField() {
        component = new HorizontalLayout();
        component.addStyleName(FTS_FIELD_STYLENAME);

        ComponentsFactory cf = AppBeans.get(ComponentsFactory.NAME);
        com.haulmont.cuba.gui.components.TextField searchFieldComponent =
                cf.createComponent(com.haulmont.cuba.gui.components.TextField.NAME);
        searchField = WebComponentsHelper.unwrap(searchFieldComponent);
        searchField.setStyleName("cuba-fts-field");

        AppUI ui = AppUI.getCurrent();
        if (ui.isTestMode()) {
            searchField.setCubaId("ftsField");
            searchField.setId(ui.getTestIdManager().reserveId("ftsField"));
        }
        searchField.addShortcutListener(new ShortcutListener("fts", com.vaadin.event.ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                openSearchWindow();
            }
        });

        searchBtn = new CubaButton();
        searchBtn.setStyleName("cuba-fts-button");
        searchBtn.setIcon(WebComponentsHelper.getIcon("app/images/fts-button.png"));
        searchBtn.addClickListener(
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        openSearchWindow();
                    }
                }
        );

        component.addComponent(searchField);
        component.setExpandRatio(searchField, 1);
        component.addComponent(searchBtn);

        adjustHeight();
        adjustWidth();
    }

    protected void openSearchWindow() {
        String searchTerm = searchField.getValue();
        if (StringUtils.isBlank(searchTerm)) {
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("searchTerm", searchTerm);

        getFrame().openWindow("ftsSearch", WindowManager.OpenType.NEW_TAB, params);
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
            ThemeConstants theme = App.getInstance().getThemeConstants();
            searchField.setWidth(theme.get("cuba.web.AppWindow.searchField.width"));
        } else {
            searchField.setWidth(100, Sizeable.Unit.PERCENTAGE);
        }
    }

    protected void adjustHeight() {
        if (getHeight() < 0) {
            searchField.setHeight(-1, Sizeable.Unit.PIXELS);
        } else {
            searchField.setHeight(100, Sizeable.Unit.PERCENTAGE);
        }
    }

    @Override
    public void setStyleName(String name) {
        super.setStyleName(name);

        component.addStyleName(FTS_FIELD_STYLENAME);
    }
}