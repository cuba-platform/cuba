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
package com.haulmont.cuba.web.gui.components.presentations;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.gui.presentations.PresentationsChangeListener;
import com.haulmont.cuba.security.entity.Presentation;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.WebPopupButton;
import com.haulmont.cuba.web.gui.components.presentations.actions.PresentationActionsBuilder;
import com.haulmont.cuba.web.toolkit.ui.CubaEnhancedTable;
import com.haulmont.cuba.web.toolkit.ui.CubaMenuBar;
import com.vaadin.data.Property;
import com.vaadin.data.util.AbstractProperty;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TablePresentations extends VerticalLayout {

    public static final String CUSTOM_STYLE_NAME_PREFIX = "cs";
    protected static final String MENUITEM_STYLE_CURRENT = "c-table-prefs-menuitem-current";
    protected static final String MENUITEM_STYLE_DEFAULT = "c-table-prefs-menuitem-default";

    protected CubaMenuBar menuBar;
    protected WebPopupButton button;
    protected CheckBox textSelectionCheckBox;

    protected Table table;
    protected CubaEnhancedTable tableImpl;

    protected Map<Object, com.vaadin.ui.MenuBar.MenuItem> presentationsMenuMap;

    protected Messages messages;

    protected PresentationActionsBuilder presentationActionsBuilder;

    public TablePresentations(Table component) {
        this.table = component;
        this.messages = AppBeans.get(Messages.NAME);

        this.tableImpl = (CubaEnhancedTable) WebComponentsHelper.unwrap(table);

        setSizeUndefined();
        setStyleName("c-table-prefs");
        setParent((HasComponents) WebComponentsHelper.unwrap(component));

        initLayout();

        table.getPresentations().addListener(new PresentationsChangeListener() {
            @Override
            public void currentPresentationChanged(Presentations presentations, Object oldPresentationId) {
                table.getPresentations().commit();
                if (presentationsMenuMap != null) {
                    // simple change current item
                    if (oldPresentationId != null) {
                        if (oldPresentationId instanceof Presentation)
                            oldPresentationId = ((Presentation) oldPresentationId).getId();

                        com.vaadin.ui.MenuBar.MenuItem lastMenuItem = presentationsMenuMap.get(oldPresentationId);
                        if (lastMenuItem != null)
                            removeCurrentItemStyle(lastMenuItem);
                    }

                    Presentation current = presentations.getCurrent();
                    if (current != null) {
                        com.vaadin.ui.MenuBar.MenuItem menuItem = presentationsMenuMap.get(current.getId());
                        if (menuItem != null)
                            setCurrentItemStyle(menuItem);
                    }

                    buildActions();
                }
            }

            @Override
            public void presentationsSetChanged(Presentations presentations) {
                build();
            }

            @Override
            public void defaultPresentationChanged(Presentations presentations, Object oldPresentationId) {
                if (presentationsMenuMap != null) {
                    if (oldPresentationId != null) {
                        if (oldPresentationId instanceof Presentation)
                            oldPresentationId = ((Presentation) oldPresentationId).getId();

                        com.vaadin.ui.MenuBar.MenuItem lastMenuItem = presentationsMenuMap.get(oldPresentationId);
                        if (lastMenuItem != null)
                            removeDefaultItemStyle(lastMenuItem);
                    }

                    Presentation defaultPresentation = presentations.getDefault();
                    if (defaultPresentation != null) {
                        com.vaadin.ui.MenuBar.MenuItem menuItem = presentationsMenuMap.get(defaultPresentation.getId());
                        if (menuItem != null)
                            setDefaultItemStyle(menuItem);
                    }
                }
            }
        });

        build();
    }

    protected void removeCurrentItemStyle(com.vaadin.ui.MenuBar.MenuItem item) {
        removeStyleForItem(item, MENUITEM_STYLE_CURRENT);
    }

    protected void setCurrentItemStyle(com.vaadin.ui.MenuBar.MenuItem item) {
        addStyleForItem(item, MENUITEM_STYLE_CURRENT);
    }

    protected void removeDefaultItemStyle(com.vaadin.ui.MenuBar.MenuItem item) {
        removeStyleForItem(item, MENUITEM_STYLE_DEFAULT);
    }

    protected void setDefaultItemStyle(com.vaadin.ui.MenuBar.MenuItem item) {
        addStyleForItem(item, MENUITEM_STYLE_DEFAULT);
        item.setDescription(messages.getMainMessage("PresentationsPopup.defaultPresentation"));
    }

    protected void addStyleForItem(com.vaadin.ui.MenuBar.MenuItem item, String styleName) {
        List<String> styles = new ArrayList<>();
        String style = item.getStyleName();
        if (style != null) {
            CollectionUtils.addAll(styles, style.split(" "));
        }
        if (!styles.contains(styleName)) {
            styles.add(styleName);
        }
        applyStylesForItem(item, styles);
    }

    protected void removeStyleForItem(com.vaadin.ui.MenuBar.MenuItem item, String styleName) {
        String style = item.getStyleName();
        if (style != null) {
            List<String> styles = new ArrayList<>();
            CollectionUtils.addAll(styles, style.split(" "));
            styles.remove(styleName);
            applyStylesForItem(item, styles);
        }
    }

    protected void applyStylesForItem(com.vaadin.ui.MenuBar.MenuItem item, List<String> styles) {
        styles.remove(CUSTOM_STYLE_NAME_PREFIX);
        StringBuilder joinedStyle = new StringBuilder(CUSTOM_STYLE_NAME_PREFIX);
        for (String style : styles) {
            joinedStyle.append(" ").append(style);
        }
        item.setStyleName(joinedStyle.toString());
    }

    protected void initLayout() {
        setSpacing(true);

        Label titleLabel = new Label(messages.getMainMessage("PresentationsPopup.title"));
        titleLabel.setStyleName("c-table-prefs-title");
        titleLabel.setWidth("-1px");
        addComponent(titleLabel);
        setComponentAlignment(titleLabel, Alignment.MIDDLE_CENTER);

        menuBar = new CubaMenuBar();
        menuBar.setStyleName("c-table-prefs-list");
        menuBar.setWidth("100%");
        menuBar.setHeight("-1px");
        menuBar.setVertical(true);
        addComponent(menuBar);

        button = new WebPopupButton();
        button.setCaption(messages.getMainMessage("PresentationsPopup.actions"));
        addComponent(button.<Component>getComponent());
        setComponentAlignment(button.<Component>getComponent(), Alignment.MIDDLE_CENTER);

        textSelectionCheckBox = new CheckBox();
        textSelectionCheckBox.setImmediate(true);
        textSelectionCheckBox.setInvalidCommitted(true);
        textSelectionCheckBox.setCaption(messages.getMainMessage("PresentationsPopup.textSelection"));
        addComponent(textSelectionCheckBox);
        textSelectionCheckBox.setPropertyDataSource(new AbstractProperty() {
            @Override
            public Object getValue() {
                return tableImpl.isTextSelectionEnabled();
            }

            @Override
            public void setValue(Object newValue) throws Property.ReadOnlyException {
                if (newValue instanceof Boolean) {
                    tableImpl.setTextSelectionEnabled((Boolean) newValue);
                }
            }

            @Override
            public Class getType() {
                return Boolean.class;
            }
        });
    }

    public void build() {
        button.setPopupVisible(false);
        buildPresentationsList();
        buildActions();
    }

    public void updateTextSelection() {
        textSelectionCheckBox.setValue(tableImpl.isTextSelectionEnabled());
    }

    protected void buildPresentationsList() {
        menuBar.removeItems();
        presentationsMenuMap = new HashMap<>();

        final Presentations p = table.getPresentations();

        for (final Object presId : p.getPresentationIds()) {
            final MenuBar.MenuItem item = menuBar.addItem(
                    StringUtils.defaultString(p.getCaption(presId)),
                    new com.vaadin.ui.MenuBar.Command() {
                        @Override
                        public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
                            table.applyPresentation(presId);
                        }
                    }
            );
            final Presentation current = p.getCurrent();
            if (current != null && presId.equals(current.getId())) {
                setCurrentItemStyle(item);
            }
            final Presentation defaultPresentation = p.getDefault();
            if (defaultPresentation != null && presId.equals(defaultPresentation.getId())) {
                setDefaultItemStyle(item);
            }
            presentationsMenuMap.put(presId, item);
        }
    }

    protected void buildActions() {
        button.removeAllActions();

        PresentationActionsBuilder presentationActionsBuilder = getPresentationActionsBuilder();
        if (presentationActionsBuilder != null)
            for (AbstractAction action : presentationActionsBuilder.build())
                button.addAction(action);
    }

    protected PresentationActionsBuilder getPresentationActionsBuilder() {
        if (presentationActionsBuilder == null)
            presentationActionsBuilder = new PresentationActionsBuilder(table);
        return presentationActionsBuilder;
    }

    public void setPresentationActionsBuilder(PresentationActionsBuilder presentationActionsBuilder) {
        this.presentationActionsBuilder = presentationActionsBuilder;
    }

    protected String getMessage(String key) {
        return messages.getMessage(getClass(), key);
    }
}