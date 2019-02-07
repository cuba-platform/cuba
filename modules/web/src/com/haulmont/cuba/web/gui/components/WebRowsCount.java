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

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.RowsCount;
import com.haulmont.cuba.gui.components.VisibilityChangeNotifier;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasource.Operation;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.WeakCollectionChangeListener;
import com.haulmont.cuba.web.toolkit.ui.CubaRowsCount;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WebRowsCount extends WebAbstractComponent<CubaRowsCount> implements RowsCount, VisibilityChangeNotifier {

    protected static final String TABLE_ROWS_COUNT_STYLENAME = "c-table-rows-count";

    protected CollectionDatasource datasource;
    protected boolean refreshing;
    protected State state;
    protected State lastState;
    protected int start;
    protected int size;
    protected ListComponent owner;
    protected boolean samePage;

    protected CollectionDatasource.CollectionChangeListener collectionChangeListener;
    protected WeakCollectionChangeListener weakCollectionChangeListener;
    protected List<BeforeRefreshListener> beforeRefreshListeners;

    protected List<VisibilityChangeListener> visibilityChangeListeners;

    public WebRowsCount() {
        component = new CubaRowsCount();
        component.setStyleName(TABLE_ROWS_COUNT_STYLENAME);
    }

    @Override
    public String getStyleName() {
        return StringUtils.normalizeSpace(super.getStyleName().replace(TABLE_ROWS_COUNT_STYLENAME, ""));
    }

    @Override
    public CollectionDatasource getDatasource() {
        return datasource;
    }

    @Override
    public void setDatasource(CollectionDatasource datasource) {
        Preconditions.checkNotNullArgument(datasource, "datasource is null");

        if (this.datasource != null) {
            //noinspection unchecked
            this.datasource.removeCollectionChangeListener(weakCollectionChangeListener);
            weakCollectionChangeListener = null;
        } else {
            //noinspection unchecked
            collectionChangeListener = e -> {
                samePage = Operation.REFRESH != e.getOperation()
                        && Operation.CLEAR != e.getOperation();
                onCollectionChanged();
            };

        }

        this.datasource = datasource;

        weakCollectionChangeListener = new WeakCollectionChangeListener(datasource, collectionChangeListener);
        //noinspection unchecked
        datasource.addCollectionChangeListener(weakCollectionChangeListener);

        component.getCountButton().addClickListener(event -> onLinkClick());
        component.getPrevButton().addClickListener(event -> onPrevClick());
        component.getNextButton().addClickListener(event -> onNextClick());
        component.getFirstButton().addClickListener(event -> onFirstClick());
        component.getLastButton().addClickListener(event -> onLastClick());

        if (datasource.getState() == Datasource.State.VALID) {
            onCollectionChanged();
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

    @Override
    public void addBeforeRefreshListener(BeforeRefreshListener listener) {
        if (beforeRefreshListeners == null)
            beforeRefreshListeners = new ArrayList<>(1);
        if (!beforeRefreshListeners.contains(listener))
            beforeRefreshListeners.add(listener);
    }

    @Override
    public void removeBeforeRefreshListener(BeforeRefreshListener listener) {
        if (beforeRefreshListeners != null)
            beforeRefreshListeners.remove(listener);
    }

    protected void onPrevClick() {
        if (!(datasource instanceof CollectionDatasource.SupportsPaging)) {
            return;
        }

        CollectionDatasource.SupportsPaging ds = (CollectionDatasource.SupportsPaging) datasource;
        int firstResult = ds.getFirstResult();
        int newStart = ds.getFirstResult() - ds.getMaxResults();
        ds.setFirstResult(newStart < 0 ? 0 : newStart);
        if (refreshDatasource(ds)) {
            if (owner instanceof WebAbstractTable) {
                com.vaadin.ui.Table vTable = (com.vaadin.ui.Table) ((WebAbstractTable) owner).getComponent();
                vTable.setCurrentPageFirstItemIndex(0);
            }
        } else {
            ds.setFirstResult(firstResult);
        }
    }

    protected void onNextClick() {
        if (!(datasource instanceof CollectionDatasource.SupportsPaging)) {
            return;
        }

        CollectionDatasource.SupportsPaging ds = (CollectionDatasource.SupportsPaging) datasource;
        int firstResult = ds.getFirstResult();
        ds.setFirstResult(ds.getFirstResult() + ds.getMaxResults());
        if (refreshDatasource(ds)) {
            if (state == State.LAST && size == 0) {
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
        } else {
            ds.setFirstResult(firstResult);
        }
    }

    protected void onFirstClick() {
        if (!(datasource instanceof CollectionDatasource.SupportsPaging)) {
            return;
        }

        CollectionDatasource.SupportsPaging ds = (CollectionDatasource.SupportsPaging) datasource;
        int firstResult = ds.getFirstResult();
        ds.setFirstResult(0);
        if (refreshDatasource(ds)) {
            if (owner instanceof WebAbstractTable) {
                com.vaadin.ui.Table vTable = (com.vaadin.ui.Table) ((WebAbstractTable) owner).getComponent();
                vTable.setCurrentPageFirstItemIndex(0);
            }
        } else {
            ds.setFirstResult(firstResult);
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

        int firstResult = ds.getFirstResult();
        ds.setFirstResult(count - itemsToDisplay);
        if (refreshDatasource(ds)) {
            if (owner instanceof WebAbstractTable) {
                com.vaadin.ui.Table vTable = (com.vaadin.ui.Table) ((WebAbstractTable) owner).getComponent();
                vTable.setCurrentPageFirstItemIndex(0);
            }
        } else {
            ds.setFirstResult(firstResult);
        }
    }

    protected boolean refreshDatasource(CollectionDatasource.SupportsPaging ds) {
        if (beforeRefreshListeners != null) {
            boolean refreshPrevented = false;
            for (BeforeRefreshListener listener : beforeRefreshListeners) {
                BeforeRefreshEvent event = new BeforeRefreshEvent(this, ds);
                listener.beforeDatasourceRefresh(event);
                refreshPrevented = refreshPrevented || event.isRefreshPrevented();
            }
            if (refreshPrevented)
                return false;
        }
        refreshing = true;
        try {
            ds.refresh();
        } finally {
            refreshing = false;
        }
        return true;
    }

    protected void onLinkClick() {
        if (datasource == null || !(datasource instanceof CollectionDatasource.SupportsPaging)) {
            return;
        }

        int count = ((CollectionDatasource.SupportsPaging) datasource).getCount();
        component.getCountButton().setCaption(String.valueOf(count));
        component.getCountButton().addStyleName("c-paging-count-number");
        component.getCountButton().setEnabled(false);
    }

    protected void onCollectionChanged() {
        if (datasource == null) {
            return;
        }

        String msgKey;
        size = datasource.size();
        start = 0;

        boolean refreshSizeButton = false;
        if (datasource instanceof CollectionDatasource.SupportsPaging) {
            CollectionDatasource.SupportsPaging ds = (CollectionDatasource.SupportsPaging) datasource;
            if (samePage) {
                state = lastState == null ? State.FIRST_COMPLETE : lastState;
                start = ds.getFirstResult();
                samePage = false;
                refreshSizeButton = State.LAST.equals(state);
            } else if ((size == 0 || size < ds.getMaxResults()) && ds.getFirstResult() == 0) {
                state = State.FIRST_COMPLETE;
                lastState = state;
            } else if (size == ds.getMaxResults() && ds.getFirstResult() == 0) {
                state = State.FIRST_INCOMPLETE;
                lastState = state;
            } else if (size == ds.getMaxResults() && ds.getFirstResult() > 0) {
                state = State.MIDDLE;
                start = ds.getFirstResult();
                lastState = state;
            } else if (size < ds.getMaxResults() && ds.getFirstResult() > 0) {
                state = State.LAST;
                start = ds.getFirstResult();
                lastState = state;
            } else {
                state = State.FIRST_COMPLETE;
                lastState = state;
            }
        } else {
            state = State.FIRST_COMPLETE;
            lastState = state;
        }

        String countValue;
        switch (state) {
            case FIRST_COMPLETE:
                component.getCountButton().setVisible(false);
                component.getPrevButton().setVisible(false);
                component.getNextButton().setVisible(false);
                component.getFirstButton().setVisible(false);
                component.getLastButton().setVisible(false);
                if (size == 1) {
                    msgKey = "table.rowsCount.msg2Singular1";
                } else if (size % 100 > 10 && size % 100 < 20) {
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
                countValue = countValue(start, size);
                break;
            case MIDDLE:
                component.getCountButton().setVisible(true);
                component.getPrevButton().setVisible(true);
                component.getNextButton().setVisible(true);
                component.getFirstButton().setVisible(true);
                component.getLastButton().setVisible(true);
                msgKey = "table.rowsCount.msg1";
                countValue = countValue(start, size);
                break;
            case LAST:
                component.getCountButton().setVisible(false);
                component.getPrevButton().setVisible(true);
                component.getNextButton().setVisible(false);
                component.getFirstButton().setVisible(true);
                component.getLastButton().setVisible(false);
                msgKey = "table.rowsCount.msg2Plural2";
                countValue = countValue(start, size);
                break;
            default:
                throw new UnsupportedOperationException();
        }
        Messages messages = AppBeans.get(Messages.NAME);

        component.getLabel().setValue(messages.formatMainMessage(msgKey, countValue));

        if (component.getCountButton().isVisible() && !refreshing || refreshSizeButton) {
            component.getCountButton().setCaption(messages.getMainMessage("table.rowsCount.msg3"));
            component.getCountButton().removeStyleName("c-paging-count-number");
            component.getCountButton().setEnabled(true);
        }
    }

    protected String countValue(int start, int size) {
        if (size == 0) {
            return String.valueOf(size);
        } else {
            return (start + 1) + "-" + (start + size);
        }
    }

    @Override
    public void addVisibilityChangeListener(VisibilityChangeListener listener) {
        if (visibilityChangeListeners == null) {
            visibilityChangeListeners = new LinkedList<>();
        }

        if (!visibilityChangeListeners.contains(listener)) {
            visibilityChangeListeners.add(listener);
        }
    }

    @Override
    public void removeVisibilityChangeListener(VisibilityChangeListener listener) {
        if (!visibilityChangeListeners.contains(listener)) {
            visibilityChangeListeners.remove(listener);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        if (visibilityChangeListeners != null) {
            VisibilityChangeEvent event = new VisibilityChangeEvent(this, visible);
            for (VisibilityChangeListener listener : new ArrayList<>(visibilityChangeListeners)) {
                listener.componentVisibilityChanged(event);
            }
        }
    }
}
