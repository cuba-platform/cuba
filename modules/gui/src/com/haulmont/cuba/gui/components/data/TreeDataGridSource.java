package com.haulmont.cuba.gui.components.data;

import java.util.stream.Stream;

public interface TreeDataGridSource<T> extends DataGridSource.Sortable<T> {

    int getChildCount(T parent);

    Stream<T> getChildren(T item);

    boolean hasChildren(T item);
}
