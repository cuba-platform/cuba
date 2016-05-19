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
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.RowsCount;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.toolkit.ui.CubaRowsCount;

/**
 */
public class WebRowsCount extends WebAbstractComponent<CubaRowsCount> implements RowsCount {

    protected CollectionDatasource datasource;
    protected boolean refreshing;
    protected State state;
    protected int start;
    protected int size;
    protected ListComponent owner;

    public WebRowsCount() {
        component = new CubaRowsCount();
        component.setStyleName("cuba-table-rows-count");
    }

    @Override
    public CollectionDatasource getDatasource() {
        return datasource;
    }

    @Override
    public void setDatasource(CollectionDatasource datasource) {
        this.datasource = datasource;
        if (datasource != null) {
            //noinspection unchecked
            this.datasource.addCollectionChangeListener(e -> onCollectionChanged());

            component.getCountButton().addClickListener(event -> onLinkClick());
            component.getPrevButton().addClickListener(event -> onPrevClick());
            component.getNextButton().addClickListener(event -> onNextClick());
            component.getFirstButton().addClickListener(event -> onFirstClick());
            component.getLastButton().addClickListener(event -> onLastClick());

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

    protected void onPrevClick() {
        if (!(datasource instanceof CollectionDatasource.SupportsPaging)) {
            return;
        }

        CollectionDatasource.SupportsPaging ds = (CollectionDatasource.SupportsPaging) datasource;
        int newStart = ds.getFirstResult() - ds.getMaxResults();
        ds.setFirstResult(newStart < 0 ? 0 : newStart);
        refreshDatasource(ds);
        if (owner instanceof WebAbstractTable) {
            com.vaadin.ui.Table vTable = (com.vaadin.ui.Table) ((WebAbstractTable) owner).getComponent();
            vTable.setCurrentPageFirstItemIndex(0);
        }
    }

    protected void onNextClick() {
        if (!(datasource instanceof CollectionDatasource.SupportsPaging)) {
            return;
        }

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

    protected void onFirstClick() {
        if (!(datasource instanceof CollectionDatasource.SupportsPaging)) {
            return;
        }

        CollectionDatasource.SupportsPaging ds = (CollectionDatasource.SupportsPaging) datasource;
        ds.setFirstResult(0);
        refreshDatasource(ds);
        if (owner instanceof WebAbstractTable) {
            com.vaadin.ui.Table vTable = (com.vaadin.ui.Table) ((WebAbstractTable) owner).getComponent();
            vTable.setCurrentPageFirstItemIndex(0);
        }
    }

    protected void onLastClick() {
        if (!(datasource instanceof CollectionDatasource.SupportsPaging)) {
            return;
        }

        CollectionDatasource.SupportsPaging ds = (CollectionDatasource.SupportsPaging) datasource;
        int count = ((CollectionDatasource.SupportsPaging) datasource).getCount();
        int itemsToDisplay = count % ds.getMaxResults();
        if (itemsToDisplay == 0) itemsToDisplay = ds.getMaxResults();

        ds.setFirstResult(count - itemsToDisplay);
        refreshDatasource(ds);

        if (owner instanceof WebAbstractTable) {
            com.vaadin.ui.Table vTable = (com.vaadin.ui.Table) ((WebAbstractTable) owner).getComponent();
            vTable.setCurrentPageFirstItemIndex(0);
        }
    }

    protected void refreshDatasource(CollectionDatasource.SupportsPaging ds) {
        refreshing = true;
        try {
            ds.refresh();
        } finally {
            refreshing = false;
        }
    }

    protected void onLinkClick() {
        if (datasource == null || !(datasource instanceof CollectionDatasource.SupportsPaging)) {
            return;
        }

        int count = ((CollectionDatasource.SupportsPaging) datasource).getCount();
        component.getCountButton().setCaption(String.valueOf(count));
        component.getCountButton().addStyleName("cuba-paging-count-number");
        component.getCountButton().setEnabled(false);
    }

    protected void onCollectionChanged() {
        if (datasource == null) {
            return;
        }

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
            } else {
                state = State.FIRST_COMPLETE;
            }
        } else {
            state = State.FIRST_COMPLETE;
        }

        String countValue;
        switch (state) {
            case FIRST_COMPLETE:
                component.getCountButton().setVisible(false);
                component.getPrevButton().setVisible(false);
                component.getNextButton().setVisible(false);
                component.getFirstButton().setVisible(false);
                component.getLastButton().setVisible(false);
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
                component.getFirstButton().setVisible(false);
                component.getLastButton().setVisible(true);
                msgKey = "table.rowsCount.msg1";
                countValue = "1-" + size;
                break;
            case MIDDLE:
                component.getCountButton().setVisible(true);
                component.getPrevButton().setVisible(true);
                component.getNextButton().setVisible(true);
                component.getFirstButton().setVisible(true);
                component.getLastButton().setVisible(true);
                msgKey = "table.rowsCount.msg1";
                countValue = (start + 1) + "-" + (start + size);
                break;
            case LAST:
                component.getCountButton().setVisible(false);
                component.getPrevButton().setVisible(true);
                component.getNextButton().setVisible(false);
                component.getFirstButton().setVisible(true);
                component.getLastButton().setVisible(false);
                msgKey = "table.rowsCount.msg2Plural2";
                countValue = (start + 1) + "-" + (start + size);
                break;
            default:
                throw new UnsupportedOperationException();
        }
        Messages messages = AppBeans.get(Messages.NAME);

        component.getLabel().setValue(messages.formatMainMessage(msgKey, countValue));

        if (component.getCountButton().isVisible() && !refreshing) {
            component.getCountButton().setCaption(messages.getMainMessage("table.rowsCount.msg3"));
            component.getCountButton().removeStyleName("cuba-paging-count-number");
            component.getCountButton().setEnabled(true);
        }
    }
}
