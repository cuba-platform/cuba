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

package com.haulmont.cuba.core.global;

import com.google.common.base.Strings;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Pre-processes query text before setting it to {@code LoadContext.Query}.
 */
@Component(QueryStringProcessor.NAME)
public class QueryStringProcessor {

    public static final String NAME = "cuba_QueryStringProcessor";

    public static final Pattern START_PATTERN = Pattern.compile("^(\\w+)\\s");

    @Inject
    private Metadata metadata;

    public String process(String queryString, Class<? extends Entity> entityClass) {
        if (Strings.isNullOrEmpty(queryString)) {
            return queryString;
        }
        if (entityClass.getAnnotation(javax.persistence.Entity.class) != null) {
            return processJpaQuery(queryString, entityClass);
        } else {
            return queryString;
        }
    }

    protected String processJpaQuery(String queryString, Class<? extends Entity> entityClass) {
        MetaClass metaClass = metadata.getClassNN(entityClass);
        String entityName = metaClass.getName();

        String query = queryString.trim();
        Matcher startMatcher = START_PATTERN.matcher(query);
        if (startMatcher.find()) {
            String startToken = startMatcher.group(1);
            // select, from, where, order by
            if ("select".equals(startToken)) {
                return query;
            }
            if ("from".equals(startToken)) {
                Pattern entityPattern = Pattern.compile(entityName.replace("$", "\\$") + "\\s+(\\w+)");
                Matcher entityMatcher = entityPattern.matcher(query);
                if (entityMatcher.find()) {
                    String alias = entityMatcher.group(1);
                    return "select " + alias + " " + query;
                } else {
                    throw new RuntimeException(String.format(
                            "Cannot find alias for entity %s in query '%s'", entityName, query));
                }
            }
            if ("where".equals(startToken) || "order".equals(startToken)) {
                return "select e from " + entityName + " e " + query;
            }
        } else {
            // property condition
            return "select e from " + entityName + " e where " + query;
        }
        throw new RuntimeException(String.format(
                "Unable to process query '%s'.\n" +
                        "Query string must start from 'select', 'from', 'where', 'order by', or be a property condition like 'e.property = :param'.",
                query));
    }
}
