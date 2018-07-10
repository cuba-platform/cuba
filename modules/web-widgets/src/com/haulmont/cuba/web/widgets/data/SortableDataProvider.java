package com.haulmont.cuba.web.widgets.data;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.server.SerializablePredicate;

public interface SortableDataProvider<T> extends DataProvider<T, SerializablePredicate<T>> {

    void sort(Object[] propertyId, boolean[] ascending);

    void resetSortOrder();
}
