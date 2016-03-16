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
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.jpql.DomainModel;
import com.haulmont.cuba.core.sys.jpql.DomainModelBuilder;

/**
 * Factory to get {@link QueryParser} and {@link QueryTransformer} instances.
 *
 */
public class QueryTransformerFactory {

    private static boolean useAst = AppBeans.<Configuration>get(Configuration.NAME)
            .getConfig(GlobalConfig.class).getUseAstBasedJpqlTransformer();

    private static volatile DomainModel domainModel;

    public static QueryTransformer createTransformer(String query) {
        if (useAst) {
            if (domainModel == null) {
                DomainModelBuilder builder = new DomainModelBuilder();
                domainModel = builder.produce();
            }
            return AppBeans.getPrototype(QueryTransformer.NAME, domainModel, query);
        } else {
            return new QueryTransformerRegex(query);
        }
    }

    public static QueryParser createParser(String query) {
        if (useAst) {
            if (domainModel == null) {
                DomainModelBuilder builder = new DomainModelBuilder();
                domainModel = builder.produce();
            }
            return AppBeans.getPrototype(QueryParser.NAME, domainModel, query);
        } else {
            return new QueryParserRegex(query);
        }
    }
}
