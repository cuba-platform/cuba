package com.haulmont.cuba.gui.components.data;

import java.util.stream.Stream;

/**
 * A common interface for providing data for the {@link com.haulmont.cuba.gui.components.TreeDataGrid} component.
 *
 * @param <T> items type
 */
public interface TreeDataGridSource<T> extends DataGridSource.Sortable<T> {

    /**
     * @param parent the parent item
     * @return child count of the given parent item
     */
    int getChildCount(T parent);

    /**
     * @param item the item to obtain children or {@code null} to get root items
     * @return children of the given item
     */
    Stream<T> getChildren(T item);

    /**
     * @param item the item to check
     * @return {@code true} if the item has children, {@code false} otherwise
     */
    boolean hasChildren(T item);
}
