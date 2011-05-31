/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 26.12.2008 10:10:03
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.jpql.DomainModel;
import com.haulmont.cuba.core.sys.jpql.DomainModelBuilder;
import com.haulmont.cuba.core.sys.jpql.transform.QueryTransformerAstBased;
import org.antlr.runtime.RecognitionException;

/**
 * Factory to get {@link QueryParser} and {@link QueryTransformer} instances
 */
public class QueryTransformerFactory {

    private static boolean useAst = ConfigProvider.getConfig(GlobalConfig.class).getUseAstBasedJpqlTransformer();

    private static volatile DomainModel domainModel;

    public static QueryTransformer createTransformer(String query, String targetEntity) {
        if (useAst) {
            try {
                if (domainModel == null) {
                    DomainModelBuilder builder = new DomainModelBuilder();
                    domainModel = builder.produce(MetadataHelper.getAllPersistentMetaClasses());
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
