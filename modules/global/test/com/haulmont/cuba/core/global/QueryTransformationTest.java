/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.jpql.DomainModel;
import com.haulmont.cuba.core.sys.jpql.model.Entity;
import com.haulmont.cuba.core.sys.jpql.model.EntityBuilder;
import com.haulmont.cuba.core.sys.jpql.transform.QueryTransformerAstBased;
import org.junit.Test;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class QueryTransformationTest {
    @Test
    public void testAst() throws Exception {
        for (int i = 0; i < 10000000; i++) {
            QueryTransformerAstBased transformerAstBased = new QueryTransformerAstBased(prepareDomainModel(), "select g from sec$GroupHierarchy g", "sec$GroupHierarchy");
            transformerAstBased.addWhere("g.deleteTs is null");
        }
    }

    @Test
    public void testRegexp() throws Exception {
        for (int i = 0; i < 10000; i++) {
            QueryTransformerRegex queryTransformerRegex = new QueryTransformerRegex("select g from sec$GroupHierarchy g");
            queryTransformerRegex.addWhere("g.deleteTs is null");
        }
    }

    private DomainModel prepareDomainModel() {
        EntityBuilder builder = new EntityBuilder();
        builder.startNewEntity("sec$GroupHierarchy");
        builder.addStringAttribute("group");
        builder.addStringAttribute("createdBy");
        builder.addReferenceAttribute("parent", "sec$GroupHierarchy");
        builder.addReferenceAttribute("other", "sec$GroupHierarchy");
        builder.addCollectionReferenceAttribute("constraints", "sec$Constraint");
        Entity groupHierarchy = builder.produce();

        Entity constraintEntity = builder.produceImmediately("sec$Constraint");
        return new DomainModel(groupHierarchy, constraintEntity);
    }
}
