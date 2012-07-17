package com.haulmont.chile.core.model;

import java.util.Collection;

public interface Session {
	MetaModel getModel(String name);
	Collection<MetaModel> getModels();
	
    MetaClass getClass(String name);
    MetaClass getClass(Class<?> clazz);

    Collection<MetaClass> getClasses();
}
