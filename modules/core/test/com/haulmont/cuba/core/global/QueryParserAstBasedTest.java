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
import com.haulmont.cuba.core.sys.jpql.JpqlSyntaxException;
import com.haulmont.cuba.core.sys.jpql.model.JpqlEntityModel;
import com.haulmont.cuba.core.sys.jpql.model.EntityBuilder;
import com.haulmont.cuba.core.sys.jpql.transform.QueryTransformerAstBased;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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
    public void testError() throws Exception {
        DomainModel model = prepareDomainModel();
        try {
            QueryParserAstBased parser = new QueryParserAstBased(model,
                    "select u from sec$Constraint"
            );
            parser.getEntityAlias();
            fail();
        } catch (JpqlSyntaxException e) {
            //OK
        }

        try {
            QueryParserAstBased parser = new QueryParserAstBased(model,
                    "select u from sec$GroupHierarchy where u.createdBy = 'createdBy'"
            );
            parser.getEntityAlias();
            fail();
        } catch (JpqlSyntaxException e) {
            //OK
        }

        try {
            QueryParserAstBased parser = new QueryParserAstBased(model,
                    "select u from sec$GroupHierarchy u where u.createdBy != 'createdBy'"
            );
            parser.getEntityAlias();
            fail();
        } catch (JpqlSyntaxException e) {
            //OK
        }
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
    public void testErrorsInJoin() {
        try {
            QueryParserAstBased parser = new QueryParserAstBased(prepareDomainModel(),
                    "select h, sum(u.int1) from sec$User u join u.group g join g.hierarchy hwhere u.constraint.id = :storeSelect group by h");
            parser.getEntityName();
            fail();
        } catch (JpqlSyntaxException e) {
            //Do nothing
        }

        try {
            QueryParserAstBased parser = new QueryParserAstBased(prepareDomainModel(),
                    "select u from sec$User u join fetch u.group g");
            parser.getEntityName();
            fail();
        } catch (JpqlSyntaxException e) {
            //Do nothing
        }
    }

    @Test
    public void testUsedEntityNames() throws Exception {
        QueryParserAstBased parser = new QueryParserAstBased(prepareDomainModel(),
                "select u from sec$Constraint u"
        );
        Set<String> entityNames = parser.getAllEntityNames();
        assertEquals(1, entityNames.size());
        assertTrue(entityNames.contains("sec$Constraint"));

        parser = new QueryParserAstBased(prepareDomainModel(),
                "select u from sec$Constraint u where u.group = :param"
        );
        entityNames = parser.getAllEntityNames();
        assertEquals(2, entityNames.size());
        assertTrue(entityNames.contains("sec$Constraint"));
        assertTrue(entityNames.contains("sec$GroupHierarchy"));

        parser = new QueryParserAstBased(prepareDomainModel(),
                "select u from sec$Constraint u, sec$GroupHierarchy h"
        );
        entityNames = parser.getAllEntityNames();
        assertEquals(2, entityNames.size());
        assertTrue(entityNames.contains("sec$Constraint"));
        assertTrue(entityNames.contains("sec$GroupHierarchy"));

        parser = new QueryParserAstBased(prepareDomainModel(),
                "select u from sec$Constraint u, sec$GroupHierarchy h where u.group = h"
        );
        entityNames = parser.getAllEntityNames();
        assertEquals(2, entityNames.size());
        assertTrue(entityNames.contains("sec$Constraint"));
        assertTrue(entityNames.contains("sec$GroupHierarchy"));

        parser = new QueryParserAstBased(prepareDomainModel(),
                "select u from sec$Constraint u, sec$GroupHierarchy h where h.group.id = :par and u.group = h"
        );
        entityNames = parser.getAllEntityNames();
        assertEquals(3, entityNames.size());
        assertTrue(entityNames.contains("sec$Constraint"));
        assertTrue(entityNames.contains("sec$GroupHierarchy"));
        assertTrue(entityNames.contains("sec$Group"));

        parser = new QueryParserAstBased(prepareDomainModel(),
                "select c from sec$Constraint c join c.group g"
        );
        entityNames = parser.getAllEntityNames();
        assertEquals(2, entityNames.size());
        assertTrue(entityNames.contains("sec$Constraint"));
        assertTrue(entityNames.contains("sec$GroupHierarchy"));


        parser = new QueryParserAstBased(prepareDomainModel(),
                "select c from sec$Constraint c join sec$Group g on c.group.group = g"
        );
        entityNames = parser.getAllEntityNames();
        assertEquals(3, entityNames.size());
        assertTrue(entityNames.contains("sec$Constraint"));
        assertTrue(entityNames.contains("sec$GroupHierarchy"));
        assertTrue(entityNames.contains("sec$Group"));

        parser = new QueryParserAstBased(prepareDomainModel(),
                "select c from sec$Constraint c join sec$Group g on g.name = :par"
        );
        entityNames = parser.getAllEntityNames();
        assertEquals(2, entityNames.size());
        assertTrue(entityNames.contains("sec$Constraint"));
        assertTrue(entityNames.contains("sec$Group"));

        parser = new QueryParserAstBased(prepareDomainModel(),
                "select c from sec$Constraint c join sec$Group g"
        );
        entityNames = parser.getAllEntityNames();
        assertEquals(2, entityNames.size());
        assertTrue(entityNames.contains("sec$Constraint"));
        assertTrue(entityNames.contains("sec$Group"));
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
    public void testEnumMacro() {
        DomainModel model = prepareDomainModel();
        QueryParserAstBased parser = new QueryParserAstBased(model,
                "select r from sec$Role r where r.type = @enum(com.haulmont.cuba.security.entity.RoleType.STANDARD)");
        parser.getEntityName();

        parser = new QueryParserAstBased(model,
                "select r from sec$Role r where (select r1.type from sec$Role r1 where r1.id = r.id) = @enum(com.haulmont.cuba.security.entity.RoleType.STANDARD)");
        parser.getEntityName();

        parser = new QueryParserAstBased(model,
                "select r from sec$Role r where r.type in (@enum(com.haulmont.cuba.security.entity.RoleType.STANDARD), @enum(com.haulmont.cuba.security.entity.RoleType.SUPER))");
        parser.getEntityName();
    }

    @Test
    public void testGetNestedEntityNameIfNestedSelected() throws Exception {
        DomainModel model = prepareDomainModel();
        QueryParserAstBased parser = new QueryParserAstBased(model,
                "select h.group from sec$Constraint u, sec$GroupHierarchy h where h.userGroup = :par"
        );
        assertEquals("sec$Group", parser.getOriginalEntityName());
        assertEquals("h.group", parser.getOriginalEntityPath());

        parser = new QueryParserAstBased(model,
                "select g.group from sec$GroupHierarchy h join h.group g"
        );
        assertNull(parser.getOriginalEntityName());
        assertNull(parser.getOriginalEntityPath());

        parser = new QueryParserAstBased(model,
                "select g from sec$GroupHierarchy h join h.group g"
        );
        assertNotNull(parser.getOriginalEntityName());
        assertNotNull(parser.getOriginalEntityPath());

        parser = new QueryParserAstBased(model,
                "select h.parent.other from sec$GroupHierarchy h where h.userGroup = :par"
        );
        assertEquals("sec$GroupHierarchy", parser.getOriginalEntityName());
        assertEquals("h.parent.other", parser.getOriginalEntityPath());

        parser = new QueryParserAstBased(model,
                "select h.parent.other.group from sec$GroupHierarchy h where h.userGroup = :par"
        );
        assertEquals("sec$Group", parser.getOriginalEntityName());
        assertEquals("h.parent.other.group", parser.getOriginalEntityPath());

        parser = new QueryParserAstBased(model,
                "select g from sec$GroupHierarchy h join h.group g where h.userGroup = :par"
        );
        assertEquals("sec$Group", parser.getOriginalEntityName());
        assertEquals("g", parser.getOriginalEntityPath());

        parser = new QueryParserAstBased(model,
                "select p from sec$GroupHierarchy h join h.parent p where h.userGroup = :par"
        );
        assertEquals("sec$GroupHierarchy", parser.getOriginalEntityName());
        assertEquals("p", parser.getOriginalEntityPath());

        parser = new QueryParserAstBased(model,
                "select h from sec$Constraint u, sec$GroupHierarchy h where h.userGroup = :par"
        );
        assertNull(parser.getOriginalEntityName());
        assertNull(parser.getOriginalEntityPath());

        parser = new QueryParserAstBased(model,
                "select h.parent.other.createdBy from sec$GroupHierarchy h where h.userGroup = :par"
        );
        assertNull(parser.getOriginalEntityName());
        assertNull(parser.getOriginalEntityPath());

        parser = new QueryParserAstBased(model,
                "select c from sec$GroupHierarchy h, sec$Constraint c where h.userGroup = :par"
        );
        assertNull(parser.getOriginalEntityName());
        assertNull(parser.getOriginalEntityPath());

        parser = new QueryParserAstBased(model,
                "select c.group from sec$GroupHierarchy h, sec$Constraint c where h.userGroup = :par"
        );
        assertEquals("sec$GroupHierarchy", parser.getOriginalEntityName());
        assertEquals("c.group", parser.getOriginalEntityPath());

        parser = new QueryParserAstBased(model,
                "select u.group, u.login from sec$User u where u.name like :mask"
        );
        assertNull(parser.getOriginalEntityName());
        assertNull(parser.getOriginalEntityPath());
    }

    @Test
    public void testNestedEntityGroupBy() throws Exception {
        DomainModel model = prepareDomainModel();
        QueryTransformerAstBased transformer = new QueryTransformerAstBased(model,
                "select c.group, count(c.id) from sec$Constraint c group by c.group"
        );
        transformer.replaceWithSelectEntityVariable("tempEntityAlias");
        transformer.addFirstSelectionSource(String.format("%s tempEntityAlias", "sec$Group"));
        transformer.addWhereAsIs(String.format("tempEntityAlias.id = %s.id", "c.group"));
        transformer.addEntityInGroupBy("tempEntityAlias");
        System.out.println(transformer.getResult());
    }

    @Test
    public void testHasJoins() throws Exception {
        DomainModel model = prepareDomainModel();
        QueryParserAstBased parser = new QueryParserAstBased(model,
                "select h.group from sec$Constraint u, sec$GroupHierarchy h"
        );
        assertTrue(parser.isQueryWithJoins());

        parser = new QueryParserAstBased(model,
                "select g.group from sec$GroupHierarchy h join h.group g"
        );
        assertTrue(parser.isQueryWithJoins());

        parser = new QueryParserAstBased(model,
                "select h.parent.other from sec$GroupHierarchy h"
        );
        assertFalse(parser.isQueryWithJoins());
    }

    @Test
    public void testScalarExpressionInSelect() throws Exception {
        DomainModel model = prepareDomainModel();
        QueryParserAstBased transformer = new QueryParserAstBased(model,
                "select c.int1 + c.int2 * c.int1 from sec$User u"
        );
        transformer.getParamNames();
    }

    @Test
    public void testSameAliasSeveralTimes() {
        DomainModel model = prepareDomainModel();
        QueryParserAstBased parser = new QueryParserAstBased(model,
                "select g.group from sec$GroupHierarchy h join h.group g join h.group g");
        try {
            parser.getEntityAlias();
            fail();
        } catch (JpqlSyntaxException e) {
            //success
        }
    }

    @Test
    public void testPathVariableInSubQuery() {
        DomainModel model = prepareDomainModel();
        QueryParserAstBased parser = new QueryParserAstBased(model,
                "select h from sec$GroupHierarchy h where exists(select c from h.constraints as c)");
        parser.getEntityAlias();

        parser = new QueryParserAstBased(model,
                "select h from sec$GroupHierarchy h where exists(select c from h.constraints c)");
        parser.getEntityAlias();
    }

    private DomainModel prepareDomainModel() {
        EntityBuilder builder = new EntityBuilder();
        builder.startNewEntity("sec$GroupHierarchy");
        builder.addReferenceAttribute("group", "sec$Group");
        builder.addReferenceAttribute("as", "sec$Group");
        builder.addStringAttribute("createdBy");
        builder.addReferenceAttribute("parent", "sec$GroupHierarchy");
        builder.addReferenceAttribute("other", "sec$GroupHierarchy");
        builder.addCollectionReferenceAttribute("constraints", "sec$Constraint");
        JpqlEntityModel groupHierarchy = builder.produce();

        builder = new EntityBuilder();
        builder.startNewEntity("sec$Constraint");
        builder.addReferenceAttribute("group", "sec$GroupHierarchy");
        JpqlEntityModel constraintEntity = builder.produce();


        JpqlEntityModel groupEntity = builder.produceImmediately("sec$Group", "name", "group");

        builder = new EntityBuilder();
        builder.startNewEntity("sec$User");
        builder.addStringAttribute("login");
        builder.addSingleValueAttribute(Integer.class,"int1");
        builder.addSingleValueAttribute(Integer.class,"int2");
        builder.addReferenceAttribute("group", "sec$Group");
        JpqlEntityModel userEntity = builder.produce();

        return new DomainModel(groupHierarchy, constraintEntity, userEntity, groupEntity);
    }
}
