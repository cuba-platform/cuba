package com.haulmont.chile.core.loader;

import com.haulmont.chile.core.model.Session;

public interface ClassMetadataLoader {
    Session loadPackage(String modelName, String packageName);
    Session loadClass(String modelName, Class<?> clazz);
    Session loadClass(String modelName, String className);

    Session getSession();
}
