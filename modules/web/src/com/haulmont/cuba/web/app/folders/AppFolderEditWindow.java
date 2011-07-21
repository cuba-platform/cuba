/*
* Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
* Haulmont Technology proprietary and confidential.
* Use is subject to license terms.

* Author: Konstantin Devyatkin
* Created: 01.03.11 12:39
*
* $Id$
*/
package com.haulmont.cuba.web.app.folders;

import com.haulmont.cuba.core.app.FoldersService;
import com.haulmont.cuba.core.entity.AppFolder;
import com.haulmont.cuba.core.entity.Folder;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import org.apache.commons.lang.StringUtils;

import java.util.List;


public class AppFolderEditWindow extends FolderEditWindow {
    private TextField visibilityScriptField = null;
    private TextField quantityScriptField = null;

    public static FolderEditWindow create(boolean isAppFolder, boolean adding, Folder folder, Presentations presentations, Runnable commitHandler) {
        if (isAppFolder) {
            return new AppFolderEditWindow(adding, folder, presentations, commitHandler);
        } else
            return new FolderEditWindow(adding, folder, presentations, commitHandler);
    }


    public AppFolderEditWindow(boolean adding, Folder folder, Presentations presentations, Runnable commitHandler) {
        super(adding, folder, presentations, commitHandler);
        if (!adding) {
            setWidth(500, Sizeable.UNITS_PIXELS);
            visibilityScriptField = new TextField();
            visibilityScriptField.setRows(10);
            visibilityScriptField.setColumns(40);
            visibilityScriptField.setCaption(getMessage("folders.visibilityScript"));
            String vScript = StringUtils.trimToEmpty(((AppFolder) folder).getVisibilityScript());
            visibilityScriptField.setValue(vScript);
            layout.addComponent(visibilityScriptField, 3);

            quantityScriptField = new TextField();
            String qScript = StringUtils.trimToEmpty(((AppFolder) folder).getQuantityScript());
            quantityScriptField.setValue(qScript);
            quantityScriptField.setRows(10);
            quantityScriptField.setColumns(40);
            quantityScriptField.setCaption(getMessage("folders.quantityScript"));
            layout.addComponent(quantityScriptField, 4);


        }
    }

    protected void initButtonOkListener() {
        okBtn.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                AppFolder folder = (AppFolder) AppFolderEditWindow.this.folder;
                if (StringUtils.trimToNull((String) nameField.getValue()) == null) {
                    String msg = MessageProvider.getMessage(messagesPack, "folders.folderEditWindow.emptyName");
                    showNotification(msg, Notification.TYPE_TRAY_NOTIFICATION);
                    return;
                }
                folder.setName((String) nameField.getValue());
                folder.setTabName((String) tabNameField.getValue());

                if (sortOrderField.getValue() == null || "".equals(sortOrderField.getValue())) {
                    folder.setSortOrder(null);
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
                    folder.setSortOrder(sortOrder);
                }

                Object parent = parentSelect.getValue();
                if (parent instanceof Folder)
                    folder.setParent((Folder) parent);
                else
                    folder.setParent(null);


                if (visibilityScriptField != null) {
                    String scriptText = (String) visibilityScriptField.getValue();
                    folder.setVisibilityScript(scriptText);
                }
                if (quantityScriptField != null) {
                    String scriptText = (String) quantityScriptField.getValue();
                    folder.setQuantityScript(scriptText);
                }
                folder.setApplyDefault(Boolean.valueOf(applyDefaultCb.getValue().toString()));

                AppFolderEditWindow.this.commitHandler.run();

                close();
            }
        });
    }

    protected void fillParentSelect() {
        parentSelect.removeAllItems();

        String root = getMessage("folders.appFoldersRoot");
        parentSelect.addItem(root);
        parentSelect.setNullSelectionItemId(root);

        FoldersService service = ServiceLocator.lookup(FoldersService.JNDI_NAME);
        List<AppFolder> list = service.loadAppFolders();
        for (AppFolder folder : list) {
            if (!folder.equals(this.folder)) {
                parentSelect.addItem(folder);
                parentSelect.setItemCaption(folder, getMessage(folder.getName()));
            }
        }
    }


}
