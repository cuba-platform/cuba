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

/**
 * @author gorbunkov
 * @version $Id$
 */
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
        required = getComponent("required");
        hidden = getComponent("hidden");
        width = getComponent("width");

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
        defaultValueLayout = getComponent("defaultValueLayout");
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
            condition.setHidden((Boolean) hidden.getValue());
        }
        if (required != null) {
            condition.setRequired((Boolean) required.getValue());
        }
        if (width != null) {
            condition.setWidth((Integer) width.getValue());
        }

        return true;
    }

}
