/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global.filter;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class GroovyGeneratorTest {

    @Test
    public void testScriptGeneration() throws Exception {
        GroovyGenerator groovyGenerator = new GroovyGenerator();
        Clause clause = clause("name", Op.EQUAL, String.class, null);
        Assert.assertEquals("{E}.name == null", groovyGenerator.generateGroovy(clause));

        clause = clause("name", Op.EQUAL, String.class, "testName");
        Assert.assertEquals("{E}.name == 'testName'", groovyGenerator.generateGroovy(clause));

        clause = clause("name", Op.CONTAINS, String.class, "testName");
        Assert.assertEquals("{E}.name.contains('testName')", groovyGenerator.generateGroovy(clause));

        clause = clause("name", Op.DOES_NOT_CONTAIN, String.class, "testName");
        Assert.assertEquals("!({E}.name.contains('testName'))", groovyGenerator.generateGroovy(clause));

        clause = clause("name", Op.NOT_EMPTY, String.class, "testName");
        Assert.assertEquals("{E}.name != null", groovyGenerator.generateGroovy(clause));

        clause = clause("name", Op.STARTS_WITH, String.class, "testName");
        Assert.assertEquals("{E}.name.startWith('testName')", groovyGenerator.generateGroovy(clause));

//        clause = clause("name", Op.IN, Integer.class, Arrays.<String>asList("1", "2", "3").toString());
//        Assert.assertEquals("{E}.name in ['1', '2', '3']", groovyGenerator.generateGroovy(clause));

        clause = clause("version", Op.EQUAL, Integer.class, "42");
        Assert.assertEquals("{E}.version == 42", groovyGenerator.generateGroovy(clause));

        clause = clause("version", Op.GREATER_OR_EQUAL, Integer.class, "42");
        Assert.assertEquals("{E}.version >= 42", groovyGenerator.generateGroovy(clause));

        clause = clause("version", Op.LESSER_OR_EQUAL, Integer.class, "42");
        Assert.assertEquals("{E}.version <= 42", groovyGenerator.generateGroovy(clause));

        clause = clause("version", Op.IN, Integer.class, Arrays.<Integer>asList(1, 2, 3).toString());
        Assert.assertEquals("{E}.version in [1, 2, 3]", groovyGenerator.generateGroovy(clause));

        clause = clause("version", Op.IN, Integer.class, new HashSet<>(Arrays.<Integer>asList(1, 2, 3)).toString());
        Assert.assertEquals("{E}.version in [1, 2, 3]", groovyGenerator.generateGroovy(clause));

        clause = clause("id", Op.EQUAL, UUID.class, "a66abe96-3b9d-11e2-9db2-3860770d7eaf");
        Assert.assertEquals("{E}.id == value(java.util.UUID.class, 'a66abe96-3b9d-11e2-9db2-3860770d7eaf')", groovyGenerator.generateGroovy(clause));
    }

    private Clause clause(String fieldName, Op operation, Class clazz, String paramValue) {
        Clause clause = new Clause(fieldName, ":param$parameter1", null, operation.name(), "");
        clause.parameters.iterator().next().setJavaClass(clazz);
        clause.parameters.iterator().next().setValue(paramValue);
        return clause;
    }
}
