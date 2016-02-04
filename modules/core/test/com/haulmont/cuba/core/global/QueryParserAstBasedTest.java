/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.jpql.DomainModel;
import com.haulmont.cuba.core.sys.jpql.model.Entity;
import com.haulmont.cuba.core.sys.jpql.model.EntityBuilder;
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

        parser = new QueryParserAstBased(model,
                "select h.parent.other from sec$GroupHierarchy h where h.userGroup = :par"
        );
        assertEquals("sec$GroupHierarchy", parser.getEntityNameIfSecondaryReturnedInsteadOfMain());

        parser = new QueryParserAstBased(model,
                "select h.parent.other.group from sec$GroupHierarchy h where h.userGroup = :par"
        );
        assertEquals("sec$Group", parser.getEntityNameIfSecondaryReturnedInsteadOfMain());

        parser = new QueryParserAstBased(model,
                "select g from sec$GroupHierarchy h join h.group g where h.userGroup = :par"
        );
        assertEquals("sec$Group", parser.getEntityNameIfSecondaryReturnedInsteadOfMain());

        parser = new QueryParserAstBased(model,
                "select h from sec$Constraint u, sec$GroupHierarchy h where h.userGroup = :par"
        );
        assertNull(parser.getEntityNameIfSecondaryReturnedInsteadOfMain());

        parser = new QueryParserAstBased(model,
                "select h.parent.other.createdBy from sec$GroupHierarchy h where h.userGroup = :par"
        );
        assertNull(parser.getEntityNameIfSecondaryReturnedInsteadOfMain());
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

        Entity userEntity = builder.produceImmediately("sec$User", "login");
        Entity groupEntity = builder.produceImmediately("sec$Group", "name");
        return new DomainModel(groupHierarchy, constraintEntity, userEntity, groupEntity);
    }
}
