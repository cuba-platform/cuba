/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.persistence;

import junit.framework.TestCase;

import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;

public class UuidBinaryValueHandlerTest extends TestCase
{
    private UuidBinaryValueHandler handler = new UuidBinaryValueHandler();

    public void test() {
        UUID uuid = UUID.fromString("60885987-1b61-4247-94c7-dff348347f93");

        byte[] bytes = (byte[]) handler.toDataStoreValue(null, uuid, null);
        UUID val = (UUID) handler.toObjectValue(null, bytes);

        assertEquals(uuid, val);
    }
}
