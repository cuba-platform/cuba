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

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.FoldersService;
import com.haulmont.cuba.core.entity.AbstractSearchFolder;
import com.haulmont.cuba.core.entity.Folder;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.security.entity.Presentation;
import com.haulmont.cuba.security.entity.SearchFolder;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.gui.components.WebButton;
import com.haulmont.cuba.web.gui.components.util.ShortcutListenerDelegate;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.widgets.CubaButton;
import com.haulmont.cuba.web.widgets.CubaCheckBox;
import com.haulmont.cuba.web.widgets.CubaWindow;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.v7.ui.ComboBox;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;

public class FolderEditWindow extends CubaWindow {

    protected Folder folder;
    protected String messagesPack;
    protected TextField nameField;
    protected TextField tabNameField;
    protected ComboBox parentSelect;
    protected TextField sortOrderField;
    protected ComboBox presentation;
    protected CheckBox globalCb;
    protected CheckBox applyDefaultCb;
    protected Runnable commitHandler;
    protected VerticalLayout layout;
    protected Button okBtn;
    protected Button cancelBtn;
    protected Messages messages;
    protected UserSessionSource userSessionSource;
    protected ClientConfig clientConfig;
    protected TextField selectedPresentationField;

    protected IconResolver iconResolver = AppBeans.get(IconResolver.class);

    public FolderEditWindow(boolean adding, Folder folder, Presentations presentations, Runnable commitHandler) {
        this.folder = folder;
        this.commitHandler = commitHandler;

        messages = AppBeans.get(Messages.NAME);
        messagesPack = AppConfig.getMessagesPack();
        userSessionSource = AppBeans.get(UserSessionSource.NAME);
        Configuration configuration = AppBeans.get(Configuration.NAME);
        clientConfig = configuration.getConfig(ClientConfig.class);

        setCaption(adding ? getMessage("folders.folderEditWindow.adding") : getMessage("folders.folderEditWindow"));

        ThemeConstants theme = App.getInstance().getThemeConstants();
        setWidthUndefined();
        setResizable(false);

        addAction(
                new ShortcutListenerDelegate("commit", KeyCode.ENTER, new int[]{ModifierKey.CTRL})
                        .withHandler((sender, target) ->
                                commit()
                        ));

        layout = new VerticalLayout();
        layout.setWidthUndefined();
        layout.setSpacing(true);
        layout.setMargin(false);

        setContent(layout);
        setModal(true);
        center();

        String fieldWidth = theme.get("cuba.web.FolderEditWindow.field.width");

        nameField = new TextField();
        nameField.setRequiredIndicatorVisible(true);
        nameField.setCaption(getMessage("folders.folderEditWindow.nameField"));
        nameField.setWidth(fieldWidth);
        nameField.setValue(folder.getName());
        nameField.focus();
        layout.addComponent(nameField);

        tabNameField = new TextField();
        tabNameField.setCaption(getMessage("folders.folderEditWindow.tabNameField"));
        tabNameField.setWidth(fieldWidth);
        tabNameField.setValue(StringUtils.trimToEmpty(folder.getTabName()));
        layout.addComponent(tabNameField);

        parentSelect = new ComboBox();
        parentSelect.setCaption(getMessage("folders.folderEditWindow.parentSelect"));
        parentSelect.setWidth(fieldWidth);
        parentSelect.setNullSelectionAllowed(true);
        fillParentSelect();
        parentSelect.setValue(folder.getParent());
        layout.addComponent(parentSelect);

        if (folder instanceof SearchFolder) {
            if (presentations != null) {
                presentation = new ComboBox();
                presentation.setCaption(getMessage("folders.folderEditWindow.presentation"));
                presentation.setWidth(fieldWidth);
                presentation.setNullSelectionAllowed(true);
                fillPresentations(presentations);
                presentation.setValue(((SearchFolder) folder).getPresentation());
                layout.addComponent(presentation);
            } else if (((SearchFolder) folder).getPresentation() != null) {
                selectedPresentationField = new TextField();
                selectedPresentationField.setWidth(fieldWidth);
                selectedPresentationField.setCaption(getMessage("folders.folderEditWindow.presentation"));
                selectedPresentationField.setValue(((SearchFolder) folder).getPresentation().getName());
                selectedPresentationField.setEnabled(false);
                layout.addComponent(selectedPresentationField);
            }
        }

        sortOrderField = new TextField();
        sortOrderField.setCaption(getMessage("folders.folderEditWindow.sortOrder"));
        sortOrderField.setWidth(fieldWidth);
        sortOrderField.setValue(folder.getSortOrder() == null ? "" : folder.getSortOrder().toString());
        layout.addComponent(sortOrderField);

        if (userSessionSource.getUserSession().isSpecificPermitted("cuba.gui.searchFolder.global")
                && folder instanceof SearchFolder
                && BooleanUtils.isNotTrue(((SearchFolder) folder).getIsSet())) {
            globalCb = new CubaCheckBox(getMessage("folders.folderEditWindow.global"));
            globalCb.setValue(((SearchFolder) folder).getUser() == null);
            layout.addComponent(globalCb);
        }

        applyDefaultCb = new CubaCheckBox(getMessage("folders.folderEditWindow.applyDefault"));
        applyDefaultCb.setValue(BooleanUtils.isTrue(((AbstractSearchFolder)folder).getApplyDefault()));
        applyDefaultCb.setVisible(clientConfig.getGenericFilterManualApplyRequired()
                && folder instanceof SearchFolder
                && BooleanUtils.isNotTrue(((SearchFolder) folder).getIsSet()));
        layout.addComponent(applyDefaultCb);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setMargin(new MarginInfo(true, false, false, false));
        buttonsLayout.setSpacing(true);
        layout.addComponent(buttonsLayout);

        okBtn = new CubaButton(getMessage("actions.Ok"));
        okBtn.setIcon(iconResolver.getIconResource("icons/ok.png"));
        okBtn.addStyleName(WebButton.ICON_STYLE);

        initButtonOkListener();
        buttonsLayout.addComponent(okBtn);

        cancelBtn = new CubaButton(getMessage("actions.Cancel"));
        cancelBtn.setIcon(iconResolver.getIconResource("icons/cancel.png"));
        cancelBtn.addStyleName(WebButton.ICON_STYLE);
        cancelBtn.addClickListener(event ->
                forceClose()
        );

        buttonsLayout.addComponent(cancelBtn);

        if (AppUI.getCurrent().isTestMode()) {
            setCubaId("folderEditWindow");

            nameField.setCubaId("nameField");
            tabNameField.setCubaId("tabNameField");
            parentSelect.setCubaId("parentSelect");
            if (presentation != null) {
                presentation.setCubaId("presentationSelect");
            }
            sortOrderField.setCubaId("sortOrderField");
            if (selectedPresentationField != null) {
                selectedPresentationField.setCubaId("selectedPresentationField");
            }
            if (globalCb != null) {
                globalCb.setCubaId("globalCb");
            }
            applyDefaultCb.setCubaId("applyDefaultCb");
            okBtn.setCubaId("okBtn");
            cancelBtn.setCubaId("cancelBtn");
        }
    }

    protected void initButtonOkListener() {
        okBtn.addClickListener(event ->
                commit()
        );
    }

    protected void commit() {
        SearchFolder folder = (SearchFolder)FolderEditWindow.this.folder;
        if (StringUtils.trimToNull(nameField.getValue()) == null) {
            String msg = messages.getMainMessage("folders.folderEditWindow.emptyName");
            App.getInstance().getWindowManager().showNotification(msg, Frame.NotificationType.TRAY);
            return;
        }
        folder.setName(nameField.getValue());
        folder.setTabName(tabNameField.getValue());

        if (sortOrderField.getValue() == null || "".equals(sortOrderField.getValue())) {
            folder.setSortOrder(null);
        } else {
            String value = sortOrderField.getValue();
            int sortOrder;
            try {
                sortOrder = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                String msg = messages.getMainMessage("folders.folderEditWindow.invalidSortOrder");
                App.getInstance().getWindowManager().showNotification(msg, Frame.NotificationType.WARNING);
                return;
            }
            folder.setSortOrder(sortOrder);
        }

        Object parent = parentSelect.getValue();
        if (parent instanceof Folder)
            folder.setParent((Folder) parent);
        else
            folder.setParent(null);

        folder.setApplyDefault(Boolean.valueOf(applyDefaultCb.getValue().toString()));
        if (globalCb != null) {
            if (BooleanUtils.isTrue(globalCb.getValue())) {
                folder.setUser(null);
            } else {
                folder.setUser(userSessionSource.getUserSession().getCurrentOrSubstitutedUser());
            }
        } else {
            folder.setUser(userSessionSource.getUserSession().getCurrentOrSubstitutedUser());
        }

        if (presentation != null) {
            folder.setPresentation((Presentation) presentation.getValue());
        }

        FolderEditWindow.this.commitHandler.run();

        forceClose();
    }

    protected void fillParentSelect() {
        parentSelect.removeAllItems();

        String root = getMessage("folders.searchFoldersRoot");
        parentSelect.addItem(root);
        parentSelect.setNullSelectionItemId(root);

        FoldersService service = AppBeans.get(FoldersService.NAME);
        List<SearchFolder> list = service.loadSearchFolders();
        for (SearchFolder folder : list) {
            if (!folder.equals(this.folder)) {
                parentSelect.addItem(folder);
                parentSelect.setItemCaption(folder, folder.getCaption());
            }
        }
    }

    protected void fillPresentations(Presentations presentations) {
        presentation.removeAllItems();

        Collection<Object> availablePresentationIds = presentations.getPresentationIds();
        for (Object pId : availablePresentationIds) {
            Presentation p = presentations.getPresentation(pId);
            presentation.addItem(p);
            presentation.setItemCaption(p, presentations.getCaption(pId));
        }
    }

    protected String getMessage(String key) {
        return messages.getMainMessage(key);
    }
}