/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.condition.DynamicAttributesCondition;
import com.haulmont.cuba.gui.components.filter.condition.PropertyCondition;
import org.apache.commons.lang.RandomStringUtils;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gorbunkov
 * @version $Id$
 */
@ManagedBean(ConditionParamBuilder.NAME)
public class ConditionParamBuilderImpl implements ConditionParamBuilder{

    protected Map<Class, Builder> builders = new HashMap<>();

    protected Builder defaultBuilder;

    @PostConstruct
    public void initBuilders() {
        builders.put(PropertyCondition.class, new PropertyParamBuilder());
        builders.put(DynamicAttributesCondition.class, new DynamicPropertyParamBuilder());
        defaultBuilder = new DefaultParamBuilder();
    }

    @Override
    public Param createParam(AbstractCondition condition) {
        Builder builder = builders.get(condition.getClass());
        if (builder == null) {
            builder = defaultBuilder;
        }

        return builder.createParam(condition);
    }

    @Override
    public String createParamName(AbstractCondition condition) {
        return "component$" + condition.getFilterComponentName() + "." +
                condition.getName().replace('.', '_') + RandomStringUtils.randomNumeric(5);
    }

    protected interface Builder {
        Param createParam(AbstractCondition condition);
    }

    protected class DefaultParamBuilder implements Builder {

        @Override
        public Param createParam(AbstractCondition condition) {
            if (condition.getUnary())
                return new Param(condition.getParamName(), null, null, null, null, false, condition.getRequired());

            return new Param(condition.getParamName(), condition.getParamClass() == null ? condition.getJavaClass() : condition.getParamClass(),
                    condition.getEntityParamWhere(), condition.getEntityParamView(), condition.getDatasource(), condition.getInExpr(), condition.getRequired());
        }
    }

    protected class PropertyParamBuilder implements Builder {

        @Override
        public Param createParam(AbstractCondition condition) {
            if (condition.getUnary())
                return new Param(condition.getParamName(), Boolean.class, null, null, null, false, condition.getRequired());

            MetaProperty metaProperty = condition.getDatasource().getMetaClass().getProperty(condition.getName());
            return new Param(condition.getParamName(), condition.getJavaClass(), condition.getEntityParamWhere(), condition.getEntityParamView(),
                    condition.getDatasource(), metaProperty, condition.getInExpr(), condition.getRequired());
        }
    }

    protected class DynamicPropertyParamBuilder extends DefaultParamBuilder {
        @Override
        public Param createParam(AbstractCondition condition) {
            DynamicAttributesCondition _condition = (DynamicAttributesCondition) condition;
            if (_condition.getCategoryAttributeId() != null) {
                Class paramJavaClass = _condition.getUnary() ? Boolean.class : _condition.getJavaClass();

                MetaPropertyPath metaPropertyPath = DynamicAttributesUtils.getMetaPropertyPath(_condition.getDatasource().getMetaClass(), _condition.getCategoryAttributeId());
                return new Param(_condition.getParamName(), paramJavaClass, null, null, _condition.getDatasource(),
                        metaPropertyPath != null ? metaPropertyPath.getMetaProperty() : null,
                        _condition.getInExpr(), _condition.getRequired(), _condition.getCategoryId());
            } else {
                return super.createParam(condition);
            }
        }
    }
}