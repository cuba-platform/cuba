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

public class PresentationEditor extends CubaWindow {

    protected Logger log = LoggerFactory.getLogger(PresentationEditor.class);

    protected Presentation presentation;

    protected Component.HasPresentations component;
    protected TextField nameField;
    protected CheckBox autoSaveField;
    protected CheckBox defaultField;

    protected CheckBox globalField;

    protected boolean isNew;
    protected boolean allowGlobalPresentations;

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

        setWidthUndefined();

        String titleMessageKey = isNew ? "PresentationsEditor.new" : "PresentationsEditor.edit";
        setCaption(getMessage(titleMessageKey));

        setModal(true);
        setResizable(false);
    }

    protected void initLayout() {
        ThemeConstants theme = App.getInstance().getThemeConstants();

        VerticalLayout root = new VerticalLayout();
        root.setWidthUndefined();
        root.setSpacing(true);
        setContent(root);

        messages = AppBeans.get(Messages.class);

        nameField = new TextField(messages.getMainMessage("PresentationsEditor.name"));
        nameField.setWidth(theme.get("cuba.web.PresentationEditor.name.width"));
        nameField.setValue(getPresentationCaption());
        root.addComponent(nameField);

        autoSaveField = new CheckBox();
        autoSaveField.setCaption(messages.getMainMessage("PresentationsEditor.autoSave"));
        autoSaveField.setValue(BooleanUtils.isTrue(presentation.getAutoSave()));
        root.addComponent(autoSaveField);

        defaultField = new CheckBox();
        defaultField.setCaption(messages.getMainMessage("PresentationsEditor.default"));
        defaultField.setValue(BooleanUtils.isTrue(presentation.getId().equals(component.getDefaultPresentationId())));
        root.addComponent(defaultField);

        if (allowGlobalPresentations) {
            globalField = new CheckBox();
            globalField.setCaption(messages.getMainMessage("PresentationsEditor.global"));
            globalField.setValue(!isNew && presentation.getUser() == null);
            root.addComponent(globalField);
        }

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.setWidthUndefined();
        root.addComponent(buttons);
        root.setComponentAlignment(buttons, Alignment.MIDDLE_LEFT);

        Button commitButton = new CubaButton(messages.getMainMessage("PresentationsEditor.save"));
        commitButton.addClickListener(event -> {
            if (validate()) {
                commit();
                close();
            }
        });
        buttons.addComponent(commitButton);

        Button closeButton = new CubaButton(messages.getMainMessage("PresentationsEditor.close"));
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
                    messages.getMainMessage("PresentationsEditor.error"),
                    messages.getMainMessage("PresentationsEditor.error.nameRequired"),
                    Frame.NotificationType.HUMANIZED);
            return false;
        }

        //check that name is unique
        final Presentation pres = presentations.getPresentationByName(nameField.getValue());
        if (pres != null && !pres.equals(presentation)) {
            App.getInstance().getWindowManager().showNotification(
                    messages.getMainMessage("PresentationsEditor.error"),
                    messages.getMainMessage("PresentationsEditor.error.nameAlreadyExists"),
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