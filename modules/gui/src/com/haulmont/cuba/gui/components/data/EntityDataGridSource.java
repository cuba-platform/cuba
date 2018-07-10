package com.haulmont.cuba.gui.components.data;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;

import java.util.Collection;

/**
 * todo JavaDoc
 */
public interface EntityDataGridSource<E extends Entity> extends DataGridSource<E> {

    MetaClass getEntityMetaClass();

    // todo rename
    Collection<MetaPropertyPath> getAutowiredProperties();
}
