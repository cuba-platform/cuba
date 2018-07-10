package com.haulmont.cuba.gui.components;

/**
 * A callback interface for generating custom CSS class names for items.
 */
public interface StyleProvider<T> {
    /**
     * Gets a class name for the {@code item}.
     *
     * @param item the item to get the class name for
     * @return style name or null to apply the default
     */
    String getStyleName(T item);
}