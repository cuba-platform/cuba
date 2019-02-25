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

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.DataUnit;
import com.haulmont.cuba.gui.components.data.meta.ContainerDataUnit;
import com.haulmont.cuba.gui.components.data.meta.DatasourceDataUnit;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.CollectionDatasource.Operation;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.WeakCollectionChangeListener;
import com.haulmont.cuba.gui.model.*;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.widgets.CubaRowsCount;
import com.vaadin.shared.Registration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public class WebRowsCount extends WebAbstractComponent<CubaRowsCount> implements RowsCount, VisibilityChangeNotifier,
        Component.HasXmlDescriptor {

    protected static final String TABLE_ROWS_COUNT_STYLENAME = "c-table-rows-count";

    private static final Logger log = LoggerFactory.getLogger(WebRowsCount.class);

    protected Messages messages;

    protected Adapter adapter;

    @Inject
    protected DataManager dataManager;

    protected boolean refreshing;
    protected State state;
    protected State lastState;
    protected int start;
    protected int size;
    protected boolean samePage;

    protected RowsCountTarget target;

    protected Registration onLinkClickRegistration;
    protected Registration onPrevClickRegistration;
    protected Registration onNextClickRegistration;
    protected Registration onFirstClickRegistration;
    protected Registration onLastClickRegistration;

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
        // todo extract icon constants
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
        checkNotNullArgument(datasource, "datasource is null");

        if (adapter != null) {
            adapter.unbind();
        }
        adapter = createDatasourceAdapter(datasource);

        initButtonListeners();
    }

    protected void initButtonListeners() {
        unregisterListeners();
        onLinkClickRegistration = component.getCountButton().addClickListener(event -> onLinkClick());
        onPrevClickRegistration = component.getPrevButton().addClickListener(event -> onPrevClick());
        onNextClickRegistration = component.getNextButton().addClickListener(event -> onNextClick());
        onFirstClickRegistration = component.getFirstButton().addClickListener(event -> onFirstClick());
        onLastClickRegistration = component.getLastButton().addClickListener(event -> onLastClick());
    }

    protected void unregisterListeners() {
        if (onLinkClickRegistration != null)
            onLinkClickRegistration.remove();

        if (onPrevClickRegistration != null)
            onPrevClickRegistration.remove();

        if (onNextClickRegistration != null)
            onNextClickRegistration.remove();

        if (onFirstClickRegistration != null)
            onFirstClickRegistration.remove();

        if (onLastClickRegistration != null)
            onLastClickRegistration.remove();
    }

    @Override
    public ListComponent getOwner() {
        if (target instanceof ListComponent) {
            return (ListComponent) target;
        }
        return null;
    }

    @Override
    public void setOwner(ListComponent owner) {
        if (owner instanceof RowsCountTarget) {
            this.target = (RowsCountTarget) owner;
        }
    }

    @Override
    public RowsCountTarget getRowsCountTarget() {
        return target;
    }

    protected Adapter createAdapter(RowsCountTarget target) {
        if (target instanceof ListComponent) {
            DataUnit items = ((ListComponent) target).getItems();
            if (items instanceof DatasourceDataUnit) {
                return createDatasourceAdapter(((DatasourceDataUnit) items).getDatasource());
            } else if (items instanceof ContainerDataUnit) {
                return createLoaderAdapter(((ContainerDataUnit) items).getContainer());
            }

            throw new IllegalStateException("Unsupported data unit type: " + items);
        }

        throw new UnsupportedOperationException("Unsupported RowsCountTarget: " + target);
    }

    protected Adapter createLoaderAdapter(CollectionContainer container) {
        DataLoader loader = null;
        if (container instanceof HasLoader) {
            loader = ((HasLoader) container).getLoader();
        }
        if (loader != null && !(loader instanceof BaseCollectionLoader)) {
            throw new IllegalStateException("RowsCount component currently supports only BaseCollectionLoader");
        }
        return new LoaderAdapter(container, (BaseCollectionLoader) loader);
    }

    protected Adapter createDatasourceAdapter(CollectionDatasource datasource) {
        if (datasource instanceof CollectionDatasource.SupportsPaging) {
            return new DatasourceAdapter((CollectionDatasource.SupportsPaging) datasource);
        } else {
            throw new UnsupportedOperationException("Datasource class does not support paging: " + datasource.getClass());
        }
    }

    @Override
    public void setRowsCountTarget(RowsCountTarget target) {
        checkNotNullArgument(target, "target is null");

        if (!(target instanceof ListComponent)) {
            throw new UnsupportedOperationException("Unsupported RowsCountTarget: " + target);
        }

        this.target = target;

        if (((ListComponent) target).getItems() != null) {
            if (adapter != null) {
                adapter.unbind();
            }
            adapter = createAdapter(target);
        }

        initButtonListeners();
    }

    @Override
    public void addBeforeRefreshListener(Consumer<BeforeRefreshEvent> listener) {
        getEventHub().subscribe(BeforeRefreshEvent.class, listener);
    }

    @Override
    public void removeBeforeRefreshListener(Consumer<BeforeRefreshEvent> listener) {
        unsubscribe(BeforeRefreshEvent.class, listener);
    }

    protected void onPrevClick() {
        int firstResult = adapter.getFirstResult();
        int newStart = adapter.getFirstResult() - adapter.getMaxResults();
        adapter.setFirstResult(newStart < 0 ? 0 : newStart);
        if (refreshData()) {
            if (target instanceof WebAbstractTable) {
                resetCurrentDataPage((Table) target);
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
            if (target instanceof WebAbstractTable) {
                resetCurrentDataPage((Table) target);
            }
        } else {
            adapter.setFirstResult(firstResult);
        }
    }

    protected void onFirstClick() {
        int firstResult = adapter.getFirstResult();
        adapter.setFirstResult(0);
        if (refreshData()) {
            if (target instanceof WebAbstractTable) {
                resetCurrentDataPage((Table) target);
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
            if (target instanceof WebAbstractTable) {
                resetCurrentDataPage((Table) target);
            }
        } else {
            adapter.setFirstResult(firstResult);
        }
    }

    @SuppressWarnings("deprecation")
    protected void resetCurrentDataPage(Table table) {
        com.vaadin.v7.ui.Table vTable = table.unwrap(com.vaadin.v7.ui.Table.class);
        vTable.setCurrentPageFirstItemIndex(0);
    }

    protected boolean refreshData() {
        if (hasSubscriptions(BeforeRefreshEvent.class)) {
            BeforeRefreshEvent event = new BeforeRefreshEvent(this);

            publish(BeforeRefreshEvent.class, event);

            if (event.isRefreshPrevented()) {
                return false;
            }
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
        component.getCountButton().setCaption(String.valueOf(count)); // todo rework with datatype
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

    @Override
    public Subscription addVisibilityChangeListener(Consumer<VisibilityChangeEvent> listener) {
        return getEventHub().subscribe(VisibilityChangeEvent.class, listener);
    }

    @Override
    public void removeVisibilityChangeListener(Consumer<VisibilityChangeEvent> listener) {
        unsubscribe(VisibilityChangeEvent.class, listener);
    }

    public interface Adapter {
        void unbind();

        int getFirstResult();
        int getMaxResults();
        void setFirstResult(int startPosition);
        void setMaxResults(int maxResults);
        int getCount();
        int size();
        void refresh();
    }

    protected class LoaderAdapter implements Adapter {

        protected CollectionContainer container;

        protected Consumer<CollectionContainer.CollectionChangeEvent> containerCollectionChangeListener;
        protected com.haulmont.cuba.gui.model.impl.WeakCollectionChangeListener weakContainerCollectionChangeListener;

        @Nullable
        protected BaseCollectionLoader loader;

        @SuppressWarnings("unchecked")
        public LoaderAdapter(CollectionContainer container, @Nullable BaseCollectionLoader loader) {
            this.container = container;
            this.loader = loader;

            containerCollectionChangeListener = e -> {
                samePage = CollectionChangeType.REFRESH != e.getChangeType();
                onCollectionChanged();
            };

            weakContainerCollectionChangeListener = new com.haulmont.cuba.gui.model.impl.WeakCollectionChangeListener (
                    container, containerCollectionChangeListener);

            onCollectionChanged();
        }

        @Override
        public void unbind() {
            weakContainerCollectionChangeListener.removeItself();
        }

        @Override
        public int getFirstResult() {
            return loader != null ? loader.getFirstResult() : 0;
        }

        @Override
        public int getMaxResults() {
            return loader != null ? loader.getMaxResults() : Integer.MAX_VALUE;
        }

        @Override
        public void setFirstResult(int startPosition) {
            if (loader != null)
                loader.setFirstResult(startPosition);
        }

        @Override
        public void setMaxResults(int maxResults) {
            if (loader != null)
                loader.setMaxResults(maxResults);
        }

        @SuppressWarnings("unchecked")
        @Override
        public int getCount() {
            if (loader == null) {
                return container.getItems().size();
            }

            if (loader instanceof CollectionLoader) {
                return (int) dataManager.getCount(((CollectionLoader) loader).createLoadContext());
            } else if (loader instanceof KeyValueCollectionLoader) {
                ValueLoadContext context = ((KeyValueCollectionLoader) loader).createLoadContext();
                QueryTransformer transformer = QueryTransformerFactory.createTransformer(context.getQuery().getQueryString());
                // TODO it doesn't work for query containing scalars in select
                transformer.replaceWithCount();
                context.getQuery().setQueryString(transformer.getResult());
                context.setProperties(Collections.singletonList("cnt"));
                List<KeyValueEntity> list = dataManager.loadValues(context);
                Number count = list.get(0).getValue("cnt");
                return count == null ? 0 : count.intValue();
            } else {
                log.warn("Unsupported loader type: {}", loader.getClass().getName());
                return 0;
            }
        }

        @Override
        public int size() {
            return container.getItems().size();
        }

        @Override
        public void refresh() {
            if (loader != null)
                loader.load();
        }
    }

    protected class DatasourceAdapter implements Adapter {

        protected CollectionDatasource.SupportsPaging datasource;

        protected CollectionDatasource.CollectionChangeListener datasourceCollectionChangeListener;
        protected WeakCollectionChangeListener weakDatasourceCollectionChangeListener;

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
        public void unbind() {
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