/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components.presentations;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.security.entity.Presentation;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.toolkit.ui.CubaButton;
import com.haulmont.cuba.web.toolkit.ui.CubaWindow;
import com.vaadin.ui.*;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gorodnov
 * @version $Id$
 */
public class PresentationEditor extends CubaWindow {

    protected Presentation presentation;

    protected Component.HasPresentations component;
    protected TextField nameField;
    protected CheckBox autoSaveField;
    protected CheckBox defaultField;

    protected CheckBox globalField;

    protected boolean isNew;
    protected boolean allowGlobalPresentations;

    protected Logger log = LoggerFactory.getLogger(getClass());

    protected Messages messages;
    protected UserSessionSource sessionSource;

    public PresentationEditor(Presentation presentation, Component.HasPresentations component) {
        this.presentation = presentation;
        this.component = component;

        messages = AppBeans.get(Messages.NAME);
        sessionSource = AppBeans.get(UserSessionSource.NAME);

        isNew = PersistenceHelper.isNew(presentation);
        allowGlobalPresentations = sessionSource.getUserSession()
                .isSpecificPermitted("cuba.gui.presentations.global");

        initLayout();

        ThemeConstants theme = App.getInstance().getThemeConstants();
        setWidth(theme.get("cuba.web.PresentationEditor.width"));

        String key = isNew ? "PresentationsEditor.new" : "PresentationsEditor.edit";
        setCaption(getMessage(key));
        setModal(true);
        setResizable(false);
    }

    protected void initLayout() {
        ThemeConstants theme = App.getInstance().getThemeConstants();

        VerticalLayout root = new VerticalLayout();
        root.setSpacing(true);
        setContent(root);

        nameField = new TextField(getMessage("PresentationsEditor.name"));
        nameField.setWidth(theme.get("cuba.web.PresentationEditor.name.width"));
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

        if (allowGlobalPresentations) {
            globalField = new CheckBox();
            globalField.setCaption(getMessage("PresentationsEditor.global"));
            globalField.setValue(!isNew && presentation.getUser() == null);
            root.addComponent(globalField);
        }

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.setWidth("-1px");
        root.addComponent(buttons);
        root.setComponentAlignment(buttons, Alignment.MIDDLE_LEFT);

        Button commitButton = new CubaButton(getMessage("PresentationsEditor.save"));
        commitButton.addClickListener(event -> {
            if (validate()) {
                commit();
                close();
            }
        });
        buttons.addComponent(commitButton);

        Button closeButton = new CubaButton(getMessage("PresentationsEditor.close"));
        closeButton.addClickListener(event -> {
            close();
        });
        buttons.addComponent(closeButton);

        nameField.focus();
    }

    protected boolean validate() {
        Presentations presentations = component.getPresentations();

        //check that name is empty
        if (StringUtils.isEmpty(nameField.getValue())) {
            App.getInstance().getWindowManager().showNotification(
                    getMessage("PresentationsEditor.error"),
                    getMessage("PresentationsEditor.error.nameRequired"),
                    Frame.NotificationType.HUMANIZED);
            return false;
        }

        //check that name is unique
        final Presentation pres = presentations.getPresentationByName(nameField.getValue());
        if (pres != null && !pres.equals(presentation)) {
            App.getInstance().getWindowManager().showNotification(
                    getMessage("PresentationsEditor.error"),
                    getMessage("PresentationsEditor.error.nameAlreadyExists"),
                    Frame.NotificationType.HUMANIZED);
            return false;
        }
        return true;
    }

    protected void commit() {
        Presentations presentations = component.getPresentations();

        Document doc = DocumentHelper.createDocument();
        doc.setRootElement(doc.addElement("presentation"));

        component.saveSettings(doc.getRootElement());

        String xml = Dom4j.writeDocument(doc, false);
        presentation.setXml(xml);

        presentation.setName(nameField.getValue());
        presentation.setAutoSave(autoSaveField.getValue());
        presentation.setDefault(defaultField.getValue());

        User user = sessionSource.getUserSession().getCurrentOrSubstitutedUser();

        boolean userOnly = !allowGlobalPresentations || !BooleanUtils.isTrue(globalField.getValue());
        presentation.setUser(userOnly ? user : null);

        if (log.isTraceEnabled()) {
            log.trace(String.format("XML: %s", Dom4j.writeDocument(doc, true)));
        }

        if (isNew) {
            presentations.add(presentation);
        } else {
            presentations.modify(presentation);
        }
        presentations.commit();

        addCloseListener(e -> {
            if (isNew) {
                component.applyPresentation(presentation.getId());
            }
        });
    }

    protected String getPresentationCaption() {
        return presentation.getName() == null ? "" : presentation.getName();
    }

    protected String getMessage(String key) {
        return messages.getMessage(getClass(), key);
    }
}