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

import com.haulmont.cuba.core.entity.dummy.DummyIntegerIdEntity;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class JpqlGeneratorTest {

    @Test
    public void testScriptGeneration() throws Exception {
        SecurityJpqlGenerator jpqlGenerator = new SecurityJpqlGenerator();

        //test strings
        Clause clause = clause("name", Op.EQUAL, String.class, null);
        Assertions.assertEquals("{E}.name = null", jpqlGenerator.generateJpql(clause));

        clause = clause("name", Op.EQUAL, String.class, "testName");
        Assertions.assertEquals("{E}.name = 'testName'", jpqlGenerator.generateJpql(clause));

        clause = clause("name", Op.CONTAINS, String.class, "testName");
        Assertions.assertEquals("{E}.name like '%testName%'", jpqlGenerator.generateJpql(clause));

        clause = clause("name", Op.DOES_NOT_CONTAIN, String.class, "testName");
        Assertions.assertEquals("{E}.name not like '%testName%'", jpqlGenerator.generateJpql(clause));

        clause = clause("name", Op.NOT_EMPTY, String.class, "testName");
        Assertions.assertEquals("{E}.name is not null", jpqlGenerator.generateJpql(clause));

        clause = clause("name", Op.STARTS_WITH, String.class, "testName");
        Assertions.assertEquals("{E}.name like 'testName%'", jpqlGenerator.generateJpql(clause));

        clause = clause("name", Op.IN, String.class, "1,2,3");
        Assertions.assertEquals("{E}.name in ('1', '2', '3')", jpqlGenerator.generateJpql(clause));

        clause = clause("name", Op.IN, String.class, Arrays.<String>asList("1", "2", "3").toString());
        Assertions.assertEquals("{E}.name in ('1', '2', '3')", jpqlGenerator.generateJpql(clause));

        //test integers
        clause = clause("version", Op.EQUAL, Integer.class, "42");
        Assertions.assertEquals("{E}.version = 42", jpqlGenerator.generateJpql(clause));

        clause = clause("version", Op.GREATER_OR_EQUAL, Integer.class, "42");
        Assertions.assertEquals("{E}.version >= 42", jpqlGenerator.generateJpql(clause));

        clause = clause("version", Op.LESSER_OR_EQUAL, Integer.class, "42");
        Assertions.assertEquals("{E}.version <= 42", jpqlGenerator.generateJpql(clause));

        clause = clause("version", Op.IN, Integer.class, Arrays.<Integer>asList(1, 2, 3).toString());
        Assertions.assertEquals("{E}.version in (1, 2, 3)", jpqlGenerator.generateJpql(clause));

        clause = clause("version", Op.IN, Integer.class, new HashSet<>(Arrays.<Integer>asList(1, 2, 3)).toString());
        Assertions.assertEquals("{E}.version in (1, 2, 3)", jpqlGenerator.generateJpql(clause));

        clause = clause("version", Op.IN, Integer.class, "1,2,3");
        Assertions.assertEquals("{E}.version in (1, 2, 3)", jpqlGenerator.generateJpql(clause));

        //test enums
        clause = clause("state", Op.EQUAL, PermissionType.class, "SCREEN");
        Assertions.assertEquals("{E}.state = 10",
                jpqlGenerator.generateJpql(clause));

        clause = clause("state", Op.IN, PermissionType.class, "SCREEN, ENTITY_OP");
        Assertions.assertEquals("{E}.state in (10, 20)",
                jpqlGenerator.generateJpql(clause));

        clause = clause("state", Op.IN, PermissionType.class, Arrays.asList(PermissionType.SCREEN, PermissionType.ENTITY_OP).toString());
        Assertions.assertEquals("{E}.state in (10, 20)",
                jpqlGenerator.generateJpql(clause));

        //test uuids
        clause = clause("id", Op.EQUAL, UUID.class, "a66abe96-3b9d-11e2-9db2-3860770d7eaf");
        Assertions.assertEquals("{E}.id = 'a66abe96-3b9d-11e2-9db2-3860770d7eaf'", jpqlGenerator.generateJpql(clause));

        clause = clause("id", Op.IN, UUID.class, "a66abe96-3b9d-11e2-9db2-3860770d7eaf, a66abe96-3b9d-11e2-9db2-3860770d7eaf");
        Assertions.assertEquals("{E}.id in ('a66abe96-3b9d-11e2-9db2-3860770d7eaf', 'a66abe96-3b9d-11e2-9db2-3860770d7eaf')",
                jpqlGenerator.generateJpql(clause));

        clause = clause("id", Op.IN, UUID.class, Arrays.asList(UUID.fromString("a66abe96-3b9d-11e2-9db2-3860770d7eaf"),
                UUID.fromString("a66abe96-3b9d-11e2-9db2-3860770d7eaf")).toString());
        Assertions.assertEquals("{E}.id in ('a66abe96-3b9d-11e2-9db2-3860770d7eaf', 'a66abe96-3b9d-11e2-9db2-3860770d7eaf')",
                jpqlGenerator.generateJpql(clause));

        //test uuid entities
        clause = clause("user", Op.EQUAL, User.class, "a66abe96-3b9d-11e2-9db2-3860770d7eaf");
        Assertions.assertEquals("{E}.user.id = 'a66abe96-3b9d-11e2-9db2-3860770d7eaf'",
                jpqlGenerator.generateJpql(clause));

        clause = clause("user", Op.IN, User.class, "a66abe96-3b9d-11e2-9db2-3860770d7eaf, a66abe96-3b9d-11e2-9db2-3860770d7eaf");
        Assertions.assertEquals("{E}.user.id in ('a66abe96-3b9d-11e2-9db2-3860770d7eaf', " +
                        "'a66abe96-3b9d-11e2-9db2-3860770d7eaf')",
                jpqlGenerator.generateJpql(clause));

        clause = clause("user", Op.IN, User.class, Arrays.asList(UUID.fromString("a66abe96-3b9d-11e2-9db2-3860770d7eaf"),
                UUID.fromString("a66abe96-3b9d-11e2-9db2-3860770d7eaf")).toString());
        Assertions.assertEquals("{E}.user.id in ('a66abe96-3b9d-11e2-9db2-3860770d7eaf', " +
                        "'a66abe96-3b9d-11e2-9db2-3860770d7eaf')",
                jpqlGenerator.generateJpql(clause));

        //test integer entities
        clause = clause("user", Op.EQUAL, DummyIntegerIdEntity.class, "1");
        Assertions.assertEquals("{E}.user.id = 1",
                jpqlGenerator.generateJpql(clause));

        clause = clause("user", Op.IN, DummyIntegerIdEntity.class, "1, 2");
        Assertions.assertEquals("{E}.user.id in (1, 2)",
                jpqlGenerator.generateJpql(clause));

        clause = clause("user", Op.IN, DummyIntegerIdEntity.class, Arrays.asList(1, 2).toString());
        Assertions.assertEquals("{E}.user.id in (1, 2)",
                jpqlGenerator.generateJpql(clause));
    }

    private Clause clause(String fieldName, Op operation, Class clazz, String paramValue) {
        Clause clause = new Clause(fieldName, ":param$parameter1", null, operation.name(), "");
        clause.getCompiledParameters().iterator().next().setJavaClass(clazz);
        clause.getCompiledParameters().iterator().next().setValue(paramValue);
        return clause;
    }
}