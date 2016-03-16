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

package com.haulmont.cuba.gui.components.filter.addcondition;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.KeyCombination.Key;
import com.haulmont.cuba.gui.components.filter.FilterHelper;
import com.haulmont.cuba.gui.components.filter.descriptor.AbstractConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.descriptor.HeaderConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.descriptor.PropertyConditionDescriptor;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Window for adding new filter condition
 *
 */
public class AddConditionWindow extends AbstractWindow {

    @Inject
    protected ComponentsFactory componentsFactory;

    @Inject
    protected ConditionDescriptorsDs conditionDescriptorsDs;

    @Inject
    protected TextField treeFilter;

    @Inject
    protected Tree tree;

    @Inject
    protected Button cancelBtn;

    @Inject
    protected Button selectBtn;

    @Inject
    protected ThemeConstantsManager themeConstantsManager;

    @Inject
    protected Metadata metadata;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        ThemeConstants theme = themeConstantsManager.getConstants();
        getDialogParams()
                .setHeight(Integer.valueOf(theme.get("cuba.gui.addFilterCondition.dialog.height")))
                .setWidth(Integer.valueOf(theme.get("cuba.gui.addFilterCondition.dialog.width")))
                .setResizable(true);
        conditionDescriptorsDs.refresh(params);
        expandTreeRoots();
        tree.setItemClickAction(new AbstractAction("select") {
            @Override
            public void actionPerform(Component component) {
                select();
            }
        });

        FilterHelper filterHelper = AppBeans.get(FilterHelper.class);
        filterHelper.addTextChangeListener(treeFilter, new FilterHelper.TextChangeListener() {
            @Override
            public void textChanged(String text) {
                _search(text);
            }
        });

        filterHelper.addShortcutListener(treeFilter, new FilterHelper.ShortcutListener("search", new KeyCombination(Key.ENTER)) {
            @Override
            public void handleShortcutPressed() {
                search();
            }
        });
    }

    public void expandTreeRoots() {
        Collection<UUID> rootItemIds = conditionDescriptorsDs.getRootItemIds();
        for (UUID rootItemId : rootItemIds) {
            tree.expand(rootItemId);
        }
    }

    public void search() {
        String filterValue = treeFilter.getValue();
        _search(filterValue);
    }

    protected void _search(String filterValue) {
        conditionDescriptorsDs.setFilter(filterValue);
        tree.expandTree();
    }

    public void select() {
        Set<Entity> selectedItems = tree.getSelected();
        if (selectedItems.isEmpty()) {
            showNotification(messages.getMainMessage("filter.addCondition.selectCondition"), NotificationType.WARNING);
            return;
        } else {
            for (Entity item : selectedItems) {
                if (item instanceof HeaderConditionDescriptor) {
                    showNotification(messages.getMainMessage("filter.addCondition.youSelectedGroup"), NotificationType.WARNING);
                    return;
                } else if (isEmbeddedProperty((AbstractConditionDescriptor) item)) {
                    showNotification(messages.getMainMessage("filter.addCondition.youSelectedEmbedded"), NotificationType.WARNING);
                    return;
                }
            }
        }

        close(COMMIT_ACTION_ID);
    }

    protected boolean isEmbeddedProperty(AbstractConditionDescriptor item) {
        if (item instanceof PropertyConditionDescriptor) {
            MetaProperty metaProperty = ((PropertyConditionDescriptor) item).getMetaProperty();
            if (metaProperty != null && metadata.getTools().isEmbedded(metaProperty)) {
                return true;
            }
        }
        return false;
    }

    public void cancel() {
        close(CLOSE_ACTION_ID);
    }

    public Collection<AbstractConditionDescriptor> getDescriptors() {
        return tree.getSelected();
    }
}
