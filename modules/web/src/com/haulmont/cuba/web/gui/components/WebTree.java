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

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.LookupComponent;
import com.haulmont.cuba.gui.components.sys.ShowInfoAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenersWrapper;
import com.haulmont.cuba.web.gui.data.HierarchicalDsWrapper;
import com.haulmont.cuba.web.widgets.CubaTree;
import com.vaadin.v7.event.ItemClickEvent;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.Tree;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class WebTree<E extends Entity> extends WebAbstractTree<CubaTree, E>
        implements LookupComponent.LookupSelectionChangeNotifier {

    protected String hierarchyProperty;
    protected CaptionMode captionMode = CaptionMode.ITEM;
    protected String captionProperty;

    protected Action doubleClickAction;
    protected ItemClickEvent.ItemClickListener itemClickListener;

    public WebTree() {
        component = new CubaTree();
        component.setMultiSelect(false);
        component.setBeforePaintListener(() -> {
            Tree.ItemStyleGenerator generator = component.getItemStyleGenerator();
            if (generator instanceof WebAbstractTree.StyleGeneratorAdapter) {
                //noinspection unchecked
                ((StyleGeneratorAdapter) generator).resetExceptionHandledFlag();
            }
        });

        component.setItemCaptionMode(ItemCaptionMode.ITEM);

        contextMenuPopup.setParent(component);
        component.setContextMenuPopup(contextMenuPopup);

        component.addValueChangeListener(event -> {
            if (datasource != null) {
                Set<E> selected = getSelected();
                if (selected.isEmpty()) {
                    Entity dsItem = datasource.getItemIfValid();
                    datasource.setItem(null);

                    if (dsItem == null) {
                        // in this case item change event will not be generated
                        refreshActionsState();
                    }
                } else {
                    // reset selection and select new item
                    if (isMultiSelect()) {
                        datasource.setItem(null);
                    }
                    Entity newItem = selected.iterator().next();
                    Entity dsItem = datasource.getItemIfValid();
                    datasource.setItem(newItem);

                    if (Objects.equals(dsItem, newItem)) {
                        // in this case item change event will not be generated
                        refreshActionsState();
                    }
                }
            }

            LookupSelectionChangeEvent lvChangeEvent = new LookupSelectionChangeEvent(this);
            getEventRouter().fireEvent(LookupSelectionChangeListener.class,
                    LookupSelectionChangeListener::lookupValueChanged, lvChangeEvent);
        });

        initComponent(component);
    }

    protected void refreshActionsState() {
        for (Action action : getActions()) {
            action.refreshState();
        }
    }

    @Override
    protected ContextMenuButton createContextMenuButton() {
        //noinspection IncorrectCreateGuiComponent
        return new ContextMenuButton(showIconsForPopupMenuActions) {
            @Override
            protected void beforeActionPerformed() {
                WebTree.this.component.hideContextMenuPopup();
            }

            @Override
            protected void performAction(Action action) {
                // do action for table component
                action.actionPerform(WebTree.this);
            }
        };
    }

    @Override
    public CaptionMode getCaptionMode() {
        return captionMode;
    }

    @Override
    public void setCaptionMode(CaptionMode captionMode) {
        if (this.captionMode != captionMode) {
            this.captionMode = captionMode;
            switch (captionMode) {
                case ITEM:
                    component.setItemCaptionMode(ItemCaptionMode.ITEM);
                    break;

                case PROPERTY:
                    component.setItemCaptionMode(ItemCaptionMode.PROPERTY);
                    break;

                default:
                    throw new UnsupportedOperationException();
            }
        }
    }

    @Override
    public String getCaptionProperty() {
        return captionProperty;
    }

    @Override
    public void setCaptionProperty(String captionProperty) {
        if (StringUtils.isEmpty(captionProperty)) {
            setCaptionMode(CaptionMode.ITEM);
        } else {
            setCaptionMode(CaptionMode.PROPERTY);
        }

        if (!Objects.equals(this.captionProperty, captionProperty)) {
            this.captionProperty = captionProperty;

            tryToAssignCaptionProperty();
        }
    }

    @Override
    public String getHierarchyProperty() {
        return hierarchyProperty;
    }

    protected void tryToAssignCaptionProperty() {
        if (datasource != null && captionProperty != null && captionMode == CaptionMode.PROPERTY) {
            MetaPropertyPath propertyPath = datasource.getMetaClass().getPropertyPath(captionProperty);

            if (propertyPath != null && component.getContainerDataSource() != null) {
                ((HierarchicalDsWrapper) component.getContainerDataSource()).addProperty(propertyPath);
                component.setItemCaptionPropertyId(propertyPath);
            } else {
                throw new IllegalArgumentException(String.format("Can't find property for given caption property: %s", captionProperty));
            }
        }
    }

    @Override
    public void setDatasource(HierarchicalDatasource datasource) {
        Preconditions.checkNotNullArgument(datasource, "datasource is null");

        if (this.datasource != null) {
            throw new UnsupportedOperationException("Changing datasource is not supported by the Tree component");
        }

        this.datasource = datasource;
        this.hierarchyProperty = datasource.getHierarchyPropertyName();
        collectionDsListenersWrapper = createCollectionDsListenersWrapper();

        component.setContainerDataSource(new HierarchicalDsWrapper(datasource, collectionDsListenersWrapper));

        tryToAssignCaptionProperty();

        Security security = AppBeans.get(Security.NAME);
        if (security.isSpecificPermitted(ShowInfoAction.ACTION_PERMISSION)) {
            ShowInfoAction action = (ShowInfoAction) getAction(ShowInfoAction.ACTION_ID);
            if (action == null) {
                action = new ShowInfoAction();
                addAction(action);
            }
            action.setDatasource(datasource);
        }

        collectionDsListenersWrapper.bind(datasource);

        for (Action action : getActions()) {
            action.refreshState();
        }
    }

    protected CollectionDsListenersWrapper createCollectionDsListenersWrapper() {
        return new TreeCollectionDsListenersWrapper();
    }

    @Override
    public Action getItemClickAction() {
        return doubleClickAction;
    }

    @Override
    public void setItemClickAction(Action action) {
        if (this.doubleClickAction != action) {
            if (action != null) {
                if (itemClickListener == null) {
                    component.setDoubleClickMode(true);
                    itemClickListener = event -> {
                        if (event.isDoubleClick() && !component.isReadOnly()) {
                            if (!component.isMultiSelect()) {
                                component.setValue(event.getItemId());
                            } else {
                                component.setValue(Collections.singletonList(event.getItemId()));
                            }

                            if (doubleClickAction != null) {
                                doubleClickAction.actionPerform(WebTree.this);
                            }
                        }
                    };
                    component.addItemClickListener(itemClickListener);
                }
            } else {
                component.setDoubleClickMode(false);
                component.removeItemClickListener(itemClickListener);
                itemClickListener = null;
            }

            this.doubleClickAction = action;
        }
    }

    @Override
    public void addLookupValueChangeListener(LookupSelectionChangeListener listener) {
        getEventRouter().addListener(LookupSelectionChangeListener.class, listener);
    }

    @Override
    public void removeLookupValueChangeListener(LookupSelectionChangeListener listener) {
        getEventRouter().removeListener(LookupSelectionChangeListener.class, listener);
    }

    public class TreeCollectionDsListenersWrapper extends CollectionDsListenersWrapper {

        @SuppressWarnings("unchecked")
        @Override
        public void collectionChanged(CollectionDatasource.CollectionChangeEvent e) {
            // replacement for collectionChangeSelectionListener
            // #PL-2035, reload selection from ds
            Set<Object> selectedItemIds = getSelectedItemIds();
            if (selectedItemIds == null) {
                selectedItemIds = Collections.emptySet();
            }

            Set<Object> newSelection = new HashSet<>();
            for (Object entityId : selectedItemIds) {
                if (e.getDs().containsItem(entityId)) {
                    newSelection.add(entityId);
                }
            }

            if (e.getDs().getState() == Datasource.State.VALID && e.getDs().getItem() != null) {
                newSelection.add(e.getDs().getItem().getId());
            }

            if (newSelection.isEmpty()) {
                setSelected((E) null);
            } else {
                setSelectedIds(newSelection);
            }

            super.collectionChanged(e);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void itemChanged(Datasource.ItemChangeEvent e) {
            for (Action action : getActions()) {
                action.refreshState();
            }

            super.itemChanged(e);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void itemPropertyChanged(Datasource.ItemPropertyChangeEvent e) {
            for (Action action : getActions()) {
                action.refreshState();
            }

            super.itemPropertyChanged(e);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void stateChanged(Datasource.StateChangeEvent e) {
            for (Action action : getActions()) {
                action.refreshState();
            }

            super.stateChanged(e);
        }
    }
}