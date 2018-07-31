package com.haulmont.cuba.gui.components.data;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;

import java.util.Collection;

/**
 * A common interface for providing data for the {@link com.haulmont.cuba.gui.components.DataGrid} component.
 *
 * @param <E> items type, extends {@link Entity}.
 */
public interface EntityDataGridSource<E extends Entity> extends DataGridSource<E> {

    /**
     * @return {@link MetaClass} of an entity contained in the source
     */
    MetaClass getEntityMetaClass();

    // todo rename
    Collection<MetaPropertyPath> getAutowiredProperties();
}
