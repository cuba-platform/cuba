/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.testmodel.related_properties.EntityWithRelatedProperties;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PropertyChangedTest {
    @ClassRule
    public static final TestContainer cont = TestContainer.Common.INSTANCE;

    @Test
    public void relatedPropertyChangedTest() {
        EntityWithRelatedProperties testEntity = new EntityWithRelatedProperties();
        List<String> changedAttrs = new ArrayList<>();

        testEntity.addPropertyChangeListener(p -> {
            changedAttrs.add(p.getProperty());
            if ("name".equals(p.getProperty())) {
                assertNull(p.getPrevValue());
                assertEquals("Name", p.getValue());
            }
            if ("nickName".equals(p.getProperty())) {
                assertNull(p.getPrevValue());
                assertEquals("Name MegaCool null", p.getValue());
            }
            if ("someAttr".equals(p.getProperty())) {
                assertNull(p.getPrevValue());
                assertEquals("Name MegaCool null additional string", p.getValue());
            }
        });

        testEntity.setName("Name");
        assertEquals(3, changedAttrs.size());
        assertTrue(changedAttrs.contains("name"));
        assertTrue(changedAttrs.contains("nickName"));
        assertTrue(changedAttrs.contains("someAttr"));
    }

    @Test
    public void notRelatedPropertyChangedTest() {
        EntityWithRelatedProperties testEntity = new EntityWithRelatedProperties();
        List<String> changedAttrs = new ArrayList<>();

        testEntity.addPropertyChangeListener(p -> {
            changedAttrs.add(p.getProperty());

            assertEquals("notRelatedAttr", p.getProperty());
            assertNull(p.getPrevValue());
            assertEquals("SomeValue", p.getValue());

        });

        testEntity.setNotRelatedAttr("SomeValue");
        assertEquals(1, changedAttrs.size());
        assertTrue(changedAttrs.contains("notRelatedAttr"));
        assertFalse(changedAttrs.contains("nickName"));
        assertFalse(changedAttrs.contains("someAttr"));
    }
}
