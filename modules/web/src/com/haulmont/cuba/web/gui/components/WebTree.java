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
package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.EntityTreeSource;
import com.haulmont.cuba.gui.components.data.TreeSource;
import com.haulmont.cuba.web.widgets.CubaTree;
import com.vaadin.event.selection.SelectionEvent;

import java.util.Set;
import java.util.function.Consumer;

public class WebTree<E extends Entity> extends WebAbstractTree<CubaTree<E>, E> {

    public WebTree() {
    }

    @Override
    protected CubaTree<E> createComponent() {
        return new CubaTree<>();
    }

    @Override
    public void initComponent(CubaTree<E> component) {
        super.initComponent(component);

        setSelectionMode(SelectionMode.SINGLE);
    }

    protected void onSelectionChange(SelectionEvent<E> event) {
        TreeSource<E> treeSource = getTreeSource();

        if (treeSource == null
                || treeSource.getState() == BindingState.INACTIVE) {
            return;
        }

        Set<E> selected = getSelected();
        if (treeSource instanceof EntityTreeSource) {
            if (selected.isEmpty()) {
                ((EntityTreeSource<E>) treeSource).setSelectedItem(null);
            } else {
                // reset selection and select new item
                if (isMultiSelect()) {
                    ((EntityTreeSource<E>) treeSource).setSelectedItem(null);
                }

                E newItem = selected.iterator().next();
                ((EntityTreeSource<E>) treeSource).setSelectedItem(newItem);
            }
        }

        LookupSelectionChangeEvent selectionChangeEvent = new LookupSelectionChangeEvent(this);
        publish(LookupSelectionChangeEvent.class, selectionChangeEvent);

        // todo implement selection change events
    }

    @Override
    public void setSelectionMode(SelectionMode selectionMode) {
        super.setSelectionMode(selectionMode);

        // Every time we change selection mode, the new selection model is set,
        // so we need to add selection listener again.
        if (!SelectionMode.NONE.equals(selectionMode)) {
            component.addSelectionListener(this::onSelectionChange);
        }
    }

    @Override
    public Subscription addLookupValueChangeListener(Consumer<LookupSelectionChangeEvent> listener) {
        return getEventHub().subscribe(LookupSelectionChangeEvent.class, listener);
    }

    @Override
    public void removeLookupValueChangeListener(Consumer<LookupSelectionChangeEvent> listener) {
        unsubscribe(LookupSelectionChangeEvent.class, listener);
    }
}