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

package com.haulmont.chile.core.model.impl;

import com.haulmont.chile.core.model.*;

import java.io.InvalidObjectException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@SuppressWarnings({"TransientFieldNotInitialized"})
public class MetaPropertyImpl extends MetadataObjectImpl implements MetaProperty {

    private MetaClass domain;
    private transient final MetaModel model;

    private transient boolean mandatory;
    private transient boolean readOnly;
    private transient Type type;
    private transient Range range;

    private transient MetaProperty inverse;

    private transient AnnotatedElement annotatedElement;
    private transient Class<?> javaType;
    private transient Class<?> declaringClass;

    private static final long serialVersionUID = -2827471157045502206L;

    public MetaPropertyImpl(MetaClass domain, String name) {
        this.domain = domain;
        this.model = domain.getModel();
        this.name = name;

        ((MetaClassImpl) domain).registerProperty(this);
    }

    protected Object readResolve() throws InvalidObjectException {
        Session session = SessionImpl.serializationSupportSession;
        if (session == null) {
            return Proxy.newProxyInstance(
                    this.getClass().getClassLoader(),
                    new Class[]{MetaProperty.class},
                    new MetaPropertyInvocationHandler(domain, name)
            );
        } else {
            return domain.getProperty(name);
        }
    }

    @Override
    public MetaClass getDomain() {
        return domain;
    }

    public void setDomain(MetaClass domain) {
        this.domain = domain;
    }

    @Override
    public MetaProperty getInverse() {
        return inverse;
    }

    public void setInverse(MetaProperty inverse) {
        this.inverse = inverse;
    }

    @Override
    public MetaModel getModel() {
        return model;
    }

    @Override
    public Range getRange() {
        return range;
    }

    @Override
    public AnnotatedElement getAnnotatedElement() {
        return annotatedElement;
    }

    public void setAnnotatedElement(AnnotatedElement annotatedElement) {
        this.annotatedElement = annotatedElement;
    }

    @Override
    public Class<?> getJavaType() {
        return javaType;
    }

    public void setJavaType(Class<?> javaType) {
        this.javaType = javaType;
    }

    @Override
    public Class<?> getDeclaringClass() {
        return declaringClass;
    }

    public void setDeclaringClass(Class<?> declaringClass) {
        this.declaringClass = declaringClass;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    @Override
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public String toString() {
        return domain.getName() + "." + name;
    }

    private static class MetaPropertyInvocationHandler implements InvocationHandler {

        private MetaClass domain;
        private String name;
        private volatile MetaProperty metaProperty;

        public MetaPropertyInvocationHandler(MetaClass domain, String name) {
            this.domain = domain;
            this.name = name;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("hashCode".equals(method.getName())) {
                return hashCode();
            }
            if (metaProperty == null) {
                synchronized (this) {
                    if (metaProperty == null) {
                        metaProperty = domain.getProperty(name);
                    }
                }
            }
            return method.invoke(metaProperty, args);
        }
    }
}