/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.RowsCount;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.vaadin.ui.Button;

import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public class WebRowsCount
        extends WebAbstractComponent<com.haulmont.cuba.web.toolkit.ui.RowsCount>
        implements RowsCount {
    protected CollectionDatasource datasource;
    protected boolean refreshing;
    protected State state;
    protected int start;
    protected int size;
    protected ListComponent owner;

    public WebRowsCount() {
        component = new com.haulmont.cuba.web.toolkit.ui.RowsCount();
        component.setStyleName("table-rows-count");
    }

    @Override
    public CollectionDatasource getDatasource() {
        return datasource;
    }

    @Override
    public void setDatasource(CollectionDatasource datasource) {
        this.datasource = datasource;
        if (datasource != null) {
            this.datasource.addListener(
                    new CollectionDsListenerAdapter<Entity>() {
                        @Override
                        public void collectionChanged(CollectionDatasource ds, Operation operation, List<Entity> items) {
                            onCollectionChanged();
                        }
                    }
            );
            component.getCountButton().addListener(
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            onLinkClick();
                        }
                    }
            );
            component.getPrevButton().addListener(
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            onPrevClick();
                        }
                    }
            );
            component.getNextButton().addListener(
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            onNextClick();
                        }
                    }
            );
            if (datasource.getState() == Datasource.State.VALID) {
                onCollectionChanged();
            }
        }
    }

    @Override
    public ListComponent getOwner() {
        return owner;
    }

    @Override
    public void setOwner(ListComponent owner) {
        this.owner = owner;
    }

    private void onPrevClick() {
        if (!(datasource instanceof CollectionDatasource.SupportsPaging))
            return;

        CollectionDatasource.SupportsPaging ds = (CollectionDatasource.SupportsPaging) datasource;
        int newStart = ds.getFirstResult() - ds.getMaxResults();
        ds.setFirstResult(newStart < 0 ? 0 : newStart);
        refreshDatasource(ds);
        if (owner instanceof WebAbstractTable) {
            com.vaadin.ui.Table vTable = (com.vaadin.ui.Table) ((WebAbstractTable) owner).getComponent();
            vTable.setCurrentPageFirstItemIndex(0);
        }
    }

    private void onNextClick() {
        if (!(datasource instanceof CollectionDatasource.SupportsPaging))
            return;

        CollectionDatasource.SupportsPaging ds = (CollectionDatasource.SupportsPaging) datasource;
        int firstResult = ds.getFirstResult();
        ds.setFirstResult(ds.getFirstResult() + ds.getMaxResults());
        refreshDatasource(ds);

        if (state.equals(State.LAST) && size == 0) {
            ds.setFirstResult(firstResult);
            int maxResults = ds.getMaxResults();
            ds.setMaxResults(maxResults + 1);
            refreshDatasource(ds);
            ds.setMaxResults(maxResults);
        }
        if (owner instanceof WebAbstractTable) {
            com.vaadin.ui.Table vTable = (com.vaadin.ui.Table) ((WebAbstractTable) owner).getComponent();
            vTable.setCurrentPageFirstItemIndex(0);
        }
    }

    private void refreshDatasource(CollectionDatasource.SupportsPaging ds) {
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

    protected void onCollectionChanged() {
        if (datasource == null)
            return;

        String msgKey;
        size = datasource.size();
        start = 0;

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
                if (size % 100 > 10 && size % 100 < 20) {
                    msgKey = "table.rowsCount.msg2Plural1";
                } else {
                    switch (size % 10) {
                        case 1:
                            msgKey = "table.rowsCount.msg2Singular";
                            break;
                        case 2:
                        case 3:
                        case 4:
                            msgKey = "table.rowsCount.msg2Plural2";
                            break;
                        default:
                            msgKey = "table.rowsCount.msg2Plural1";
                    }
                }
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
                msgKey = "table.rowsCount.msg2Plural2";
                countValue = (start + 1) + "-" + (start + size);
                break;
            default:
                throw new UnsupportedOperationException();
        }

        String messagesPack = AppConfig.getMessagesPack();
        component.getLabel().setValue(MessageProvider.formatMessage(messagesPack, msgKey, countValue));

        if (component.getCountButton().isVisible() && !refreshing) {
            component.getCountButton().setCaption(MessageProvider.getMessage(messagesPack, "table.rowsCount.msg3"));
        }
    }
}
