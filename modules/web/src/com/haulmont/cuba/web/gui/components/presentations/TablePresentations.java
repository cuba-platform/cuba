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
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.gui.presentations.PresentationsChangeListener;
import com.haulmont.cuba.gui.sys.TestIdManager;
import com.haulmont.cuba.security.entity.Presentation;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.presentations.actions.PresentationActionsBuilder;
import com.haulmont.cuba.web.widgets.CubaButton;
import com.haulmont.cuba.web.widgets.CubaEnhancedTable;
import com.haulmont.cuba.web.widgets.CubaMenuBar;
import com.haulmont.cuba.web.widgets.CubaPopupButton;
import com.haulmont.cuba.web.widgets.CubaPopupButtonLayout;
import com.vaadin.ui.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.defaultString;

public class TablePresentations extends VerticalLayout {

    public static final String CUSTOM_STYLE_NAME_PREFIX = "cs";

    protected static final String CURRENT_MENUITEM_STYLENAME = "c-table-prefs-menuitem-current";
    protected static final String DEFAULT_MENUITEM_STYLENAME = "c-table-prefs-menuitem-default";
    protected static final String TABLE_PREFS_STYLENAME = "c-table-prefs";

    protected static final String CONTEXT_MENU_BUTTON_STYLENAME = "c-cm-button";

    protected CubaMenuBar menuBar;
    protected CubaPopupButton button;
    protected CheckBox textSelectionCheckBox;
    protected CheckBox multiLineCellsCheckBox;

    protected Table table;
    protected CubaEnhancedTable tableImpl;

    protected Map<Object, com.vaadin.ui.MenuBar.MenuItem> presentationsMenuMap;

    protected Messages messages;

    protected PresentationActionsBuilder presentationActionsBuilder;

    public TablePresentations(Table component) {
        this.table = component;
        this.messages = AppBeans.get(Messages.NAME);

        this.tableImpl = table.unwrapOrNull(CubaEnhancedTable.class);

        setMargin(false);

        setSizeUndefined();
        setStyleName(TABLE_PREFS_STYLENAME);
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

    @Override
    public String getStyleName() {
        return StringUtils.normalizeSpace(super.getStyleName().replace(TABLE_PREFS_STYLENAME, ""));
    }

    protected void removeCurrentItemStyle(com.vaadin.ui.MenuBar.MenuItem item) {
        removeStyleForItem(item, CURRENT_MENUITEM_STYLENAME);
    }

    protected void setCurrentItemStyle(com.vaadin.ui.MenuBar.MenuItem item) {
        addStyleForItem(item, CURRENT_MENUITEM_STYLENAME);
    }

    protected void removeDefaultItemStyle(com.vaadin.ui.MenuBar.MenuItem item) {
        removeStyleForItem(item, DEFAULT_MENUITEM_STYLENAME);
    }

    protected void setDefaultItemStyle(com.vaadin.ui.MenuBar.MenuItem item) {
        addStyleForItem(item, DEFAULT_MENUITEM_STYLENAME);
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
        titleLabel.setWidthUndefined();
        addComponent(titleLabel);
        setComponentAlignment(titleLabel, Alignment.MIDDLE_CENTER);

        menuBar = new CubaMenuBar();
        menuBar.setStyleName("c-table-prefs-list");
        menuBar.setWidth(100, Unit.PERCENTAGE);
        menuBar.setHeightUndefined();
        menuBar.setVertical(true);
        addComponent(menuBar);

        button = new CubaPopupButton();
        button.setCaption(messages.getMainMessage("PresentationsPopup.actions"));
        addComponent(button);
        setComponentAlignment(button, Alignment.MIDDLE_CENTER);

        textSelectionCheckBox = new CheckBox();
        textSelectionCheckBox.setCaption(messages.getMainMessage("PresentationsPopup.textSelection"));
        addComponent(textSelectionCheckBox);

        textSelectionCheckBox.setValue(tableImpl.isTextSelectionEnabled());
        textSelectionCheckBox.addValueChangeListener(e -> tableImpl.setTextSelectionEnabled(e.getValue()));

        multiLineCellsCheckBox = new CheckBox();
        multiLineCellsCheckBox.setCaption(messages.getMainMessage("PresentationsPopup.multiLineCells"));
        addComponent(multiLineCellsCheckBox);

        multiLineCellsCheckBox.setValue(tableImpl.isMultiLineCells());
        multiLineCellsCheckBox.addValueChangeListener(e -> tableImpl.setMultiLineCells(e.getValue()));
    }

    public void build() {
        button.setPopupVisible(false);
        buildPresentationsList();
        buildActions();
    }

    public void updateTextSelection() {
        textSelectionCheckBox.setValue(tableImpl.isTextSelectionEnabled());
    }

    public void updateMultiLineCells() {
        multiLineCellsCheckBox.setValue(tableImpl.isMultiLineCells());
    }

    protected void buildPresentationsList() {
        menuBar.removeItems();
        presentationsMenuMap = new HashMap<>();

        Presentations p = table.getPresentations();

        for (Object presId : p.getPresentationIds()) {
            MenuBar.MenuItem item = menuBar.addItem(
                    defaultString(p.getCaption(presId)),
                    selectedItem -> table.applyPresentation(presId)
            );
            Presentation current = p.getCurrent();
            if (current != null && presId.equals(current.getId())) {
                setCurrentItemStyle(item);
            }
            Presentation defaultPresentation = p.getDefault();
            if (defaultPresentation != null && presId.equals(defaultPresentation.getId())) {
                setDefaultItemStyle(item);
            }
            presentationsMenuMap.put(presId, item);
        }
    }

    protected void buildActions() {
        CubaPopupButtonLayout actionsContainer = new CubaPopupButtonLayout();

        PresentationActionsBuilder presentationActionsBuilder = getPresentationActionsBuilder();
        if (presentationActionsBuilder != null) {
            for (Action action : presentationActionsBuilder.build()) {
                actionsContainer.addComponent(createActionButton(action));
            }
        }

        button.setContent(actionsContainer);
    }

    protected CubaButton createActionButton(Action action) {
        CubaButton actionBtn = new CubaButton();

        actionBtn.setWidth("100%");
        actionBtn.setPrimaryStyleName(CONTEXT_MENU_BUTTON_STYLENAME);

        setPopupButtonAction(actionBtn, action);

        AppUI ui = AppUI.getCurrent();
        if (ui != null) {
            if (ui.isTestMode()) {
                actionBtn.setCubaId(action.getId());
            }

            if (ui.isPerformanceTestMode()) {
                String debugId = getDebugId();
                if (debugId != null) {
                    TestIdManager testIdManager = ui.getTestIdManager();
                    actionBtn.setId(testIdManager.getTestId(debugId + "_" + action.getId()));
                }
            }
        }

        return actionBtn;
    }

    protected void setPopupButtonAction(CubaButton actionBtn, Action action) {
        actionBtn.setCaption(action.getCaption());

        String description = action.getDescription();
        if (description == null && action.getShortcutCombination() != null) {
            description = action.getShortcutCombination().format();
        }
        if (description != null) {
            actionBtn.setDescription(description);
        }

        actionBtn.setEnabled(action.isEnabled());
        actionBtn.setVisible(action.isVisible());

        actionBtn.setClickHandler(mouseEventDetails -> {
            this.focus();

            if (button.isAutoClose()) {
                button.setPopupVisible(false);
            }

            action.actionPerform(null);
        });
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