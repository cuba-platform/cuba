/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.datatypes.impl.EnumerationImpl;
import com.haulmont.cuba.core.entity.FtsChangeType;
import com.haulmont.cuba.security.entity.PermissionType;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;

/**
 * @author gorbunkov
 * @version $Id$
 */
public class EnumerationImplTest {
    @Test
    public void testParseIntegerId() throws ParseException {
        EnumerationImpl enumeration = new EnumerationImpl(PermissionType.class);
        Enum enumValue = enumeration.parse("10");
        Assert.assertEquals(PermissionType.SCREEN, enumValue);
    }

    @Test
    public void testParseStringId() throws ParseException {
        EnumerationImpl enumeration = new EnumerationImpl(FtsChangeType.class);
        Enum enumValue = enumeration.parse("I");
        Assert.assertEquals(FtsChangeType.INSERT, enumValue);
    }

    @Test
    public void testParseNonExistingId() throws ParseException {
        EnumerationImpl enumeration = new EnumerationImpl(FtsChangeType.class);
        Enum enumValue = enumeration.parse("ZZZ");
        Assert.assertNull(enumValue);
    }
}
