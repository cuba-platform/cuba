package com.haulmont.cuba.gui.components;

/**
 * A callback interface for generating optional descriptions (tooltips) for an item.
 */
public interface DescriptionProvider<T> {

    /**
     * Gets a description for the {@code item}.
     *
     * @param item the item to get the description for
     * @return the description or {@code null} for no description
     */
    String getDescription(T item);
}