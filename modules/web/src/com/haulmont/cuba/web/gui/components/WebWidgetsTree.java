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
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.WidgetsTree;
import com.haulmont.cuba.web.widgets.CubaTree;
import com.haulmont.cuba.web.widgets.CubaWidgetsTree;

import java.util.function.Consumer;

public class WebWidgetsTree<E extends Entity> extends WebAbstractTree<CubaWidgetsTree<E>, E> implements WidgetsTree<E> {

    public WebWidgetsTree() {
    }

    @Override
    protected CubaWidgetsTree<E> createComponent() {
        return new CubaWidgetsTree<>();
    }

    @Override
    public void initComponent(CubaTree<E> component) {
        // TODO: gg, implement
        /*component.setBeforePaintListener(() -> {
            Tree.ItemStyleGenerator generator = component.getItemStyleGenerator();
            if (generator instanceof WebAbstractTree.StyleGeneratorAdapter) {
                //noinspection unchecked
                ((StyleGeneratorAdapter) generator).resetExceptionHandledFlag();
            }
        });*/

        super.initComponent(component);

        setSelectionMode(SelectionMode.NONE);
    }

    @Override
    public void setSelectionMode(SelectionMode selectionMode) {
        // TODO: gg, check if we need this
        throw new UnsupportedOperationException();
    }

    @Override
    public void setItemClickAction(Action action) {
        //do nothing
    }

    @Override
    public Action getItemClickAction() {
        return null;
    }

    @Override
    public void setWidgetBuilder(final WidgetBuilder widgetBuilder) {
        // TODO: gg, implement
        /*if (widgetBuilder != null) {
            component.setWidgetBuilder((CubaWidgetsTree.WidgetBuilder) (source, itemId, leaf) -> {
                Component widget = widgetBuilder.build((HierarchicalDatasource) datasource, itemId, leaf);
                return WebComponentsHelper.getComposition(widget);
            });
        } else {
            component.setWidgetBuilder(null);
        }*/
    }

    @Override
    public CaptionMode getCaptionMode() {
        return null;
    }

    @Override
    public void setCaptionMode(CaptionMode captionMode) {
        //do nothing
    }

    @Override
    public String getCaptionProperty() {
        return null;
    }

    @Override
    public void setCaptionProperty(String captionProperty) {
        //do nothing
    }

    @Override
    public Subscription addLookupValueChangeListener(Consumer<LookupSelectionChangeEvent> listener) {
        //do nothing
        return null;
    }

    @Override
    public void removeLookupValueChangeListener(Consumer<LookupSelectionChangeEvent> listener) {
        //do nothing
    }
}