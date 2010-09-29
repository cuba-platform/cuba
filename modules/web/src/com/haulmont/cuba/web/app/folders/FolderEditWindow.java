/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.12.2009 18:12:21
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.folders;

import com.haulmont.cuba.core.app.FoldersService;
import com.haulmont.cuba.core.entity.Folder;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.UserSessionClient;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.security.entity.Presentation;
import com.haulmont.cuba.security.entity.SearchFolder;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import org.apache.commons.lang.BooleanUtils;

import java.util.Collection;
import java.util.List;

public class FolderEditWindow extends Window {

    private Folder folder;
    private String messagesPack;
    private TextField nameField;
    private Select parentSelect;
    private TextField sortOrderField;
    private Select presentation;
    private CheckBox globalCb;
    private Runnable commitHandler;

    public FolderEditWindow(boolean adding, Folder folder, Presentations presentations, Runnable commitHandler) {
        super();
        this.folder = folder;
        this.commitHandler = commitHandler;

        messagesPack = AppConfig.getInstance().getMessagesPack();
        setCaption(adding ? getMessage("folders.folderEditWindow.adding") : getMessage("folders.folderEditWindow"));

        setWidth(300, Sizeable.UNITS_PIXELS);

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        setContent(layout);
        center();

        nameField = new TextField();
        nameField.setCaption(getMessage("folders.folderEditWindow.nameField"));
        nameField.setWidth(250, Sizeable.UNITS_PIXELS);
        nameField.setValue(folder.getName());
        layout.addComponent(nameField);

        parentSelect = new Select();
        parentSelect.setCaption(getMessage("folders.folderEditWindow.parentSelect"));
        parentSelect.setWidth(250, Sizeable.UNITS_PIXELS);
        parentSelect.setNullSelectionAllowed(true);
        fillParentSelect();
        parentSelect.setValue(folder.getParent());
        layout.addComponent(parentSelect);

        if (folder instanceof SearchFolder) {
            if (presentations != null) {
                presentation = new Select();
                presentation.setCaption(getMessage("folders.folderEditWindow.presentation"));
                presentation.setWidth("250px");
                presentation.setNullSelectionAllowed(true);
                fillPresentations(presentations);
                presentation.setValue(((SearchFolder) folder).getPresentation());
                layout.addComponent(presentation);
            } else if (((SearchFolder) folder).getPresentation() != null) {
                final TextField selectedPresentation = new TextField();
                selectedPresentation.setWidth("250px");
                selectedPresentation.setCaption(getMessage("folders.folderEditWindow.presentation"));
                selectedPresentation.setValue(((SearchFolder) folder).getPresentation().getName());
                selectedPresentation.setEnabled(false);
                layout.addComponent(selectedPresentation);
            }
        }

        sortOrderField = new TextField();
        sortOrderField.setCaption(getMessage("folders.folderEditWindow.sortOrder"));
        sortOrderField.setWidth(250, Sizeable.UNITS_PIXELS);
        sortOrderField.setValue(folder.getSortOrder() == null ? "" : folder.getSortOrder());
        layout.addComponent(sortOrderField);

        if (UserSessionClient.getUserSession().isSpecificPermitted("cuba.gui.searchFolder.global")
                && folder instanceof SearchFolder)
        {
            globalCb = new CheckBox(getMessage("folders.folderEditWindow.global"));
            globalCb.setValue(((SearchFolder) folder).getUser() == null);
            layout.addComponent(globalCb);
        }

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setMargin(true, false, false, false);
        buttonsLayout.setSpacing(true);
        layout.addComponent(buttonsLayout);

        Button okBtn = new Button(getMessage("actions.Ok"));
        okBtn.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                FolderEditWindow.this.folder.setName((String) nameField.getValue());

                if (sortOrderField.getValue() == null || "".equals(sortOrderField.getValue())) {
                    FolderEditWindow.this.folder.setSortOrder(null);
                } else {
                    Object value = sortOrderField.getValue();
                    int sortOrder;
                    if (value instanceof Integer)
                        sortOrder = (Integer) value;
                    else
                        try {
                            sortOrder = Integer.parseInt((String) value);
                        } catch (NumberFormatException e) {
                            String msg = MessageProvider.getMessage(messagesPack, "folders.folderEditWindow.invalidSortOrder");
                            showNotification(msg, Notification.TYPE_WARNING_MESSAGE);
                            return;
                        }
                    FolderEditWindow.this.folder.setSortOrder(sortOrder);
                }

                Object parent = parentSelect.getValue();
                if (parent instanceof Folder)
                    FolderEditWindow.this.folder.setParent((Folder) parent);
                else
                    FolderEditWindow.this.folder.setParent(null);

                if (globalCb != null) {
                    if (BooleanUtils.isTrue((Boolean) globalCb.getValue()))
                        ((SearchFolder) FolderEditWindow.this.folder).setUser(null);
                    else
                        ((SearchFolder) FolderEditWindow.this.folder).setUser(UserSessionClient.getUserSession().getCurrentOrSubstitutedUser());
                }

                if (presentation != null) {
                    ((SearchFolder) FolderEditWindow.this.folder).setPresentation((Presentation) presentation.getValue());
                }

                FolderEditWindow.this.commitHandler.run();

                close();
            }
        });
        buttonsLayout.addComponent(okBtn);

        Button cancelBtn = new Button(getMessage("actions.Cancel"));
        cancelBtn.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        buttonsLayout.addComponent(cancelBtn);
    }

    private void fillParentSelect() {
        parentSelect.removeAllItems();

        String root = getMessage("folders.searchFoldersRoot");
        parentSelect.addItem(root);
        parentSelect.setNullSelectionItemId(root);

        FoldersService service = ServiceLocator.lookup(FoldersService.JNDI_NAME);
        List<SearchFolder> list = service.loadSearchFolders();
        for (SearchFolder folder : list) {
            if (!folder.equals(this.folder) && folder.getCode() == null) {
                parentSelect.addItem(folder);
                parentSelect.setItemCaption(folder, folder.getName());
            }
        }
    }

    private void fillPresentations(Presentations presentations) {
        presentation.removeAllItems();

        final Collection<Object> availablePresentationIds = presentations.getPresentationIds();
        for (final Object pId : availablePresentationIds) {
            final Presentation p = presentations.getPresentation(pId);
            presentation.addItem(p);
            presentation.setItemCaption(p, presentations.getCaption(pId));
        }
    }

    private String getMessage(String key) {
        return MessageProvider.getMessage(messagesPack, key);
    }
}
