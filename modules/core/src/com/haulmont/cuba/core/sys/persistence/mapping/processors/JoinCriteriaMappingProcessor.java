/*
 * Copyright (c) 2008-2020 Haulmont.
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

package com.haulmont.cuba.core.sys.persistence.mapping.processors;

import com.haulmont.cuba.core.global.AppBeans;
import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.OneToManyMapping;
import org.eclipse.persistence.mappings.OneToOneMapping;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Created by Aleksey Stukalov on 01.04.2020.
 */
@Component("cuba_JoinCriteriaMappingProcessor")
public class JoinCriteriaMappingProcessor implements MappingProcessor {

    @Override
    public void process(MappingProcessorContext context) {
        DatabaseMapping mapping = context.getMapping();

        Expression expression = AppBeans.getAll(JoinExpressionProvider.class)
                .values().stream()
                .map(provider -> provider.getJoinCriteriaExpression(mapping))
                .filter(Objects::nonNull)
                .reduce(Expression::and).orElse(null);

        //Applying additional join criteria, e.g. for soft delete or multitenancy -> move to mapping processor
        if (mapping.isOneToManyMapping() || mapping.isOneToOneMapping()) {
            //Apply expression to mappings
            if (mapping.isOneToManyMapping()) {
                ((OneToManyMapping) mapping).setAdditionalJoinCriteria(expression);
            } else if (mapping.isOneToOneMapping()) {
                ((OneToOneMapping) mapping).setAdditionalJoinCriteria(expression);
            }
        }
    }
}
