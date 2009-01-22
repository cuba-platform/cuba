/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 18:10:44
 * $Id$
 */
package com.haulmont.cuba.gui.data;

import java.util.Collection;

public interface CollectionDatasourceListener<T> extends DatasourceListener<T> {
    class CollectionOperation<T> {
        public enum Type {
            REFRESH,
            ADD,
            REMOVE
        }

        protected Type type;
        protected Collection<T> items;

        public CollectionOperation(Type type, Collection<T> items) {
            this.type = type;
            this.items = items;
        }

        public Type getType() {
            return type;
        }

        public Collection<T> getItems() {
            return items;
        }
    }

    void collectionChanged(Datasource<T> ds, CollectionOperation operation);
}
