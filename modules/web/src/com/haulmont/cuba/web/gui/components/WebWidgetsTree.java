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

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.WidgetsTree;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.web.gui.data.HierarchicalDsWrapper;
import com.haulmont.cuba.web.toolkit.ui.CubaWidgetsTree;
import com.vaadin.ui.Tree;

public class WebWidgetsTree<E extends Entity> extends WebAbstractTree<CubaWidgetsTree, E> implements WidgetsTree<E> {

    protected String hierarchyProperty;

    public WebWidgetsTree() {
        component = new CubaWidgetsTree();
        component.setSelectable(false);
        component.setImmediate(true);
        component.setBeforePaintListener(() -> {
            Tree.ItemStyleGenerator generator = component.getItemStyleGenerator();
            if (generator instanceof WebAbstractTree.StyleGeneratorAdapter) {
                //noinspection unchecked
                ((StyleGeneratorAdapter) generator).resetExceptionHandledFlag();
            }
        });

        initComponent(component);
    }

    @Override
    protected ContextMenuButton createContextMenuButton() {
        //noinspection IncorrectCreateGuiComponent
        return new ContextMenuButton(showIconsForPopupMenuActions) {
            @Override
            protected void performAction(Action action) {
                action.actionPerform(WebWidgetsTree.this);
            }

            @Override
            protected void beforeActionPerformed() {
                WebWidgetsTree.this.component.hideContextMenuPopup();
            }
        };
    }

    @Override
    public String getHierarchyProperty() {
        return hierarchyProperty;
    }

    @Override
    public void setDatasource(HierarchicalDatasource datasource) {
        this.datasource = datasource;
        this.hierarchyProperty = datasource.getHierarchyPropertyName();

        HierarchicalDsWrapper wrapper = new HierarchicalDsWrapper(datasource);
        component.setContainerDataSource(wrapper);

        for (Action action : getActions()) {
            action.refreshState();
        }
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
        if (widgetBuilder != null) {
            component.setWidgetBuilder((CubaWidgetsTree.WidgetBuilder) (source, itemId, leaf) -> {
                Component widget = widgetBuilder.build((HierarchicalDatasource) datasource, itemId, leaf);
                return WebComponentsHelper.getComposition(widget);
            });
        } else {
            component.setWidgetBuilder(null);
        }
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
}