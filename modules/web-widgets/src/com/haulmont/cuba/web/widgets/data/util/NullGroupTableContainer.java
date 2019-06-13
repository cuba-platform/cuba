/*
 * Copyright (c) 2008-2018 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.haulmont.cuba.web.widgets.data.util;

import com.haulmont.cuba.web.widgets.data.AggregationContainer;
import com.haulmont.cuba.web.widgets.data.GroupTableContainer;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.ContainerOrderedWrapper;

import java.util.Collection;
import java.util.Map;

@SuppressWarnings("deprecation")
public class NullGroupTableContainer extends ContainerOrderedWrapper
        implements GroupTableContainer, AggregationContainer {

    public static final String ERROR_MESSAGE = "Wrapped container is not GroupTableContainer";

    public NullGroupTableContainer(Container groupTableContainer) {
        super(groupTableContainer);
    }

    @Override
    public void groupBy(Object[] properties) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public boolean isGroup(Object id) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public Collection<?> rootGroups() {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public boolean hasChildren(Object id) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public Collection<?> getChildren(Object id) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public boolean hasGroups() {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public Object getGroupProperty(Object itemId) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public Object getGroupPropertyValue(Object itemId) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public Collection<?> getGroupItemIds(Object itemId) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public int getGroupItemsCount(Object itemId) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public Collection<?> getGroupProperties() {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public void expand(Object id) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public boolean isExpanded(Object id) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public void expandAll() {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public void collapseAll() {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public void collapse(Object id) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public Collection getAggregationPropertyIds() {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public void addContainerPropertyAggregation(Object propertyId, Type type) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public void removeContainerPropertyAggregation(Object propertyId) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public Map<Object, Object> aggregate(Context context) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public Map<Object, Object> aggregateValues(Context context) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public void sort(Object[] propertyId, boolean[] ascending) {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public Collection<?> getSortableContainerPropertyIds() {
        throw new IllegalStateException(ERROR_MESSAGE);
    }

    @Override
    public void resetSortOrder() {
        throw new IllegalStateException(ERROR_MESSAGE);
    }
}