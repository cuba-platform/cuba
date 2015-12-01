/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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

/**
 * @author degtyarjov
 * @version $Id$
 */
public class BaseGenericIdEntityTest {
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
