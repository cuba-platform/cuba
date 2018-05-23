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

import com.haulmont.chile.core.datatypes.DatatypeRegistry;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaModel;
import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.core.model.impl.MetaClassImpl;
import com.haulmont.chile.core.model.impl.MetaModelImpl;
import com.haulmont.chile.core.model.impl.SessionImpl;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributes;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.User;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class BaseGenericIdEntityTest {

    @SuppressWarnings("unused")
    @Mocked
    protected AppBeans appBeans;

    protected DynamicAttributes dynamicAttributes;

    protected Metadata metadata;

    @Mocked
    protected ReferenceToEntitySupport referenceToEntitySupport;

    @Before
    public void setUp() throws Exception {
        dynamicAttributes = new DynamicAttributes() {
            @Override
            public Collection<Category> getCategoriesForMetaClass(MetaClass metaClass) {
                return null;
            }

            @Override
            public Collection<CategoryAttribute> getAttributesForMetaClass(MetaClass metaClass) {
                return null;
            }

            @Nullable
            @Override
            public CategoryAttribute getAttributeForMetaClass(MetaClass metaClass, String code) {
                CategoryAttribute categoryAttribute = new CategoryAttribute();
                categoryAttribute.setCode(code);
                return categoryAttribute;
            }
        };

        metadata = new Metadata() {
            @Override
            public Session getSession() {
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

            @Override
            public ViewRepository getViewRepository() {
                return null;
            }

            @Override
            public ExtendedEntities getExtendedEntities() {
                return null;
            }

            @Override
            public MetadataTools getTools() {
                return null;
            }

            @Override
            public DatatypeRegistry getDatatypes() {
                return null;
            }

             @Override
            public <T> T create(Class<T> entityClass) {
                if (User.class.equals(entityClass)) {
                    return (T) new User();
                }
                if (CategoryAttributeValue.class.equals(entityClass)) {
                    CategoryAttributeValue attributeValue = new CategoryAttributeValue();
                    attributeValue.setEntity(new ReferenceToEntity());
                    return (T) attributeValue;
                }
                throw new IllegalArgumentException("Add support for " + entityClass.getSimpleName() + " to Mock");
            }

            @Override
            public Entity create(MetaClass metaClass) {
                return null;
            }

            @Override
            public Entity create(String entityName) {
                return null;
            }

            @Override
            public List<String> getRootPackages() {
                return null;
            }

            @Override
            public MetaModel getModel(String name) {
                return null;
            }

            @Override
            public Collection<MetaModel> getModels() {
                return null;
            }

            @Nullable
            @Override
            public MetaClass getClass(String name) {
                return null;
            }

            @Override
            public MetaClass getClassNN(String name) {
                return null;
            }

            @Nullable
            @Override
            public MetaClass getClass(Class<?> clazz) {
                return null;
            }

            @Override
            public MetaClass getClassNN(Class<?> clazz) {
                return null;
            }

            @Override
            public Collection<MetaClass> getClasses() {
                return null;
            }
        };

        new Expectations() {
            {
                AppBeans.get(DynamicAttributes.NAME); result = dynamicAttributes; minTimes = 0;
                AppBeans.get(Metadata.NAME); result = metadata; minTimes = 0;
                AppBeans.get(ReferenceToEntitySupport.class); result = referenceToEntitySupport; minTimes = 0;
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