/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.app.ui.sendingmessage.browse;

import com.haulmont.cuba.core.entity.SendingMessage;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;

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

    @Override
    public void init(Map<String, Object> params) {
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
