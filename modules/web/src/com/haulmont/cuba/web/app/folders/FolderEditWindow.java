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

import com.vaadin.ui.*;
import com.vaadin.terminal.Sizeable;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.app.FoldersService;
import com.haulmont.cuba.core.entity.Folder;
import com.haulmont.cuba.security.entity.SearchFolder;

import java.util.List;

public class FolderEditWindow extends Window {

    private Folder folder;
    private String messagesPack;
    private TextField nameField;
    private Select parentSelect;
    private Runnable commitHandler;

    public FolderEditWindow(boolean adding, Folder folder, Runnable commitHandler) {
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

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setMargin(true, false, false, false);
        buttonsLayout.setSpacing(true);
        layout.addComponent(buttonsLayout);

        Button okBtn = new Button(getMessage("actions.Ok"));
        okBtn.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                FolderEditWindow.this.folder.setName((String) nameField.getValue());

                Object parent = parentSelect.getValue();
                if (parent instanceof Folder)
                    FolderEditWindow.this.folder.setParent((Folder) parent);
                else
                    FolderEditWindow.this.folder.setParent(null);    

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
            if (!folder.equals(this.folder)) {
                parentSelect.addItem(folder);
                parentSelect.setItemCaption(folder, folder.getName());
            }
        }
    }

    private String getMessage(String key) {
        return MessageProvider.getMessage(messagesPack, key);
    }
}
