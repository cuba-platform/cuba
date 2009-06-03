package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.chile.core.model.MetaClass;

/**
 * User: Nikolay Gorodnov
 * Date: 03.06.2009
 */
public abstract class TreeTableDatasourceWrapper<T extends Entity, K> 
        extends TreeDatasourceWrapper<T, K>
        implements TreeTableDatasource<T, K>
{
    protected TreeTableDatasourceWrapper(
            DsContext context,
            DataService dataservice,
            String id,
            MetaClass metaClass,
            String viewName
    ) {
        super(context, dataservice, id, metaClass, viewName);
    }
}
