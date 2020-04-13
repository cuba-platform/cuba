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
import org.eclipse.persistence.mappings.ManyToManyMapping;
import org.eclipse.persistence.mappings.ManyToOneMapping;
import org.eclipse.persistence.mappings.OneToManyMapping;
import org.eclipse.persistence.mappings.OneToOneMapping;

/**
 * Simplifies join expression generation depending on mapping type.
 */
public abstract class AbstractJoinExpressionProvider implements JoinExpressionProvider {

    @Override
    public Expression getJoinCriteriaExpression(DatabaseMapping mapping) {
        if (mapping.isOneToManyMapping()) {
            return processOneToManyMapping((OneToManyMapping)mapping);
        } else if (mapping.isOneToOneMapping()) {
            if (mapping.isManyToOneMapping()) {
                return processManyToOneMapping((ManyToOneMapping) mapping);
            } else {
                return processOneToOneMapping((OneToOneMapping) mapping);
            }
        } else if (mapping.isManyToManyMapping()) {
            return processManyToManyMapping((ManyToManyMapping) mapping);
        }
        return null;
    }

    protected abstract Expression processOneToManyMapping(OneToManyMapping mapping);

    protected abstract Expression processOneToOneMapping(OneToOneMapping mapping);

    protected abstract Expression processManyToOneMapping(ManyToOneMapping mapping);

    protected abstract Expression processManyToManyMapping(ManyToManyMapping mapping);

}
