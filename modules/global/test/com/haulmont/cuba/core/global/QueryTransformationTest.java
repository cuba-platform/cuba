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
import com.haulmont.cuba.core.sys.jpql.model.JpqlEntityModel;
import com.haulmont.cuba.core.sys.jpql.model.EntityBuilder;
import com.haulmont.cuba.core.sys.jpql.transform.QueryTransformerAstBased;
import org.junit.Test;

public class QueryTransformationTest {
    @Test
    public void testAst() throws Exception {
        for (int i = 0; i < 1000; i++) {
            QueryTransformerAstBased transformerAstBased = new QueryTransformerAstBased(prepareDomainModel(), "select g from sec$GroupHierarchy g");
            transformerAstBased.addWhere("g.deleteTs is null");
        }
    }

    @Test
    public void testRegexp() throws Exception {
        for (int i = 0; i < 1000; i++) {
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
        JpqlEntityModel groupHierarchy = builder.produce();

        JpqlEntityModel constraintEntity = builder.produceImmediately("sec$Constraint");
        return new DomainModel(groupHierarchy, constraintEntity);
    }
}