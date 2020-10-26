/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.gui.actions.list;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.ActionType;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.data.meta.EntityDataUnit;
import com.haulmont.cuba.gui.components.filter.FilterHelper;
import com.haulmont.cuba.gui.components.sys.ValuePathHelper;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.meta.StudioAction;
import com.haulmont.cuba.gui.screen.MapScreenOptions;
import com.haulmont.cuba.gui.screen.OpenMode;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * An action that adds a record to the set.
 */
@StudioAction(category = "List Actions",
        description = "Adds the selected entity to a record set to be displayed in the folders pane of the main screen")
@ActionType(AddToSetAction.ID)
public class AddToSetAction extends SecuredListAction {

    public static final String ID = "addToSet";

    protected Filter filter;

    protected FilterHelper filterHelper;

    public AddToSetAction() {
        this(ID);
    }

    public AddToSetAction(String id) {
        super(id);
    }

    @Inject
    public void setFilterHelper(FilterHelper filterHelper) {
        this.filterHelper = filterHelper;
    }

    @Inject
    public void setIcons(Icons icons) {
        this.icon = icons.get(CubaIcon.ADD_TO_SET_ACTION);
    }

    @Inject
    public void setMessages(Messages messages) {
        this.caption = messages.getMainMessage("actions.AddToSet");
    }

    @Override
    public void actionPerform(Component component) {
        MetaClass entityMetaClass;
        if (target.getItems() instanceof EntityDataUnit) {
            entityMetaClass = ((EntityDataUnit) target.getItems()).getEntityMetaClass();
        } else {
            throw new UnsupportedOperationException("Unsupported data unit " + target.getItems());
        }

        String query;
        if (filter.getDatasource() != null) {
            query = filter.getDatasource().getQuery();
        } else {
            query = filter.getDataLoader().getQuery();
        }

        String[] strings = ValuePathHelper.parse(ComponentsHelper.getFilterComponentPath(filter));
        String componentId = ValuePathHelper.pathSuffix(strings);
        Set ownerSelection = target.getSelected();

        Map<String, Object> params = new HashMap<>();
        params.put("entityType", entityMetaClass.getName());
        params.put("items", ownerSelection);
        params.put("componentPath", ComponentsHelper.getFilterComponentPath(filter));
        params.put("componentId", componentId);
        params.put("foldersPane", filterHelper.getFoldersPane());
        params.put("entityClass", entityMetaClass.getJavaClass().getName());
        params.put("query", query);

        Screens screens = ComponentsHelper.getScreenContext(filter).getScreens();
        screens.create("saveSetInFolder", OpenMode.DIALOG, new MapScreenOptions(params))
                .show();
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public Filter getFilter() {
        return filter;
    }

    @Override
    protected boolean isApplicable() {
        return super.isApplicable()
                && (filter != null || searchFilter());
    }

    protected boolean searchFilter() {
        if (target == null) {
            return false;
        }

        return ComponentsHelper.walkComponents(target.getFrame(), component -> {
            if (component instanceof Filter
                    && Objects.equals(((Filter) component).getApplyTo(), target)) {
                setFilter((Filter) component);
                return true;
            }
            return false;
        });
    }

    @Override
    protected boolean isPermitted() {
        return super.isPermitted()
                && filterHelper.isTableActionsEnabled()
                && filterHelper.mainScreenHasFoldersPane(target.getFrame());
    }
}
