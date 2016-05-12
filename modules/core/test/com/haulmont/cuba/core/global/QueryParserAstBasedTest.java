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
import com.haulmont.cuba.core.sys.jpql.model.Entity;
import com.haulmont.cuba.core.sys.jpql.model.EntityBuilder;
import com.haulmont.cuba.core.sys.jpql.transform.QueryTransformerAstBased;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class QueryParserAstBasedTest {

    @Test
    public void testMainEntity() throws Exception {
        DomainModel model = prepareDomainModel();
        QueryParserAstBased parser = new QueryParserAstBased(model,
                "select h from sec$Constraint u, sec$GroupHierarchy h where h.userGroup = :par"
        );

        assertEquals("sec$GroupHierarchy", parser.getEntityName());
        assertEquals("h", parser.getEntityAlias());

        parser = new QueryParserAstBased(model,
                "select h.group from sec$Constraint u, sec$GroupHierarchy h where h.userGroup = :par"
        );

        assertEquals("sec$GroupHierarchy", parser.getEntityName());
        assertEquals("h", parser.getEntityAlias());

        parser = new QueryParserAstBased(model,
                "select u from sec$Constraint u, sec$GroupHierarchy h where h.userGroup = :par"
        );

        assertEquals("sec$Constraint", parser.getEntityName());
        assertEquals("u", parser.getEntityAlias());
    }


    @Test
    public void testGetParamNames() throws Exception {
        QueryParserAstBased parser = new QueryParserAstBased(prepareDomainModel(),
                "select u from sec$Constraint u, sec$GroupHierarchy h where h.userGroup = :par"
        );

        Set<String> paramNames = parser.getParamNames();
        assertEquals(1, paramNames.size());
        assertTrue(paramNames.contains("par"));

        parser = new QueryParserAstBased(prepareDomainModel(),
                "select u from sec$Constraint u, sec$GroupHierarchy h " +
                        "where " +
                        "h.userGroup = :par and " +
                        "h.createdBy like :par2 and " +
                        "h.parent <> :par3"
        );

        paramNames = parser.getParamNames();
        assertEquals(3, paramNames.size());
        assertTrue(paramNames.contains("par"));
        assertTrue(paramNames.contains("par2"));
        assertTrue(paramNames.contains("par3"));

        parser = new QueryParserAstBased(prepareDomainModel(),
                "select c from sec$Constraint c join sec$Group g on g.name = :par");
        paramNames = parser.getParamNames();
        assertEquals(1, paramNames.size());
        assertTrue(paramNames.contains("par"));
    }

    @Test
    public void testEntityAlias() throws Exception {
        QueryParserAstBased parser = new QueryParserAstBased(prepareDomainModel(),
                "select h from sec$Constraint u, sec$GroupHierarchy h where h.userGroup = :par"
        );

        assertEquals("h", parser.getEntityAlias());
        assertEquals("u", parser.getEntityAlias("sec$Constraint"));
    }

    @Test
    public void testIsEntitySelect() throws Exception {
        DomainModel model = prepareDomainModel();
        QueryParserAstBased parser = new QueryParserAstBased(model,
                "select h from sec$Constraint u, sec$GroupHierarchy h where h.userGroup = :par"
        );

        assertTrue(parser.isEntitySelect("sec$GroupHierarchy"));

        parser = new QueryParserAstBased(model,
                "select h.createdBy, h.parent from sec$Constraint u, sec$GroupHierarchy h where h.userGroup = :par"
        );

        assertFalse(parser.isEntitySelect("sec$GroupHierarchy"));
    }

    @Test
    public void testHasIsNullCondition() throws Exception {
        DomainModel model = prepareDomainModel();
        QueryParserAstBased parser = new QueryParserAstBased(model, "select c from ref$Car c");
        assertFalse(parser.hasIsNullCondition("colour"));

        parser = new QueryParserAstBased(model, "select c from ref$Car c where c.colour = ?1");
        assertFalse(parser.hasIsNullCondition("colour"));

        parser = new QueryParserAstBased(model, "select c from ref$Car c where c.colour is null");
        assertTrue(parser.hasIsNullCondition("colour"));

        parser = new QueryParserAstBased(model, "select c from ref$Car c where c.model.manufacturer is null");
        assertTrue(parser.hasIsNullCondition("model.manufacturer"));

        parser = new QueryParserAstBased(model, "select c from ref$Car c where c.model = (select a from ref$Other a where a.model is null)");
        assertFalse(parser.hasIsNullCondition("model"));
    }

    @Test
    public void testGetNestedEntityNameIfNestedSelected() throws Exception {
        DomainModel model = prepareDomainModel();
        QueryParserAstBased parser = new QueryParserAstBased(model,
                "select h.group from sec$Constraint u, sec$GroupHierarchy h where h.userGroup = :par"
        );
        assertEquals("sec$Group", parser.getEntityNameIfSecondaryReturnedInsteadOfMain());
        assertEquals("h.group", parser.getEntityPathIfSecondaryReturnedInsteadOfMain());

        parser = new QueryParserAstBased(model,
                "select h.parent.other from sec$GroupHierarchy h where h.userGroup = :par"
        );
        assertEquals("sec$GroupHierarchy", parser.getEntityNameIfSecondaryReturnedInsteadOfMain());
        assertEquals("h.parent.other", parser.getEntityPathIfSecondaryReturnedInsteadOfMain());

        parser = new QueryParserAstBased(model,
                "select h.parent.other.group from sec$GroupHierarchy h where h.userGroup = :par"
        );
        assertEquals("sec$Group", parser.getEntityNameIfSecondaryReturnedInsteadOfMain());
        assertEquals("h.parent.other.group", parser.getEntityPathIfSecondaryReturnedInsteadOfMain());

        parser = new QueryParserAstBased(model,
                "select g from sec$GroupHierarchy h join h.group g where h.userGroup = :par"
        );
        assertEquals("sec$Group", parser.getEntityNameIfSecondaryReturnedInsteadOfMain());
        assertEquals("g", parser.getEntityPathIfSecondaryReturnedInsteadOfMain());

        parser = new QueryParserAstBased(model,
                "select p from sec$GroupHierarchy h join h.parent p g where h.userGroup = :par"
        );
        assertEquals("sec$GroupHierarchy", parser.getEntityNameIfSecondaryReturnedInsteadOfMain());
        assertEquals("p", parser.getEntityPathIfSecondaryReturnedInsteadOfMain());

        parser = new QueryParserAstBased(model,
                "select h from sec$Constraint u, sec$GroupHierarchy h where h.userGroup = :par"
        );
        assertNull(parser.getEntityNameIfSecondaryReturnedInsteadOfMain());
        assertNull(parser.getEntityPathIfSecondaryReturnedInsteadOfMain());

        parser = new QueryParserAstBased(model,
                "select h.parent.other.createdBy from sec$GroupHierarchy h where h.userGroup = :par"
        );
        assertNull(parser.getEntityNameIfSecondaryReturnedInsteadOfMain());
        assertNull(parser.getEntityPathIfSecondaryReturnedInsteadOfMain());

        parser = new QueryParserAstBased(model,
                "select c from sec$GroupHierarchy h, sec$Constraint c where h.userGroup = :par"
        );
        assertNull(parser.getEntityNameIfSecondaryReturnedInsteadOfMain());
        assertNull(parser.getEntityPathIfSecondaryReturnedInsteadOfMain());

        parser = new QueryParserAstBased(model,
                "select c.group from sec$GroupHierarchy h, sec$Constraint c where h.userGroup = :par"
        );
        assertEquals("sec$GroupHierarchy", parser.getEntityNameIfSecondaryReturnedInsteadOfMain());
        assertEquals("c.group", parser.getEntityPathIfSecondaryReturnedInsteadOfMain());

        parser = new QueryParserAstBased(model,
                "select u.group, u.login from sec$User u where u.name like :mask"
        );
        assertNull(parser.getEntityNameIfSecondaryReturnedInsteadOfMain());
        assertNull(parser.getEntityPathIfSecondaryReturnedInsteadOfMain());
    }

    private DomainModel prepareDomainModel() {
        EntityBuilder builder = new EntityBuilder();
        builder.startNewEntity("sec$GroupHierarchy");
        builder.addReferenceAttribute("group", "sec$Group");
        builder.addStringAttribute("createdBy");
        builder.addReferenceAttribute("parent", "sec$GroupHierarchy");
        builder.addReferenceAttribute("other", "sec$GroupHierarchy");
        builder.addCollectionReferenceAttribute("constraints", "sec$Constraint");
        Entity groupHierarchy = builder.produce();

        builder = new EntityBuilder();
        builder.startNewEntity("sec$Constraint");
        builder.addReferenceAttribute("group", "sec$GroupHierarchy");
        Entity constraintEntity = builder.produce();


        Entity groupEntity = builder.produceImmediately("sec$Group", "name");

        builder = new EntityBuilder();
        builder.startNewEntity("sec$User");
        builder.addStringAttribute("login");
        builder.addReferenceAttribute("group", "sec$Group");
        Entity userEntity = builder.produce();

        return new DomainModel(groupHierarchy, constraintEntity, userEntity, groupEntity);
    }
}
