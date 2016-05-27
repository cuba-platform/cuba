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

package com.haulmont.cuba.gui.components.filter.edit;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.filter.FilterHelper;
import com.haulmont.cuba.gui.components.filter.Param;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.theme.ThemeConstants;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class ConditionFrame<T extends AbstractCondition> extends AbstractFrame {

    @Inject
    protected ClientConfig clientConfig;

    @Inject
    protected ThemeConstants theme;

    protected T condition;

    protected Filter filter;

    protected Component defaultValueComponent;
    protected CheckBox required;
    protected CheckBox hidden;
    protected LookupField width;
    protected BoxLayout defaultValueLayout;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        this.filter = (Filter) params.get("filter");
        initComponents();
        T conditionParam = (T) params.get("condition");
        if (conditionParam != null) {
            setCondition(conditionParam);
        }
    }

    protected void initComponents() {
        required = (CheckBox) getComponent("required");
        hidden = (CheckBox) getComponent("hidden");
        width = (LookupField) getComponent("width");

        if (width != null) {
            List<Integer> widthValues = new ArrayList<>();
            int conditionsColumnsQty = filter != null ? filter.getColumnsCount() : clientConfig.getGenericFilterColumnsCount();
            for (int i = 1; i <= conditionsColumnsQty; i++) {
                widthValues.add(i);
            }
            width.setOptionsList(widthValues);
            FilterHelper filterHelper = AppBeans.get(FilterHelper.class);
            filterHelper.setLookupNullSelectionAllowed(width, false);
        }
    }

    public void setCondition(T condition) {
        this.condition = condition;

        if (hidden != null) {
            hidden.setValue(condition.getHidden());
        }
        if (required != null) {
            required.setValue(condition.getRequired());
        }
        if (width != null) {
            width.setValue(condition.getWidth());
        }

        createDefaultValueComponent();
    }

    protected void createDefaultValueComponent() {
        defaultValueLayout = (BoxLayout) getComponent("defaultValueLayout");
        if (defaultValueLayout != null) {
            if (defaultValueComponent != null) {
                defaultValueLayout.remove(defaultValueComponent);
            }
            if (condition.getParam() != null) {
                defaultValueComponent = condition.getParam().createEditComponent(Param.ValueProperty.DEFAULT_VALUE);
                defaultValueLayout.add(defaultValueComponent);
                defaultValueComponent.setAlignment(Alignment.MIDDLE_LEFT);
                if (defaultValueComponent instanceof TextField) {
                    defaultValueComponent.setWidth(theme.get("cuba.gui.conditionFrame.textField.width"));
                }
            }
        }
    }

    public boolean commit() {
        if (condition == null)
            return false;

        if (hidden != null) {
            condition.setHidden(hidden.getValue());
        }
        if (required != null) {
            condition.setRequired(required.getValue());
        }
        if (width != null) {
            condition.setWidth(width.getValue());
        }

        return true;
    }
}