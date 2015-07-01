/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.jpql.DomainModel;
import com.haulmont.cuba.core.sys.jpql.DomainModelBuilder;
import com.haulmont.cuba.core.sys.jpql.transform.QueryTransformerAstBased;
import org.antlr.runtime.RecognitionException;

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
            try {
                if (domainModel == null) {
                    MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);
                    MessageTools messageTools = AppBeans.get(MessageTools.NAME);
                    DomainModelBuilder builder = new DomainModelBuilder(metadataTools, messageTools);
                    domainModel = builder.produce();
                }
                return new QueryTransformerAstBased(domainModel, query, "");
            } catch (RecognitionException e) {
                throw new RuntimeException(e);
            }
        } else {
            return new QueryTransformerRegex(query);
        }
    }

    public static QueryParser createParser(String query) {
        return new QueryParserRegex(query);
    }
}
