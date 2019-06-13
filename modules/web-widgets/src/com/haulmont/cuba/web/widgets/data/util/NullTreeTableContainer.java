/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */

package com.haulmont.cuba.web.widgets.data.util;

import com.haulmont.cuba.web.widgets.data.AggregationContainer;
import com.haulmont.cuba.web.widgets.data.TreeTableContainer;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.ContainerHierarchicalWrapper;

import java.util.*;

@SuppressWarnings("deprecation")
public class NullTreeTableContainer extends ContainerHierarchicalWrapper
        implements TreeTableContainer, AggregationContainer, Container.Ordered {

    public static final String ERROR_MESSAGE = "Wrapped container is not TreeTableContainer";

    protected Set<Object> expanded; // Contains expanded items ids

    protected LinkedList<Object> inline; // Contains visible (including children of expanded items) items ids inline

    protected Map<Object, String> captions;

    protected Object first;

    public NullTreeTableContainer(Container toBeWrapped) {
        super(toBeWrapped);

        inline = new LinkedList<>();
        expanded = new HashSet<>();
        captions = new HashMap<>();
    }

    @Override
    public void updateHierarchicalWrapper() {
        super.updateHierarchicalWrapper();

        updateFirst();

        if (inline == null || expanded == null || captions == null) {
            inline = new LinkedList<>();
            expanded = new HashSet<>();
            captions = new HashMap<>();
        } else {
            inline.clear();

            Set<Object> s = new HashSet<>();
            s.addAll(expanded);
            s.addAll(captions.keySet());
            for (Object o : s) {
                if (!_container().containsId(o)) {
                    expanded.remove(o);
                    captions.remove(o);
                }
            }
        }
        makeInlineElements(inline, rootItemIds());
    }

    @Override
    protected void addToHierarchyWrapper(Object itemId) {
        super.addToHierarchyWrapper(itemId);

        // Add item to the end of the list
        if (!inline.contains(itemId)) {
            inline.add(itemId);
            if (areChildrenAllowed(itemId)) {
                makeInlineElements(inline, getChildren(itemId));
            }
        }
    }

    @Override
    protected void removeFromHierarchyWrapper(Object itemId) {
        boolean b = isFirstId(itemId);

        if (containsInline(itemId)) {
            List<Object> inlineChildren;
            if (areChildrenAllowed(itemId)
                    && (inlineChildren = getInlineChildren(itemId)) != null) {
                inline.removeAll(inlineChildren);
            }
            inline.remove(itemId);
        }

        super.removeFromHierarchyWrapper(itemId);

        if (b) {
            updateFirst();
        }
    }

    @Override
    public boolean setParent(Object itemId, Object newParentId) {
        if (itemId == null) {
            throw new IllegalArgumentException("Item id cannot be NULL");
        }

        if (!_container().containsId(itemId)) {
            return false;
        }

        Object oldParentId = getParent(itemId);

        if ((newParentId == null && oldParentId == null)
                || (newParentId != null && newParentId.equals(oldParentId))) {
            return true;
        }

        boolean b = super.setParent(itemId, newParentId);
        if (b) {
            LinkedList<Object> inlineList = new LinkedList<>();
            inlineList.add(itemId);
            inlineList.addAll(getInlineChildren(itemId));

            if (containsInline(itemId)) {
                inline.removeAll(inlineList);
            }

            if (containsInline(newParentId)
                    && areChildrenAllowed(newParentId)
                    && isExpanded(newParentId)) {
                int lastChildInlineIndex = lastInlineIndex(newParentId);
                if (lastChildInlineIndex > -1) {
                    inline.addAll(lastChildInlineIndex + 1, inlineList);
                } else {
                    inline.addAll(inlineIndex(newParentId) + 1, inlineList);
                }
            }
        }
        return b;
    }

    @Override
    public int size() {
        return inline.size();
    }

    @Override
    public Object nextItemId(Object itemId) {
        if (itemId == null) {
            return null;
        }
        int index = inlineIndex(itemId);
        if (index == -1 || isLastId(itemId)) {
            return null;
        }
        return inline.get(index + 1);
    }

    @Override
    public Object prevItemId(Object itemId) {
        if (itemId == null) {
            return null;
        }
        int index = inlineIndex(itemId);
        if (index == -1 || isFirstId(itemId)) {
            return null;
        }
        return inline.get(index - 1);
    }

    @Override
    public Object firstItemId() {
        return first;
    }

    @Override
    public Object lastItemId() {
        return inline.peekLast();
    }

    @Override
    public boolean isFirstId(Object itemId) {
        return itemId != null && itemId.equals(first);
    }

    @Override
    public boolean isLastId(Object itemId) {
        return itemId != null && itemId.equals(lastItemId());
    }

    @Override
    public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLevel(Object itemId) {
        throw new IllegalArgumentException(ERROR_MESSAGE);
    }

    public boolean isExpanded(Object itemId) {
        if (itemId == null) {
            throw new IllegalArgumentException("Item id cannot be NULL");
        }
        return expanded.contains(itemId);
    }

    public boolean setExpanded(Object itemId) {
        if (itemId == null) {
            throw new IllegalArgumentException("Item id cannot be NULL");
        }

        if (areChildrenAllowed(itemId)) {
            if (isExpanded(itemId)) {
                return true;
            }
            expanded.add(itemId);

            int itemIndex;
            if ((itemIndex = inlineIndex(itemId)) > -1) {
                final List<Object> inlineChildren = getInlineChildren(itemId);
                if (inlineChildren != null) {
                    inline.addAll(itemIndex + 1, inlineChildren);
                }
            }

            return true;
        }
        return false;
    }

    public void expandAll() {
        collapseAll();
        if (_hierarchical()) {
            expandAll(rootItemIds());
        } else {
            if (_children() != null) {
                for (Object itemId : _children().keySet()) {
                    setExpanded(itemId);
                }
            }
        }
    }

    protected void expandAll(Collection itemIds) {
        for (Object itemId : itemIds) {
            if (areChildrenAllowed(itemId) && hasChildren(itemId)) {
                setExpanded(itemId);
                expandAll(getChildren(itemId));
            }
        }
    }

    public void collapseAll() {
        expanded.clear();
        inline.clear();
        makeInlineElements(inline, rootItemIds());
    }

    protected LinkedList<Object> getInlineChildren(Object itemId) {
        if (areChildrenAllowed(itemId)) {
            LinkedList<Object> inlineChildren = new LinkedList<>();
            if (isExpanded(itemId)) {
                makeInlineElements(inlineChildren, getChildren(itemId));
            }
            return inlineChildren;
        }
        return null;
    }

    private void makeInlineElements(List<Object> inline, Collection elements) {
        if (elements != null) {
            for (Object e : elements) {
                inline.add(e);
                if (areChildrenAllowed(e) && isExpanded(e)) {
                    makeInlineElements(inline, getChildren(e));
                }
            }
        }
    }

    private void updateFirst() {
        Collection roots = rootItemIds();
        if (roots.size() > 0) {
            first = roots.iterator().next();
        } else {
            first = null;
        }
    }

    private boolean containsInline(Object itemId) {
        return inline.contains(itemId);
    }

    private int inlineIndex(Object itemId) {
        return inline.indexOf(itemId);
    }

    private int lastInlineIndex(Object itemId) {
        LinkedList<Object> inlineChildren = getInlineChildren(itemId);
        if (inlineChildren != null && !inlineChildren.isEmpty()) {
            return inlineIndex(inlineChildren.getLast());
        }
        return -1;
    }

    @Override
    public void sort(Object[] propertyId, boolean[] ascending) {
        throw new IllegalArgumentException(ERROR_MESSAGE);
    }

    @Override
    public Collection getSortableContainerPropertyIds() {
        throw new IllegalArgumentException(ERROR_MESSAGE);
    }

    @Override
    public Collection getAggregationPropertyIds() {
        throw new IllegalStateException("Wrapped container is not AggregationContainer: " + _container().getClass());
    }

    @Override
    public void addContainerPropertyAggregation(Object propertyId, Type type) {
        throw new IllegalStateException("Wrapped container is not AggregationContainer: " + _container().getClass());
    }

    @Override
    public void removeContainerPropertyAggregation(Object propertyId) {
        throw new IllegalStateException("Wrapped container is not AggregationContainer: " + _container().getClass());
    }

    @Override
    public Map<Object, Object> aggregate(Context context) {
        throw new IllegalStateException("Wrapped container is not AggregationContainer: " + _container().getClass());
    }

    @Override
    public Map<Object, Object> aggregateValues(Context context) {
        throw new IllegalStateException("Wrapped container is not AggregationContainer: " + _container().getClass());
    }

    @Override
    public void resetSortOrder() {
        throw new IllegalArgumentException(ERROR_MESSAGE);
    }
}