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
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.global.queryconditions.Condition;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.Map;

public interface DataLoadContextQuery {

    DataLoadContextQuery setParameter(String name, Object value);

    DataLoadContextQuery setParameter(String name, Date value, TemporalType temporalType);

    Map<String, Object> getParameters();
    DataLoadContextQuery setParameters(Map<String, Object> parameters);

    int getFirstResult();
    DataLoadContextQuery setFirstResult(int firstResult);

    int getMaxResults();
    DataLoadContextQuery setMaxResults(int maxResults);

    Condition getCondition();
    DataLoadContextQuery setCondition(Condition condition);
}
