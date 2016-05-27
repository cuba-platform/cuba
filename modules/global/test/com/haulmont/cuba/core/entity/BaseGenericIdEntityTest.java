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

package com.haulmont.cuba.core.entity;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.core.model.impl.MetaClassImpl;
import com.haulmont.chile.core.model.impl.MetaModelImpl;
import com.haulmont.chile.core.model.impl.SessionImpl;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributes;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.security.entity.User;
import org.junit.Assert;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class BaseGenericIdEntityTest {

    @SuppressWarnings("unused")
    @Mocked
    protected AppBeans appBeans;

    @Mocked
    protected DynamicAttributes dynamicAttributes;

    @Mocked
    protected Metadata metadata;

    @Before
    public void setUp() throws Exception {
        dynamicAttributes = new MockUp<DynamicAttributes>() {
            @SuppressWarnings("UnusedDeclaration")
            @Mock
            CategoryAttribute getAttributeForMetaClass(MetaClass metaClass, String code) {
                CategoryAttribute categoryAttribute = new CategoryAttribute();
                categoryAttribute.setCode(code);
                return categoryAttribute;
            }
        }.getMockInstance();

        metadata = new MockUp<Metadata>() {
            @SuppressWarnings("UnusedDeclaration")
            @Mock
            Session getSession(){
                return new SessionImpl() {
                    @Override
                    public MetaClass getClassNN(String name) {
                        return new MetaClassImpl(new MetaModelImpl(this, name), name);
                    }

                    @Override
                    public MetaClass getClassNN(Class clazz) {
                        return new MetaClassImpl(new MetaModelImpl(this, clazz.getName()), clazz.getName());
                    }
                };
            }

            @SuppressWarnings({"UnusedDeclaration", "unchecked"})
            @Mock
            <T> T create(Class<T> entityClass) {
                if (User.class.equals(entityClass)) {
                    return (T) new User();
                }
                if (CategoryAttributeValue.class.equals(entityClass)) {
                    return (T) new CategoryAttributeValue();
                }
                throw new IllegalArgumentException("Add support for " + entityClass.getSimpleName() + " to Mock");
            }
        }.getMockInstance();

        new NonStrictExpectations() {
            {
                AppBeans.get(DynamicAttributes.NAME); result = dynamicAttributes;
                AppBeans.get(Metadata.NAME); result = metadata;
            }
        };
    }

    @Test
    public void testDynamicAttributes() throws Exception {
        User user = new User();

        try {
            user.setValue("+extend", null);
            Assert.fail("should fail with exception");
        } catch (Exception e) {
            //do nothing
        }

        user.setDynamicAttributes(new HashMap<>());

        user.setValue("+extend", "some dynamic value");
        Assert.assertEquals("some dynamic value", user.getValue("+extend"));

        user.setValue("+extend", "another dynamic value");
        Assert.assertEquals("another dynamic value", user.getValue("+extend"));
    }
}