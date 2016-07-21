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

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.condition.DynamicAttributesCondition;
import com.haulmont.cuba.gui.components.filter.condition.PropertyCondition;
import org.apache.commons.lang.RandomStringUtils;

import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component(ConditionParamBuilder.NAME)
public class ConditionParamBuilderImpl implements ConditionParamBuilder{

    protected Map<Class, ParameterInstantiationStrategy> strategies = new HashMap<>();

    protected ParameterInstantiationStrategy defaultParameterInstantiationStrategy;

    @PostConstruct
    public void initCreatingStrategies() {
        strategies.put(PropertyCondition.class, new PropertyParameterInstantiationStrategy());
        strategies.put(DynamicAttributesCondition.class, new DynamicPropertyParameterInstantiationStrategy());
        defaultParameterInstantiationStrategy = new DefaultParameterInstantiationStrategy();
    }

    @Override
    public Param createParam(AbstractCondition condition) {
        ParameterInstantiationStrategy parameterInstantiationStrategy = strategies.get(condition.getClass());
        if (parameterInstantiationStrategy == null) {
            parameterInstantiationStrategy = defaultParameterInstantiationStrategy;
        }

        return parameterInstantiationStrategy.createParam(condition);
    }

    @Override
    public String createParamName(AbstractCondition condition) {
        return "component$" + condition.getFilterComponentName() + "." +
                condition.getName().replace('.', '_').replace(" ", "_") + RandomStringUtils.randomNumeric(5);
    }

    protected interface ParameterInstantiationStrategy {
        Param createParam(AbstractCondition condition);
    }

    protected class DefaultParameterInstantiationStrategy implements ParameterInstantiationStrategy {

        public Param createParam(AbstractCondition condition) {
            Param.Builder builder = getParamBuilder(condition);
            return builder.build();
        }

        protected Param.Builder getParamBuilder(AbstractCondition condition) {
            Param.Builder builder = Param.Builder.getInstance()
                    .setName(condition.getParamName())
                    .setRequired(condition.getRequired());

            if (!condition.getUnary()) {
                builder.setJavaClass(condition.getParamClass() == null ?
                        condition.getJavaClass() : condition.getParamClass());
                builder.setEntityWhere(condition.getEntityParamWhere());
                builder.setEntityView(condition.getEntityParamView());
                builder.setDataSource(condition.getDatasource());
                builder.setInExpr(condition.getInExpr());
            }

            return builder;
        }
    }

    protected class PropertyParameterInstantiationStrategy extends DefaultParameterInstantiationStrategy {
        @Override
        public Param.Builder getParamBuilder(AbstractCondition condition) {
            Param.Builder builder = super.getParamBuilder(condition);
            MetaProperty metaProperty = condition.getDatasource().getMetaClass().getProperty(condition.getName());
            if (!condition.getUnary())
                builder.setJavaClass(condition.getJavaClass())
                        .setProperty(metaProperty);
            return builder;
        }
    }

    protected class DynamicPropertyParameterInstantiationStrategy extends DefaultParameterInstantiationStrategy {
        @Override
        public Param.Builder getParamBuilder(AbstractCondition condition) {
            Param.Builder builder;
            DynamicAttributesCondition _condition = (DynamicAttributesCondition) condition;
            if (_condition.getCategoryAttributeId() != null) {
                Class paramJavaClass = _condition.getUnary() ? Boolean.class : _condition.getJavaClass();

                MetaPropertyPath metaPropertyPath = DynamicAttributesUtils.getMetaPropertyPath(
                        _condition.getDatasource().getMetaClass(), _condition.getCategoryAttributeId());

                builder = Param.Builder.getInstance()
                        .setJavaClass(paramJavaClass)
                        .setEntityWhere(null)
                        .setEntityView(null)
                        .setDataSource(_condition.getDatasource())
                        .setProperty(metaPropertyPath != null ? metaPropertyPath.getMetaProperty() : null)
                        .setInExpr(_condition.getInExpr())
                        .setCategoryAttrId(_condition.getCategoryAttributeId())
                        .setRequired(condition.getRequired());
            } else
                builder = super.getParamBuilder(condition);

            return builder;
        }
    }
}