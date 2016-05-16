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
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.app.scheduled.MethodInfo;
import com.haulmont.cuba.core.app.scheduled.MethodParameterInfo;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.CubaDefaultListableBeanFactory;
import org.apache.commons.lang.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.TargetClassAware;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Abstract class that is used for getting an information about middleware beans names and their methods
 */
public abstract class AbstractBeansMetadata {

    protected Logger log = LoggerFactory.getLogger(getClass());

    protected List<String> beansToIgnore = Arrays.asList("cubaDataSource", "entityManagerFactory", "hibernateSessionFactory",
            "mailSendTaskExecutor", "scheduler", "sqlSession", "sqlSessionFactory", "transactionManager",
            "cuba_ServerInfoService", "cuba_LoginService", "cuba_LocalizedMessageService");

    /**
     * Returns a map, the key is bean name, the value is a list of objects that hold a bean methods information
     */
    public Map<String, List<MethodInfo>> getAvailableBeans() {
        Map<String, List<MethodInfo>> result = new TreeMap<>();

        String[] beanNames = AppContext.getApplicationContext().getBeanDefinitionNames();
        for (String name : beanNames) {
            if (AppContext.getApplicationContext().isSingleton(name)
                    && !name.startsWith("org.springframework.")
                    && !getBeansToIgnore().contains(name)) {
                List<MethodInfo> availableMethods = getAvailableMethods(name);
                if (!availableMethods.isEmpty())
                    result.put(name, availableMethods);
            }
        }

        return result;
    }

    protected List<MethodInfo> getAvailableMethods(String beanName) {
        List<MethodInfo> methods = new ArrayList<>();
        try {
            AutowireCapableBeanFactory beanFactory = AppContext.getApplicationContext().getAutowireCapableBeanFactory();
            if (beanFactory instanceof CubaDefaultListableBeanFactory) {
                BeanDefinition beanDefinition = ((CubaDefaultListableBeanFactory) beanFactory).getBeanDefinition(beanName);
                if (beanDefinition.isAbstract())
                    return methods;
            }

            Object bean = AppBeans.get(beanName);

            List<Class> classes = ClassUtils.getAllInterfaces(bean.getClass());
            for (Class aClass : classes) {
                if (aClass.getName().startsWith("org.springframework."))
                    continue;

                Class<?> targetClass = bean instanceof TargetClassAware ? ((TargetClassAware) bean).getTargetClass() : bean.getClass();

                Service serviceAnn = targetClass.getAnnotation(Service.class);
                if (serviceAnn != null)
                    return methods;

                for (Method method : aClass.getMethods()) {
                    if (isMethodAvailable(method)) {
                        Method targetClassMethod = targetClass.getMethod(method.getName(), method.getParameterTypes());
                        List<MethodParameterInfo> methodParameters = getMethodParameters(targetClassMethod);
                        MethodInfo methodInfo = new MethodInfo(method.getName(), methodParameters);
                        addMethod(methods, methodInfo);
                    }
                }

                if (methods.isEmpty()) {
                    for (Method method : bean.getClass().getMethods()) {
                        if (!method.getDeclaringClass().equals(Object.class) && isMethodAvailable(method)) {
                            List<MethodParameterInfo> methodParameters = getMethodParameters(method);
                            MethodInfo methodInfo = new MethodInfo(method.getName(), methodParameters);
                            addMethod(methods, methodInfo);
                        }
                    }
                }
            }
        } catch (Throwable t) {
            log.debug(t.getMessage());
        }
        return methods;
    }

    protected void addMethod(List<MethodInfo> methods, MethodInfo methodInfo) {
        for (MethodInfo mi : methods) {
            if (mi.definitionEquals(methodInfo))
                return;
        }
        methods.add(methodInfo);
    }


    protected abstract boolean isMethodAvailable(Method method);

    protected List<MethodParameterInfo> getMethodParameters(Method method) {
        ArrayList<MethodParameterInfo> params = new ArrayList<>();

        Class<?>[] parameterTypes = method.getParameterTypes();

        LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);

        if (parameterTypes != null) {
            for (int i = 0; i < parameterTypes.length; i++) {
                String parameterName = parameterNames != null ? parameterNames[i] : "arg" + i;
                MethodParameterInfo parameterInfo = new MethodParameterInfo(parameterTypes[i].getName(), parameterName, null);
                params.add(parameterInfo);
            }
        }
        return params;
    }

    protected List<String> getBeansToIgnore() {
        return beansToIgnore;
    }

}
