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

package com.haulmont.cuba.core.global.filter;

import java.util.Iterator;
import java.util.List;

public abstract class AbstractJpqlGenerator {
    public String generateJpql(Condition condition) {
        if (condition instanceof LogicalCondition) {
            LogicalOp operation = ((LogicalCondition) condition).getOperation();
            List<Condition> conditions = condition.getConditions();
            if (conditions.isEmpty())
                return "";
            else {
                StringBuilder sb = new StringBuilder();

                if (conditions.size() > 1)
                    sb.append("(");

                for (Iterator<Condition> it = conditions.iterator(); it.hasNext(); ) {
                    Condition currentCondition= it.next();
                    sb.append(generateJpql(currentCondition));
                    if (it.hasNext())
                        sb.append(" ").append(operation.forJpql()).append(" ");
                }

                if (conditions.size() > 1)
                    sb.append(")");

                return sb.toString();
            }
        } else if (condition instanceof Clause) {
            return generateClauseText((Clause) condition);
        }
        throw new UnsupportedOperationException();
    }

    protected abstract String generateClauseText(Clause condition);
}