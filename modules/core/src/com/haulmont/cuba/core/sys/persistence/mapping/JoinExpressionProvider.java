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

package com.haulmont.cuba.core.sys.persistence.mapping;

import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.mappings.DatabaseMapping;

import javax.annotation.Nullable;

/**
 * Generates additional join expression for entity mappings. Used in {@link JoinCriteriaMappingProcessor}.
 * Every provider should be a Spring @{@link org.springframework.stereotype.Component}.
 */
public interface JoinExpressionProvider {

    /**
     * Returns join expression that can be applied to certain types of mappings.
     * @param mapping mapping to be processed.
     * @return EclipseLink's expression object, similar to criteria API expression.
     */
    @Nullable
    Expression getJoinCriteriaExpression(DatabaseMapping mapping);

}
