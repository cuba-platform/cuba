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

package com.haulmont.cuba.core.sys.javacl;

import org.apache.commons.lang.exception.ExceptionUtils;

import org.springframework.stereotype.Component;
import javax.inject.Inject;

import static java.lang.String.format;

/**
 */
@Component("cuba_ClassLoaderManager")
public class ClassLoaderManager implements ClassLoaderManagerMBean {
    @Inject
    protected JavaClassLoader javaClassLoader;

    @Override
    public String loadClass(String className) {
        try {
            Class<?> aClass = javaClassLoader.loadClass(className);
            return format("Loaded %s", aClass.toString());
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Override
    public String removeClass(String className) {
        try {
            TimestampClass removed = javaClassLoader.compiled.remove(className);
            if (removed != null) {
                for (String dependent : removed.dependent) {
                    removeClass(dependent);
                }
            }
            return removed != null ? format("Removed %s", removed.clazz.toString()) : "No such class in cache";
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Override
    public String reloadClass(String className) {
        try {
            removeClass(className);
            return loadClass(className);
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Override
    public String getClassDependencies(String className) {
        try {
            TimestampClass timestampClass = javaClassLoader.compiled.get(className);
            if (timestampClass != null) {
                return format("Dependencies \n%s\nDependent \n%s", timestampClass.dependencies, timestampClass.dependent);
            }

            return "No such class in cache";
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Override
    public String clearCache() {
        try {
            javaClassLoader.clearCache();
            return "Done";
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }
}
