package com.haulmont.cuba.gui.components.data.datagrid;

import com.haulmont.bali.events.EventHub;
import com.haulmont.bali.events.Subscription;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.EntityDataGridSource;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsHelper;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionDatasourceDataGridAdapter<E extends Entity<K>, K> implements EntityDataGridSource<E> {

    protected CollectionDatasource.Indexed<E, K> datasource;
    protected EventHub events = new EventHub();

    protected BindingState state = BindingState.INACTIVE;

    public CollectionDatasourceDataGridAdapter(CollectionDatasource<E, K> datasource) {
        if (!(datasource instanceof CollectionDatasource.Indexed)) {
            throw new IllegalArgumentException("Datasource must implement " +
                    "com.haulmont.cuba.gui.data.CollectionDatasource.Indexed");
        }

        this.datasource = (CollectionDatasource.Indexed<E, K>) datasource;

        this.datasource.addStateChangeListener(this::datasourceStateChanged);
        this.datasource.addItemPropertyChangeListener(this::datasourceItemPropertyChanged);
        this.datasource.addCollectionChangeListener(this::datasourceCollectionChanged);
        this.datasource.addItemChangeListener(this::datasourceItemChanged);

        CollectionDsHelper.autoRefreshInvalid(datasource, true);

        if (datasource.getState() == Datasource.State.VALID) {
            setState(BindingState.ACTIVE);
        }
    }

    protected void datasourceItemChanged(Datasource.ItemChangeEvent<E> e) {
        events.publish(SelectedItemChangeEvent.class, new SelectedItemChangeEvent<>(this, e.getItem()));

    }

    protected void datasourceCollectionChanged(CollectionDatasource.CollectionChangeEvent<E, K> ignored) {
        events.publish(ItemSetChangeEvent.class, new ItemSetChangeEvent<>(this));
    }

    @SuppressWarnings("unchecked")
    protected void datasourceItemPropertyChanged(Datasource.ItemPropertyChangeEvent<E> e) {
        events.publish(ValueChangeEvent.class, new ValueChangeEvent(this,
                e.getItem(), e.getProperty(), e.getPrevValue(), e.getValue()));
    }

    protected void datasourceStateChanged(Datasource.StateChangeEvent<E> e) {
        if (e.getState() == Datasource.State.VALID) {
            setState(BindingState.ACTIVE);
        } else {
            setState(BindingState.INACTIVE);
        }
    }

    public CollectionDatasource<E, K> getDatasource() {
        return datasource;
    }

    @Override
    public MetaClass getEntityMetaClass() {
        return datasource.getMetaClass();
    }

    @Override
    public Collection<MetaPropertyPath> getAutowiredProperties() {
        MetadataTools metadataTools = AppBeans.get(MetadataTools.class);

        return datasource.getView() != null
                // if a view is specified - use view properties
                ? metadataTools.getViewPropertyPaths(datasource.getView(), datasource.getMetaClass())
                // otherwise use all properties from meta-class
                : metadataTools.getPropertyPaths(datasource.getMetaClass());
    }

    @Override
    public BindingState getState() {
        return state;
    }

    public void setState(BindingState state) {
        if (this.state != state) {
            this.state = state;

            events.publish(StateChangeEvent.class, new StateChangeEvent<>(this, state));
        }
    }

    @Override
    public Object getItemId(E item) {
        Preconditions.checkNotNullArgument(item);
        return item.getId();
    }

    @SuppressWarnings("unchecked")
    @Override
    public E getItem(@Nullable Object itemId) {
        return datasource.getItem((K) itemId);
    }

    @Override
    public int indexOfItem(E item) {
        Preconditions.checkNotNullArgument(item);
        return datasource.indexOfId(item.getId());
    }

    @Nullable
    @Override
    public E getItemByIndex(int index) {
        K id = datasource.getIdByIndex(index);
        return datasource.getItem(id);
    }

    @Override
    public Stream<E> getItems() {
        return datasource.getItems().stream();
    }

    @Override
    public List<E> getItems(int startIndex, int numberOfItems) {
        return datasource.getItemIds(startIndex, numberOfItems).stream()
                .map(id -> datasource.getItem(id))
                .collect(Collectors.toList());
    }

    @Override
    public boolean containsItem(E item) {
        return datasource.containsItem(item.getId());
    }

    @Override
    public int size() {
        return datasource.size();
    }

    @Override
    public E getSelectedItem() {
        return datasource.getItem();
    }

    @Override
    public void setSelectedItem(@Nullable E item) {
        datasource.setItem(item);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addStateChangeListener(Consumer<StateChangeEvent<E>> listener) {
        return events.subscribe(StateChangeEvent.class, (Consumer) listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addValueChangeListener(Consumer<ValueChangeEvent<E>> listener) {
        return events.subscribe(ValueChangeEvent.class, (Consumer) listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addItemSetChangeListener(Consumer<ItemSetChangeEvent<E>> listener) {
        return events.subscribe(ItemSetChangeEvent.class, (Consumer) listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addSelectedItemChangeListener(Consumer<SelectedItemChangeEvent<E>> listener) {
        return events.subscribe(SelectedItemChangeEvent.class, (Consumer) listener);
    }
}
