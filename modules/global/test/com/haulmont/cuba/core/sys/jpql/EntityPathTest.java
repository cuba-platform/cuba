/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.jpql;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Author: Alexander Chevelev
 * Date: 08.04.2011
 * Time: 0:11:17
 */
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
    }

}
