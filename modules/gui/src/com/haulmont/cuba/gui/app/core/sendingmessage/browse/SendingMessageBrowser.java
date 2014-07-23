/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.sendingmessage.browse;

import com.haulmont.cuba.core.app.EmailService;
import com.haulmont.cuba.core.entity.SendingMessage;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.FieldGroup;
import com.haulmont.cuba.gui.components.TextArea;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

/**
 * @author ovchinnikov
 * @version $Id$
 */
public class SendingMessageBrowser extends AbstractWindow {

    protected static final String CONTENT_TEXT = "contentText";

    @Inject
    protected CollectionDatasource<SendingMessage, UUID> sendingMessageDs;

    @Inject
    protected EmailService emailService;

    @Inject
    protected FieldGroup fg;

    @Inject
    protected ComponentsFactory factory;

    @Inject
    protected ThemeConstants themeConstants;

    @Override
    public void init(Map<String, Object> params) {
        fg.addCustomField(CONTENT_TEXT, new FieldGroup.CustomFieldGenerator() {
            @Override
            public Component generateField(Datasource datasource, String propertyId) {
                TextArea contentTextArea = factory.createComponent(TextArea.NAME);
                contentTextArea.setRows(20);
                contentTextArea.setHeight(themeConstants.get("cuba.gui.SendingMessageBrowser.contentTextArea.height"));
                return contentTextArea;
            }
        });
        fg.setEditable(CONTENT_TEXT, false);
        sendingMessageDs.addListener(new DsListenerAdapter<SendingMessage>() {
            @Override
            public void itemChanged(Datasource<SendingMessage> ds, SendingMessage prevItem, SendingMessage item) {
                selectedItemChanged(item);
            }
        });
    }

    protected void selectedItemChanged(SendingMessage item) {
        String contentText = null;
        if (item != null) {
            contentText = emailService.loadContentText(item);
        }
        fg.setEditable(CONTENT_TEXT, true);
        fg.setFieldValue(CONTENT_TEXT, contentText);
        fg.setEditable(CONTENT_TEXT, false);
    }
}