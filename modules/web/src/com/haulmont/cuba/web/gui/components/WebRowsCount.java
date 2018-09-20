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
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.DataGridSource;
import com.haulmont.cuba.gui.components.data.TableSource;
import com.haulmont.cuba.gui.components.data.datagrid.CollectionContainerDataGridSource;
import com.haulmont.cuba.gui.components.data.datagrid.CollectionDatasourceDataGridAdapter;
import com.haulmont.cuba.gui.components.data.table.CollectionContainerTableSource;
import com.haulmont.cuba.gui.components.data.table.CollectionDatasourceTableAdapter;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasource.Operation;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.WeakCollectionChangeListener;
import com.haulmont.cuba.gui.model.*;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.widgets.CubaRowsCount;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WebRowsCount extends WebAbstractComponent<CubaRowsCount> implements RowsCount, VisibilityChangeNotifier {

    protected static final String TABLE_ROWS_COUNT_STYLENAME = "c-table-rows-count";

    protected Messages messages;

    protected ListComponent owner;

    protected Adapter adapter;

    @Inject
    protected DataManager dataManager;

    protected boolean refreshing;
    protected State state;
    protected State lastState;
    protected int start;
    protected int size;
    protected boolean samePage;

    protected CollectionDatasource.CollectionChangeListener datasourceCollectionChangeListener;
    protected WeakCollectionChangeListener weakDatasourceCollectionChangeListener;

    protected Consumer<CollectionContainer.CollectionChangeEvent> containerCollectionChangeListener;
    protected com.haulmont.cuba.gui.model.impl.WeakCollectionChangeListener weakContainerCollectionChangeListener;

    protected List<BeforeRefreshListener> beforeRefreshListeners;

    private RowsCountTarget target;

    public WebRowsCount() {
        component = new CubaRowsCount();
        component.setStyleName(TABLE_ROWS_COUNT_STYLENAME);
    }

    @Inject
    public void setMessages(Messages messages) {
        this.messages = messages;
    }

    @Inject
    public void setIconResolver(IconResolver iconResolver) {
        // todo extract icon contants
        component.getFirstButton().setIcon(iconResolver.getIconResource("icons/rows-count-first.png"));
        component.getPrevButton().setIcon(iconResolver.getIconResource("icons/rows-count-prev.png"));
        component.getNextButton().setIcon(iconResolver.getIconResource("icons/rows-count-next.png"));
        component.getLastButton().setIcon(iconResolver.getIconResource("icons/rows-count-last.png"));
    }

    @Override
    public String getStyleName() {
        return StringUtils.normalizeSpace(super.getStyleName().replace(TABLE_ROWS_COUNT_STYLENAME, ""));
    }

    @Override
    public CollectionDatasource getDatasource() {
        return adapter instanceof DatasourceAdapter ? ((DatasourceAdapter) adapter).getDatasource() : null;
    }

    @Override
    public void setDatasource(CollectionDatasource datasource) {
        Preconditions.checkNotNullArgument(datasource, "datasource is null");

        if (adapter != null) {
            adapter.cleanup();
        }
        adapter = createDatasourceAdapter(datasource);

        initButtonListeners();
    }

    protected void initButtonListeners() {
        component.getCountButton().addClickListener(event -> onLinkClick());
        component.getPrevButton().addClickListener(event -> onPrevClick());
        component.getNextButton().addClickListener(event -> onNextClick());
        component.getFirstButton().addClickListener(event -> onFirstClick());
        component.getLastButton().addClickListener(event -> onLastClick());
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
    public RowsCountTarget getRowsCountTarget() {
        return target;
    }

    protected Adapter createAdapter(RowsCountTarget target) {
        if (target instanceof Table) {
            TableSource tableSource = ((Table) target).getTableSource();
            if (tableSource instanceof CollectionDatasourceTableAdapter) {
                return createDatasourceAdapter(((CollectionDatasourceTableAdapter) tableSource).getDatasource());
            } else if (tableSource instanceof CollectionContainerTableSource) {
                return createLoaderAdapter(((CollectionContainerTableSource) tableSource).getContainer());
            } else {
                throw new IllegalStateException("Unsupported TableSource: " + tableSource);
            }
        } else if (target instanceof DataGrid) {
            DataGridSource dataGridSource = ((DataGrid) target).getDataGridSource();
            if (dataGridSource instanceof CollectionDatasourceDataGridAdapter) {
                return createDatasourceAdapter(((CollectionDatasourceDataGridAdapter) dataGridSource).getDatasource());
            } else if (dataGridSource instanceof CollectionContainerDataGridSource) {
                return createLoaderAdapter(((CollectionContainerDataGridSource) dataGridSource).getContainer());
            } else {
                throw new IllegalStateException("Unsupported DataGridSource: " + dataGridSource);
            }
        } else {
            throw new UnsupportedOperationException("Unsupported RowsCountTarget: " + target);
        }
    }

    private Adapter createLoaderAdapter(CollectionContainer container) {
        DataLoader loader = null;
        if (container instanceof HasLoader) {
            loader = ((HasLoader) container).getLoader();
        }
        if (!(loader instanceof CollectionLoader)) {
            throw new IllegalStateException(String.format("Invalid loader for container %s: %s", container, loader));
        }
        return new LoaderAdapter((CollectionLoader) loader);
    }

    protected Adapter createDatasourceAdapter(CollectionDatasource datasource) {
        if (datasource instanceof CollectionDatasource.SupportsPaging) {
            return new DatasourceAdapter((CollectionDatasource.SupportsPaging) datasource);
        } else {
            throw new UnsupportedOperationException("Datasource must support paging");
        }
    }

    @Override
    public void setRowsCountTarget(RowsCountTarget target) {
        Preconditions.checkNotNullArgument(target, "target is null");
        this.target = target;

        if (adapter != null) {
            adapter.cleanup();
        }
        adapter = createAdapter(target);

        initButtonListeners();
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
        int firstResult = adapter.getFirstResult();
        int newStart = adapter.getFirstResult() - adapter.getMaxResults();
        adapter.setFirstResult(newStart < 0 ? 0 : newStart);
        if (refreshData()) {
            if (owner instanceof WebAbstractTable) {
                com.vaadin.v7.ui.Table vTable = (com.vaadin.v7.ui.Table) ((WebAbstractTable) owner).getComponent();
                vTable.setCurrentPageFirstItemIndex(0);
            }
        } else {
            adapter.setFirstResult(firstResult);
        }
    }

    protected void onNextClick() {
        int firstResult = adapter.getFirstResult();
        adapter.setFirstResult(adapter.getFirstResult() + adapter.getMaxResults());
        if (refreshData()) {
            if (state == State.LAST && size == 0) {
                adapter.setFirstResult(firstResult);
                int maxResults = adapter.getMaxResults();
                adapter.setMaxResults(maxResults + 1);
                refreshData();
                adapter.setMaxResults(maxResults);
            }
            if (owner instanceof WebAbstractTable) {
                com.vaadin.v7.ui.Table vTable = (com.vaadin.v7.ui.Table) ((WebAbstractTable) owner).getComponent();
                vTable.setCurrentPageFirstItemIndex(0);
            }
        } else {
            adapter.setFirstResult(firstResult);
        }
    }

    protected void onFirstClick() {
        int firstResult = adapter.getFirstResult();
        adapter.setFirstResult(0);
        if (refreshData()) {
            if (owner instanceof WebAbstractTable) {
                com.vaadin.v7.ui.Table vTable = (com.vaadin.v7.ui.Table) ((WebAbstractTable) owner).getComponent();
                vTable.setCurrentPageFirstItemIndex(0);
            }
        } else {
            adapter.setFirstResult(firstResult);
        }
    }

    protected void onLastClick() {
        int count = adapter.getCount();
        int itemsToDisplay = count % adapter.getMaxResults();
        if (itemsToDisplay == 0) itemsToDisplay = adapter.getMaxResults();

        int firstResult = adapter.getFirstResult();
        adapter.setFirstResult(count - itemsToDisplay);
        if (refreshData()) {
            if (owner instanceof WebAbstractTable) {
                com.vaadin.v7.ui.Table vTable = (com.vaadin.v7.ui.Table) ((WebAbstractTable) owner).getComponent();
                vTable.setCurrentPageFirstItemIndex(0);
            }
        } else {
            adapter.setFirstResult(firstResult);
        }
    }

    protected boolean refreshData() {
        if (beforeRefreshListeners != null) {
            boolean refreshPrevented = false;
            for (BeforeRefreshListener listener : beforeRefreshListeners) {
                BeforeRefreshEvent event = new BeforeRefreshEvent(this);
                listener.beforeDatasourceRefresh(event);
                refreshPrevented = refreshPrevented || event.isRefreshPrevented();
            }
            if (refreshPrevented)
                return false;
        }
        refreshing = true;
        try {
            adapter.refresh();
        } finally {
            refreshing = false;
        }
        return true;
    }

    protected void onLinkClick() {
        int count = adapter.getCount();
        component.getCountButton().setCaption(String.valueOf(count));
        component.getCountButton().addStyleName("c-paging-count-number");
        component.getCountButton().setEnabled(false);
    }

    protected void onCollectionChanged() {
        if (adapter == null) {
            return;
        }

        String msgKey;
        size = adapter.size();
        start = 0;

        boolean refreshSizeButton = false;
        if (samePage) {
            state = lastState == null ? State.FIRST_COMPLETE : lastState;
            start = adapter.getFirstResult();
            samePage = false;
            refreshSizeButton = State.LAST.equals(state);
        } else if ((size == 0 || size < adapter.getMaxResults()) && adapter.getFirstResult() == 0) {
            state = State.FIRST_COMPLETE;
            lastState = state;
        } else if (size == adapter.getMaxResults() && adapter.getFirstResult() == 0) {
            state = State.FIRST_INCOMPLETE;
            lastState = state;
        } else if (size == adapter.getMaxResults() && adapter.getFirstResult() > 0) {
            state = State.MIDDLE;
            start = adapter.getFirstResult();
            lastState = state;
        } else if (size < adapter.getMaxResults() && adapter.getFirstResult() > 0) {
            state = State.LAST;
            start = adapter.getFirstResult();
            lastState = state;
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
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        publish(VisibilityChangeEvent.class,
                new VisibilityChangeEvent(this, visible));
    }

    interface Adapter {
        void cleanup();
        int getFirstResult();
        int getMaxResults();
        void setFirstResult(int startPosition);
        void setMaxResults(int maxResults);
        int getCount();
        int size();
        void refresh();
    }

    protected class LoaderAdapter implements Adapter {

        private CollectionContainer container;
        private CollectionLoader loader;

        @SuppressWarnings("unchecked")
        public LoaderAdapter(CollectionLoader loader) {
            this.loader = loader;
            container = loader.getContainer();

            containerCollectionChangeListener = e -> {
                samePage = CollectionChangeType.REFRESH != e.getChangeType();
                onCollectionChanged();
            };

            weakContainerCollectionChangeListener = new com.haulmont.cuba.gui.model.impl.WeakCollectionChangeListener (
                    container, containerCollectionChangeListener);

            onCollectionChanged();
        }

        @Override
        public void cleanup() {
            weakContainerCollectionChangeListener.removeItself();
        }

        @Override
        public int getFirstResult() {
            return loader.getFirstResult();
        }

        @Override
        public int getMaxResults() {
            return loader.getMaxResults();
        }

        @Override
        public void setFirstResult(int startPosition) {
            loader.setFirstResult(startPosition);
        }

        @Override
        public void setMaxResults(int maxResults) {
            loader.setMaxResults(maxResults);
        }

        @SuppressWarnings("unchecked")
        @Override
        public int getCount() {
            return (int) dataManager.getCount(loader.createLoadContext());
        }

        @Override
        public int size() {
            return container.getItems().size();
        }

        @Override
        public void refresh() {
            loader.load();
        }
    }
    protected class DatasourceAdapter implements Adapter {

        private CollectionDatasource.SupportsPaging datasource;

        public DatasourceAdapter(CollectionDatasource.SupportsPaging datasource) {
            this.datasource = datasource;

            datasourceCollectionChangeListener = e -> {
                samePage = Operation.REFRESH != e.getOperation()
                        && Operation.CLEAR != e.getOperation();
                onCollectionChanged();
            };

            weakDatasourceCollectionChangeListener = new WeakCollectionChangeListener(datasource, datasourceCollectionChangeListener);
            //noinspection unchecked
            datasource.addCollectionChangeListener(weakDatasourceCollectionChangeListener);

            if (datasource.getState() == Datasource.State.VALID) {
                onCollectionChanged();
            }
        }

        @Override
        public void cleanup() {
            //noinspection unchecked
            datasource.removeCollectionChangeListener(weakDatasourceCollectionChangeListener);
            weakDatasourceCollectionChangeListener = null;
        }

        @Override
        public int getFirstResult() {
            return datasource.getFirstResult();
        }

        @Override
        public int getMaxResults() {
            return datasource.getMaxResults();
        }

        @Override
        public void setFirstResult(int startPosition) {
            datasource.setFirstResult(startPosition);
        }

        @Override
        public void setMaxResults(int maxResults) {
            datasource.setMaxResults(maxResults);
        }

        @Override
        public int getCount() {
            return datasource.getCount();
        }

        @Override
        public int size() {
            return datasource.size();
        }

        @Override
        public void refresh() {
            datasource.refresh();
        }

        public CollectionDatasource getDatasource() {
            return datasource;
        }
    }
}