/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.sendingmessage.browse;

import com.haulmont.cuba.core.entity.SendingMessage;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

/**
 * @author ovchinnikov
 * @version $Id$
 */
public class SendingMessageBrowser extends AbstractWindow {

    @Inject
    protected Datasource<SendingMessage> selectedMessageDs;
    @Inject
    protected CollectionDatasource<SendingMessage, UUID> sendingMessageDs;

    @Inject
    protected DataSupplier dataSupplier;

    @Inject
    protected FieldGroup fg;

    @Inject
    protected ComponentsFactory factory;

    @Override
    public void init(Map<String, Object> params) {
        fg.addCustomField("contentText", new FieldGroup.CustomFieldGenerator() {
            @Override
            public Component generateField(Datasource datasource, String propertyId) {
                final TextArea textArea = factory.createComponent(TextArea.NAME);
                textArea.setDatasource(selectedMessageDs, "contentText");
                textArea.setRows(20);
                textArea.setHeight("350px");
                textArea.setEditable(false);
                return textArea;
            }
        });
        sendingMessageDs.addListener(new DsListenerAdapter<SendingMessage>() {
            @Override
            public void itemChanged(Datasource<SendingMessage> ds, SendingMessage prevItem, SendingMessage item) {
                if (item != null) {
                    item = dataSupplier.reload(item, selectedMessageDs.getView());
                }
                selectedMessageDs.setItem(item);
            }
        });
    }
}
