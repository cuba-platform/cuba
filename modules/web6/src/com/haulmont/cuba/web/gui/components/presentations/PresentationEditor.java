/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components.presentations;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.UserSessionClient;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.security.entity.Presentation;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.toolkit.ui.CheckBox;
import com.vaadin.ui.*;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

/**
 * @author gorodnov
 * @version $Id$
 */
public class PresentationEditor extends Window {

    private Presentation presentation;
    private Component.HasPresentations component;

    private TextField nameField;
    private CheckBox autoSaveField;
    private CheckBox defaultField;
    private CheckBox globalField;

    private boolean isNew;

    private Log log = LogFactory.getLog(getClass());

    private static final long serialVersionUID = 6475331797497798366L;

    public PresentationEditor(Presentation presentation, Component.HasPresentations component) {
        this.presentation = presentation;
        this.component = component;

        isNew = PersistenceHelper.isNew(presentation);

        initLayout();
        setWidth("300px");

        String key = isNew ? "PresentationsEditor.new" : "PresentationsEditor.edit";
        setCaption(getMessage(key));
        setModal(true);
        setResizable(false);
    }

    protected void initLayout() {
        VerticalLayout root = new VerticalLayout();
        root.setMargin(true);
        root.setSpacing(true);
        setContent(root);

        nameField = new TextField(getMessage("PresentationsEditor.name"));
        nameField.setWidth("250px");
        nameField.setValue(getPresentationCaption());
        root.addComponent(nameField);

        autoSaveField = new CheckBox();
        autoSaveField.setCaption(getMessage("PresentationsEditor.autoSave"));
        autoSaveField.setValue(BooleanUtils.isTrue(presentation.getAutoSave()));
        root.addComponent(autoSaveField);

        defaultField = new CheckBox();
        defaultField.setCaption(getMessage("PresentationsEditor.default"));
        defaultField.setValue(BooleanUtils.isTrue(presentation.getDefault()));
        root.addComponent(defaultField);

        final boolean allowGlobalPresentations = UserSessionClient.getUserSession()
                .isSpecificPermitted("cuba.gui.presentations.global");
        if (allowGlobalPresentations) {
            globalField = new CheckBox();
            globalField.setCaption(getMessage("PresentationsEditor.global"));
            globalField.setValue(!isNew && presentation.getUser() == null);
            root.addComponent(globalField);
        }

        final HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.setMargin(true, false, false, false);
        buttons.setWidth("-1px");
        root.addComponent(buttons);
        root.setComponentAlignment(buttons, Alignment.MIDDLE_LEFT);

        Button commitButton = new Button(getMessage("PresentationsEditor.save"));
        commitButton.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                final Presentations presentations = component.getPresentations();

                //check that name is empty
                if (StringUtils.isEmpty((String) nameField.getValue())) {
                    App.getInstance().getWindowManager().showNotification(
                            getMessage("PresentationsEditor.error"),
                            getMessage("PresentationsEditor.error.nameRequired"),
                            IFrame.NotificationType.HUMANIZED);
                    return;
                }

                //check that name is unique
                final Presentation pres = presentations.getPresentationByName((String) nameField.getValue());
                if (pres != null && !pres.equals(presentation)) {
                    App.getInstance().getWindowManager().showNotification(
                            getMessage("PresentationsEditor.error"),
                            getMessage("PresentationsEditor.error.nameAlreadyExists"),
                            IFrame.NotificationType.HUMANIZED);
                    return;
                }

                Document doc = DocumentHelper.createDocument();
                doc.setRootElement(doc.addElement("presentation"));

                component.saveSettings(doc.getRootElement());

                String xml = Dom4j.writeDocument(doc, false);
                presentation.setXml(xml);

                presentation.setName((String) nameField.getValue());
                presentation.setAutoSave((Boolean) autoSaveField.getValue());
                presentation.setDefault((Boolean) defaultField.getValue());

                User user = UserSessionClient.getUserSession().getSubstitutedUser();
                if (user == null) {
                    user = UserSessionClient.getUserSession().getUser();
                }

                boolean userOnly = !allowGlobalPresentations || !BooleanUtils.isTrue((Boolean) globalField.getValue());
                presentation.setUser(userOnly ? user : null);

                log.trace(String.format("XML: %s", Dom4j.writeDocument(doc, true)));

                if (isNew) {
                    presentations.add(presentation);
                } else {
                    presentations.modify(presentation);
                }
                presentations.commit();

                addListener(new CloseListener() {
                    public void windowClose(CloseEvent e) {
                        if (isNew) {
                            component.applyPresentation(presentation.getId());
                        }
                    }
                });

                close();
            }
        });
        buttons.addComponent(commitButton);

        Button closeButton = new Button(getMessage("PresentationsEditor.close"));
        closeButton.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        buttons.addComponent(closeButton);

        nameField.focus();
    }

    protected String getPresentationCaption() {
        return presentation.getName() == null ? "" : presentation.getName();
    }
    
    protected String getMessage(String key) {
        return MessageProvider.getMessage(getClass(), key);
    }
}
