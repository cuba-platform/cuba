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

package com.haulmont.cuba.core.sys.jpql;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class EntityPathTest {
    @Test
    public void parseEntityPath() {
        EntityPath path = EntityPath.parseEntityPath("p.ni");
        assertEquals("p", path.topEntityVariableName);
        assertEquals("ni", path.lastEntityFieldPattern);
        assertArrayEquals(new String[0], path.traversedFields);

        path = EntityPath.parseEntityPath("p.");
        assertEquals("p", path.topEntityVariableName);
        assertEquals("", path.lastEntityFieldPattern);
        assertArrayEquals(new String[0], path.traversedFields);

        path = EntityPath.parseEntityPath("p.team.");
        assertEquals("p", path.topEntityVariableName);
        assertEquals("", path.lastEntityFieldPattern);
        assertArrayEquals(new String[]{"team"}, path.traversedFields);

        path = EntityPath.parseEntityPath("p.team.owner.na");
        assertEquals("p", path.topEntityVariableName);
        assertEquals("na", path.lastEntityFieldPattern);
        assertArrayEquals(new String[]{"team", "owner"}, path.traversedFields);

        path = EntityPath.parseEntityPath(".");
        Assert.assertNull(path.topEntityVariableName);
        assertEquals(null, path.lastEntityFieldPattern);
        assertArrayEquals(new String[0], path.traversedFields);
    }
}