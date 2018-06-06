/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.sys.javacl.JavaClassLoader;
import org.apache.commons.lang3.exception.ExceptionUtils;

import org.springframework.stereotype.Component;
import javax.inject.Inject;

import static java.lang.String.format;

@Component("cuba_ClassLoaderManager")
public class ClassLoaderManager implements ClassLoaderManagerMBean {
    @Inject
    protected Scripting scripting;
    @Inject
    protected JavaClassLoader javaClassLoader;

    @Override
    public String loadClass(String className) {
        try {
            Class<?> aClass = scripting.loadClassNN(className);
            return format("Loaded %s", aClass.toString());
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Override
    public String removeClass(String className) {
        try {
            boolean removed = scripting.removeClass(className);
            return removed ? format("Removed %s", className) : "No such class in cache";
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Override
    public String reloadClass(String className) {
        try {
            javaClassLoader.removeClass(className);
            return loadClass(className);
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Override
    public String getClassDependencies(String className) {
        try {
            if (javaClassLoader.isLoadedClass(className)) {
                return format("Dependencies \n%s\nDependent \n%s", javaClassLoader.getClassDependencies(className), javaClassLoader.getClassDependent(className));
            }
            return "No such class in cache";
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Override
    public String clearCache() {
        try {
            scripting.clearCache();
            return "Done";
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }
}