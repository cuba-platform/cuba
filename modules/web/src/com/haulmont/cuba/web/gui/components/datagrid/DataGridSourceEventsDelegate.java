package com.haulmont.cuba.web.gui.components.datagrid;

import com.haulmont.cuba.gui.components.data.DataGridSource;

/**
 *
 * {@link DataGridDataProvider} delegates event handling to an implementation of this interface.
 *
 * @param <I> items type
 */
public interface DataGridSourceEventsDelegate<I> {

    void dataGridSourceItemSetChanged(DataGridSource.ItemSetChangeEvent<I> event);

    void dataGridSourcePropertyValueChanged(DataGridSource.ValueChangeEvent<I> event);

    void dataGridSourceStateChanged(DataGridSource.StateChangeEvent<I> event);

    void dataGridSourceSelectedItemChanged(DataGridSource.SelectedItemChangeEvent<I> event);
}
