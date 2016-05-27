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

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.datatypes.impl.EnumerationImpl;
import com.haulmont.cuba.core.entity.FtsChangeType;
import com.haulmont.cuba.security.entity.PermissionType;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;

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