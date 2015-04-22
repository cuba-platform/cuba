/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.datatypes.impl;

import junit.framework.TestCase;

/**
 * @author krivopustov
 * @version $Id$
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