/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global.filter;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class JpqlGeneratorTest {

    @Test
    public void testScriptGeneration() throws Exception {
        SecurityJpqlGenerator jpqlGenerator = new SecurityJpqlGenerator();
        Clause clause = clause("name", Op.EQUAL, String.class, null);
        Assert.assertEquals("{E}.name = null", jpqlGenerator.generateJpql(clause));

        clause = clause("name", Op.EQUAL, String.class, "testName");
        Assert.assertEquals("{E}.name = 'testName'", jpqlGenerator.generateJpql(clause));

        clause = clause("name", Op.CONTAINS, String.class, "testName");
        Assert.assertEquals("{E}.name like '%testName%'", jpqlGenerator.generateJpql(clause));

        clause = clause("name", Op.DOES_NOT_CONTAIN, String.class, "testName");
        Assert.assertEquals("{E}.name not like '%testName%'", jpqlGenerator.generateJpql(clause));

        clause = clause("name", Op.NOT_EMPTY, String.class, "testName");
        Assert.assertEquals("{E}.name is not null", jpqlGenerator.generateJpql(clause));

        clause = clause("name", Op.STARTS_WITH, String.class, "testName");
        Assert.assertEquals("{E}.name like 'testName%'", jpqlGenerator.generateJpql(clause));

//        clause = clause("name", Op.IN, Integer.class, Arrays.<String>asList("1", "2", "3").toString());
//        Assert.assertEquals("{E}.name in ('1', '2', '3')", jpqlGenerator.generateJpql(clause));

        clause = clause("version", Op.EQUAL, Integer.class, "42");
        Assert.assertEquals("{E}.version = 42", jpqlGenerator.generateJpql(clause));

        clause = clause("version", Op.GREATER_OR_EQUAL, Integer.class, "42");
        Assert.assertEquals("{E}.version >= 42", jpqlGenerator.generateJpql(clause));

        clause = clause("version", Op.LESSER_OR_EQUAL, Integer.class, "42");
        Assert.assertEquals("{E}.version <= 42", jpqlGenerator.generateJpql(clause));

        clause = clause("version", Op.IN, String.class, Arrays.<Integer>asList(1, 2, 3).toString());
        Assert.assertEquals("{E}.version in (1, 2, 3)", jpqlGenerator.generateJpql(clause));

        clause = clause("version", Op.IN, String.class, new HashSet<>(Arrays.<Integer>asList(1, 2, 3)).toString());
        Assert.assertEquals("{E}.version in (1, 2, 3)", jpqlGenerator.generateJpql(clause));

        clause = clause("id", Op.EQUAL, UUID.class, "a66abe96-3b9d-11e2-9db2-3860770d7eaf");
        Assert.assertEquals("{E}.id = 'a66abe96-3b9d-11e2-9db2-3860770d7eaf'", jpqlGenerator.generateJpql(clause));
    }

    private Clause clause(String fieldName, Op operation, Class clazz, String paramValue) {
        Clause clause = new Clause(fieldName, ":param$parameter1", null, operation.name(), "");
        clause.parameters.iterator().next().setJavaClass(clazz);
        clause.parameters.iterator().next().setValue(paramValue);
        return clause;
    }
}
