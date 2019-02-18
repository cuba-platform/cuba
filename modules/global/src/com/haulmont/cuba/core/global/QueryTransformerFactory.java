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
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Factory to get {@link QueryParser} and {@link QueryTransformer} instances.
 */
@Component(QueryTransformerFactory.NAME)
public class QueryTransformerFactory {

    public static final String NAME = "cuba_QueryTransformerFactory";

    protected volatile DomainModel domainModel;

    @Inject
    protected BeanLocator beanLocator;

    public static QueryTransformer createTransformer(String query) {
        return AppBeans.get(NAME, QueryTransformerFactory.class).transformer(query);
    }

    public static QueryParser createParser(String query) {
        return AppBeans.get(NAME, QueryTransformerFactory.class).parser(query);
    }

    public QueryTransformer transformer(String query) {
        if (domainModel == null) {
            DomainModelBuilder builder = beanLocator.get(DomainModelBuilder.NAME);
            domainModel = builder.produce();
        }
        return beanLocator.getPrototype(QueryTransformer.NAME, domainModel, query);
    }

    public QueryParser parser(String query) {
        if (domainModel == null) {
            DomainModelBuilder builder = AppBeans.get(DomainModelBuilder.NAME);
            domainModel = builder.produce();
        }
        return beanLocator.getPrototype(QueryParser.NAME, domainModel, query);
    }
}