package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.Entity;

import java.util.Collection;

public interface HierarchicalDatasource<T extends Entity, K> extends CollectionDatasource<T, K>{
    String getHierarchyPropertyName();
    void setHierarchyPropertyName(String parentPropertyName);

    Collection<K> getRootItemIds();

    K getParent(K itemId);
    Collection<K> getChildren(K itemId);

    boolean isRoot(K itemId);
    boolean hasChildren(K itemId);
}
