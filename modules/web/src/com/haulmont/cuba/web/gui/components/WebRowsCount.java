/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.02.11 14:56
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.RowsCount;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.vaadin.ui.Button;

public class WebRowsCount
        extends WebAbstractComponent<com.haulmont.cuba.web.toolkit.ui.RowsCount>
        implements RowsCount
{
    private CollectionDatasource datasource;

    public WebRowsCount() {
        component = new com.haulmont.cuba.web.toolkit.ui.RowsCount();
        component.setStyleName("table-rows-count");
    }

    public CollectionDatasource getDatasource() {
        return datasource;
    }

    public void setDatasource(CollectionDatasource datasource) {
        this.datasource = datasource;
        if (datasource != null) {
            this.datasource.addListener(
                    new CollectionDsListenerAdapter() {
                        @Override
                        public void collectionChanged(CollectionDatasource ds, Operation operation) {
                            onCollectionChanged();
                        }
                    }
            );
            component.getLink().addListener(
                    new Button.ClickListener() {
                        public void buttonClick(Button.ClickEvent event) {
                            onLinkClick();
                        }
                    }
            );
        }
    }

    private void onLinkClick() {
        if (datasource == null || !(datasource instanceof CollectionDatasource.SupportsCount))
            return;

        int count = ((CollectionDatasource.SupportsCount) datasource).getCount();
        component.getLink().setCaption(String.valueOf(count));
    }

    private void onCollectionChanged() {
        if (datasource == null)
            return;

        String msgKey;
        int size = datasource.size();
        if (size == 0 || size != datasource.getMaxResults() || !(datasource instanceof CollectionDatasource.SupportsCount)) {
            msgKey = "table.rowsCount.msg2";
            component.getLink().setVisible(false);
        } else {
            msgKey = "table.rowsCount.msg1";
            component.getLink().setVisible(true);
        }

        String messagesPack = AppConfig.getInstance().getMessagesPack();
        component.getLabel().setValue(MessageProvider.formatMessage(messagesPack, msgKey, size));

        if (component.getLink().isVisible()) {
            component.getLink().setCaption(MessageProvider.getMessage(messagesPack, "table.rowsCount.msg3"));
        }
    }
}
