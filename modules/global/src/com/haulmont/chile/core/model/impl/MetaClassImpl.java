/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.model.impl;

import com.haulmont.chile.core.model.*;

import java.io.InvalidObjectException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

/**
 * @author abramov
 * @version $Id$
 */
@SuppressWarnings({"TransientFieldNotInitialized"})
public class MetaClassImpl extends MetadataObjectImpl<MetaClass> implements MetaClass {

	private transient Map<String, MetaProperty> propertyByName = new HashMap<String, MetaProperty>();
    private transient Map<String, MetaProperty> ownPropertyByName = new HashMap<String, MetaProperty>();

	private transient final MetaModel model;
    private transient Class javaClass;

    private static final long serialVersionUID = 7862691995170873154L;

    public MetaClassImpl(MetaModel model, String className) {
		super();

		this.model = model;
        this.name = className;

        ((MetaModelImpl) model).registerClass(this);
    }

    protected Object readResolve() throws InvalidObjectException {
        Session session = SessionImpl.serializationSupportSession;
        if (session == null) {
            return Proxy.newProxyInstance(
                    this.getClass().getClassLoader(),
                    new Class[] { MetaClass.class },
                    new MetaClassInvocationHandler(name)
            );
        } else {
            return session.getClass(name);
        }
    }


	public <T> T createInstance() throws InstantiationException, IllegalAccessException {
        final Class aClass = getJavaClass();
        if (aClass == null) throw new IllegalStateException(String.format("Can't find java class for metaClass '%s'", this));

        return (T) aClass.newInstance();
	}

	public <T> T createInstance(Class<T> clazz) {
		throw new UnsupportedOperationException();
	}

	public MetaModel getModel() {
		return model;
	}

    public Class getJavaClass() {
        return javaClass;
    }

    public Collection<MetaProperty> getProperties() {
		return propertyByName.values();
	}

	public MetaProperty getProperty(String name) {
		return propertyByName.get(name);
	}

    @Override
    public MetaProperty getPropertyNN(String name) {
        MetaProperty property = getProperty(name);
        if (property == null)
            throw new IllegalArgumentException("Property '" + name + "' not found in " + getName());
        return property;
    }

    public MetaPropertyPath getPropertyEx(String propertyPath) {
        String[] properties = propertyPath.split("[.]");
        List<MetaProperty> metaProperties = new ArrayList<MetaProperty>();

		MetaProperty currentProperty;
		MetaClass currentClass = this;

		for (String property : properties) {
			if (currentClass == null) break;
			currentProperty = currentClass.getProperty(property);
			if (currentProperty == null) break;
			
			final Range range = currentProperty.getRange();
			currentClass = range.isClass() ? range.asClass() : null;

            metaProperties.add(currentProperty);
		}
		
		return new MetaPropertyPath(this, metaProperties.toArray(new MetaProperty[metaProperties.size()]));
	}

    public MetaPropertyPath getPropertyPath(String propertyPath) {
        String[] properties = propertyPath.split("[.]");
        List<MetaProperty> metaProperties = new ArrayList<MetaProperty>();

		MetaProperty currentProperty;
		MetaClass currentClass = this;

		for (String property : properties) {
			if (currentClass == null) return null;
			currentProperty = currentClass.getProperty(property);
			if (currentProperty == null) return null;

			final Range range = currentProperty.getRange();
			currentClass = range.isClass() ? range.asClass() : null;

            metaProperties.add(currentProperty);
		}

		return new MetaPropertyPath(this, metaProperties.toArray(new MetaProperty[metaProperties.size()]));
    }

    public Collection<MetaProperty> getOwnProperties() {
        return ownPropertyByName.values();
    }

    public void setJavaClass(Class javaClass) {
        this.javaClass = javaClass;
        ((MetaModelImpl) model).registerClass(this);
    }

    public void addAncestor(MetaClass ancestorClass) {
        super.addAncestor(ancestorClass);
        for (MetaProperty metaProperty : ancestorClass.getProperties()) {
            propertyByName.put(metaProperty.getName(), metaProperty);
        }
    }

    public void registerProperty(MetaProperty metaProperty) {
        propertyByName.put(metaProperty.getName(), metaProperty);
        ownPropertyByName.put(metaProperty.getName(), metaProperty);
        for (MetaClass descendant : descendants) {
            ((MetaClassImpl) descendant).registerAncestorProperty(metaProperty);
        }
    }

    public void registerAncestorProperty(MetaProperty metaProperty) {
        final MetaProperty prop = propertyByName.get(metaProperty.getName());
        if (prop == null) {
            propertyByName.put(metaProperty.getName(), metaProperty);
            for (MetaClass descendant : descendants) {
                ((MetaClassImpl) descendant).registerAncestorProperty(metaProperty);
            }
        }
    }

    public String toString() {
        return name;
    }

    private static class MetaClassInvocationHandler implements InvocationHandler {

        private String name;
        private volatile MetaClass metaClass;

        public MetaClassInvocationHandler(String name) {
            this.name = name;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("hashCode".equals(method.getName())) {
                return hashCode();
            }
            if (metaClass == null) {
                synchronized (this) {
                    if (metaClass == null) {
                        Session session = SessionImpl.serializationSupportSession;
                        if (session == null)
                            throw new IllegalStateException("SerializationSupportSession is not initialized");
                        metaClass = session.getClass(name);
                    }
                }
            }
            return method.invoke(metaClass, args);
        }
    }
}
