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

package com.haulmont.chile.core.datatypes.impl;

import junit.framework.TestCase;

/**
 */
public class EnumUtilsTest extends TestCase {

    public void testFromId() throws Exception {
        Unit unit;

        unit = Unit.fromId("pcs");
        assertTrue(unit == Unit.PCS);

        unit = Unit.fromId(null);
        assertNull(unit);

        try {
            Unit.fromId("bla");
            fail();
        } catch (Exception ignore) {
        }
    }

    public void testFromIdDefault() throws Exception {
        Unit unit;

        unit = EnumUtils.fromId(Unit.class, "pcs", Unit.M);
        assertTrue(unit == Unit.PCS);

        unit = EnumUtils.fromId(Unit.class, null, Unit.M);
        assertTrue(unit == Unit.M);

        try {
            EnumUtils.fromId(Unit.class, "bla", Unit.M);
            fail();
        } catch (Exception ignore) {
        }
    }

    public void testFromIdSafe() throws Exception {
        Unit unit;

        unit = EnumUtils.fromIdSafe(Unit.class, "pcs", Unit.M);
        assertTrue(unit == Unit.PCS);

        unit = EnumUtils.fromIdSafe(Unit.class, null, Unit.M);
        assertTrue(unit == Unit.M);

        unit = EnumUtils.fromIdSafe(Unit.class, "bla", null);
        assertNull(unit);
    }

    public enum Unit implements EnumClass<String> {
        PCS("pcs"),
        KG("kg"),
        M("m");

        private String id;

        Unit(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }

        public static Unit fromId(String id) {
            return EnumUtils.fromId(Unit.class, id);
        }
    }
}