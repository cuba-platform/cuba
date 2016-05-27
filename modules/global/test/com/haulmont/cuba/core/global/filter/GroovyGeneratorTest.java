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

package com.haulmont.cuba.core.global.filter;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

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
        Assert.assertEquals("{E}.name.startsWith('testName')", groovyGenerator.generateGroovy(clause));

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