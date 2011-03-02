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
    private boolean refreshing;

    private enum State {
        FIRST_COMPLETE,     // "63 rows"
        FIRST_INCOMPLETE,   // "1-100 rows of [?] >"
        MIDDLE,             // "< 101-200 rows of [?] >"
        LAST                // "< 201-252 rows"
    }

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
            component.getCountButton().addListener(
                    new Button.ClickListener() {
                        public void buttonClick(Button.ClickEvent event) {
                            onLinkClick();
                        }
                    }
            );
            component.getPrevButton().addListener(
                    new Button.ClickListener() {
                        public void buttonClick(Button.ClickEvent event) {
                            onPrevClick();
                        }
                    }
            );
            component.getNextButton().addListener(
                    new Button.ClickListener() {
                        public void buttonClick(Button.ClickEvent event) {
                            onNextClick();
                        }
                    }
            );
        }
    }

    private void onPrevClick() {
        if (!(datasource instanceof CollectionDatasource.SupportsPaging))
            return;

        CollectionDatasource.SupportsPaging ds = (CollectionDatasource.SupportsPaging) datasource;
        int newStart = ds.getFirstResult() - ds.getMaxResults();
        ds.setFirstResult(newStart < 0 ? 0 : newStart);
        refreshing = true;
        try {
            ds.refresh();
        } finally {
            refreshing = false;
        }
    }

    private void onNextClick() {
        if (!(datasource instanceof CollectionDatasource.SupportsPaging))
            return;

        CollectionDatasource.SupportsPaging ds = (CollectionDatasource.SupportsPaging) datasource;
        ds.setFirstResult(ds.getFirstResult() + ds.getMaxResults());

        refreshing = true;
        try {
            ds.refresh();
        } finally {
            refreshing = false;
        }
    }

    private void onLinkClick() {
        if (datasource == null || !(datasource instanceof CollectionDatasource.SupportsPaging))
            return;

        int count = ((CollectionDatasource.SupportsPaging) datasource).getCount();
        component.getCountButton().setCaption(String.valueOf(count));
    }

    private void onCollectionChanged() {
        if (datasource == null)
            return;

        String msgKey;
        int size = datasource.size();
        int start = 0;

        State state;
        if (datasource instanceof CollectionDatasource.SupportsPaging) {
            CollectionDatasource.SupportsPaging ds = (CollectionDatasource.SupportsPaging) datasource;
            if ((size == 0 || size < ds.getMaxResults()) && ds.getFirstResult() == 0) {
                state = State.FIRST_COMPLETE;
            } else if (size == ds.getMaxResults() && ds.getFirstResult() == 0) {
                state = State.FIRST_INCOMPLETE;
            } else if (size == ds.getMaxResults() && ds.getFirstResult() > 0) {
                state = State.MIDDLE;
                start = ds.getFirstResult();
            } else if (size < ds.getMaxResults() && ds.getFirstResult() > 0) {
                state = State.LAST;
                start = ds.getFirstResult();
            } else
                state = State.FIRST_COMPLETE;
        } else {
            state = State.FIRST_COMPLETE;
        }

        String countValue;
        switch (state) {
            case FIRST_COMPLETE:
                component.getCountButton().setVisible(false);
                component.getPrevButton().setVisible(false);
                component.getNextButton().setVisible(false);
                msgKey = "table.rowsCount.msg2";
                countValue = String.valueOf(size);
                break;
            case FIRST_INCOMPLETE:
                component.getCountButton().setVisible(true);
                component.getPrevButton().setVisible(false);
                component.getNextButton().setVisible(true);
                msgKey = "table.rowsCount.msg1";
                countValue = "1-" + size;
                break;
            case MIDDLE:
                component.getCountButton().setVisible(true);
                component.getPrevButton().setVisible(true);
                component.getNextButton().setVisible(true);
                msgKey = "table.rowsCount.msg1";
                countValue = (start + 1) + "-" + (start + size);
                break;
            case LAST:
                component.getCountButton().setVisible(false);
                component.getPrevButton().setVisible(true);
                component.getNextButton().setVisible(false);
                msgKey = "table.rowsCount.msg2";
                countValue = (start + 1) + "-" + (start + size);
                break;
            default:
                throw new UnsupportedOperationException();
        }

        String messagesPack = AppConfig.getInstance().getMessagesPack();
        component.getLabel().setValue(MessageProvider.formatMessage(messagesPack, msgKey, countValue));

        if (component.getCountButton().isVisible() && !refreshing) {
            component.getCountButton().setCaption(MessageProvider.getMessage(messagesPack, "table.rowsCount.msg3"));
        }
    }
}
