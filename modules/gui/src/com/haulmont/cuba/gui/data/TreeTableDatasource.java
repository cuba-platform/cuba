package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.Entity;

/**
 * User: Nikolay Gorodnov
 * Date: 02.06.2009
 */
public interface TreeTableDatasource <T extends Entity, K>
        extends HierarchicalDatasource<T, K>
{
    boolean isCaption(K itemId);

    String getCaption(K itemId);
}
