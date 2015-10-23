/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.jpql.DomainModel;
import com.haulmont.cuba.core.sys.jpql.DomainModelBuilder;

/**
 * Factory to get {@link QueryParser} and {@link QueryTransformer} instances.
 *
 * @author krivopustov
 * @version $Id$
 */
public class QueryTransformerFactory {

    private static boolean useAst = AppBeans.<Configuration>get(Configuration.NAME)
            .getConfig(GlobalConfig.class).getUseAstBasedJpqlTransformer();

    private static volatile DomainModel domainModel;

    public static QueryTransformer createTransformer(String query) {
        if (useAst) {
            if (domainModel == null) {
                MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);
                MessageTools messageTools = AppBeans.get(MessageTools.NAME);
                ExtendedEntities extendedEntities = AppBeans.get(ExtendedEntities.NAME);

                DomainModelBuilder builder = new DomainModelBuilder(metadataTools, messageTools, extendedEntities);
                domainModel = builder.produce();
            }
            return AppBeans.getPrototype(QueryTransformer.NAME, domainModel, query);
        } else {
            return new QueryTransformerRegex(query);
        }
    }

    public static QueryParser createParser(String query) {
        return new QueryParserRegex(query);
    }
}
