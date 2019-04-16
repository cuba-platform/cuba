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

package com.haulmont.cuba.core.sys;


import com.haulmont.cuba.core.global.QueryHints;
import org.eclipse.persistence.jpa.JpaQuery;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@Component(QueryHintsProcessor.NAME)
public class QueryHintsProcessor {
    public static final String NAME = "cuba_QueryHintsProcessor";

    protected Map<String, BiConsumer<JpaQuery, Object>> hintHandlers = new HashMap<>();

    @PostConstruct
    protected void init() {
        hintHandlers.put(QueryHints.SQL_HINT,
                (query, value) -> query.setHint(org.eclipse.persistence.config.QueryHints.HINT, value));
        hintHandlers.put(QueryHints.MSSQL_RECOMPILE_HINT,
                (query, value) -> query.setHint(org.eclipse.persistence.config.QueryHints.HINT, "OPTION(RECOMPILE)"));
    }

    public void applyQueryHint(JpaQuery query, String hintName, Object value) {
        BiConsumer<JpaQuery, Object> handler = hintHandlers.get(hintName);
        if (handler == null) {
            throw new UnsupportedOperationException(String.format("Unsupported hint: %s", hintName));
        }
        handler.accept(query, value);
    }
}
