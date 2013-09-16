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

    private static boolean useAst = AppBeans.get(Configuration.class)
            .getConfig(GlobalConfig.class).getUseAstBasedJpqlTransformer();

    private static volatile DomainModel domainModel;

    public static QueryTransformer createTransformer(String query, String targetEntity) {
        if (useAst) {
            try {
                if (domainModel == null) {
                    DomainModelBuilder builder = new DomainModelBuilder(
                            AppBeans.get(MetadataTools.class), AppBeans.get(MessageTools.class));
                    domainModel = builder.produce();
                }
                return new QueryTransformerAstBased(domainModel, query, targetEntity);
            } catch (RecognitionException e) {
                throw new RuntimeException(e);
            }
        } else {
            return new QueryTransformerRegex(query, targetEntity);
        }
    }

    public static QueryParser createParser(String query) {
        return new QueryParserRegex(query);
    }
}
