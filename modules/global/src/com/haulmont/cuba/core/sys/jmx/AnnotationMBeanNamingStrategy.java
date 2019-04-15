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

package com.haulmont.cuba.core.sys.jmx;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.sys.AppContext;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jmx.export.metadata.JmxAttributeSource;
import org.springframework.jmx.export.naming.ObjectNamingStrategy;
import org.springframework.jmx.support.ObjectNameManager;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * An implementation of the {@link ObjectNamingStrategy} interface
 * that reads object name from {@link JmxBean} annotation.
 * Falls back to the bean key (bean name) if no {@link JmxBean} annotation
 * can be found.
 */
public class AnnotationMBeanNamingStrategy implements ObjectNamingStrategy {
    @Override
    public ObjectName getObjectName(Object managedBean, String beanKey) throws MalformedObjectNameException {
        Class<?> managedClass = AopUtils.getTargetClass(managedBean);
        JmxBean ann = AnnotationUtils.findAnnotation(managedClass, JmxBean.class);
        if (ann != null) {
            return ObjectNameManager.getInstance(getDomain(ann.module()), "type", ann.alias());
        } else {
            return ObjectNameManager.getInstance(beanKey);
        }
    }

    protected String getDomain(String module) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(AppContext.getProperty("cuba.webContextName"));
        if (!Strings.isNullOrEmpty(module)) {
            stringBuilder.append(".").append(module);
        }
        return stringBuilder.toString();
    }
}
