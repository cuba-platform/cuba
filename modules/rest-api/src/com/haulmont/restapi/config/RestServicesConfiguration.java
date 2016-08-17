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

package com.haulmont.restapi.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class is used for loading and storing an information about service methods that are available for REST API.
 * information loaded from configuration files defined by the {@code cuba.rest.servicesConfig} application property.
 * <p>
 * Configuration file must define method name and method argument names that will be user for method invocation by the
 * REST API.
 * <p>
 * Method parameter types can be omitted if the service doesn't contain an overloaded method with the same parameters
 * number. Otherwise, types must be defined.
 */
public class RestServicesConfiguration {

    protected Logger log = LoggerFactory.getLogger(RestServicesConfiguration.class);

    public static final String CUBA_REST_SERVICES_CONFIG_PROP_NAME = "cuba.rest.servicesConfig";

    protected Map<String, RestServiceInfo> serviceInfosMap = new ConcurrentHashMap<>();

    protected volatile boolean initialized;

    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    @Inject
    protected Resources resources;

    @Nullable
    public Method getServiceMethod(String serviceName, String methodName, List<String> methodParamNames) {
        lock.readLock().lock();
        try {
            checkInitialized();
            RestServiceInfo restServiceInfo = serviceInfosMap.get(serviceName);
            if (restServiceInfo == null) return null;
            Optional<RestMethodInfo> methodInfoOptional = restServiceInfo.getMethods().stream()
                    .filter(restMethodInfo -> methodName.equals(restMethodInfo.getName())
                            && paramsMatches(restMethodInfo.getParams(), methodParamNames))
                    .findFirst();
            if (methodInfoOptional.isPresent()) {
                return methodInfoOptional.get().getMethod();
            }
        } finally {
            lock.readLock().unlock();
        }
        return null;
    }

    protected boolean paramsMatches(List<RestMethodParamInfo> paramInfos, List<String> paramNames) {
        if (paramInfos.size() != paramNames.size()) return false;
        for (int i = 0; i < paramInfos.size(); i++) {
            if (!paramInfos.get(i).getName().equals(paramNames.get(i))) {
                return false;
            }
        }
        return true;
    }

    protected void checkInitialized() {
        if (!initialized) {
            lock.readLock().unlock();
            lock.writeLock().lock();
            try {
                if (!initialized) {
                    init();
                    initialized = true;
                }
            } finally {
                lock.readLock().lock();
                lock.writeLock().unlock();
            }
        }
    }

    protected void init() {
        String configName = AppContext.getProperty(CUBA_REST_SERVICES_CONFIG_PROP_NAME);
        StrTokenizer tokenizer = new StrTokenizer(configName);
        for (String location : tokenizer.getTokenArray()) {
            Resource resource = resources.getResource(location);
            if (resource.exists()) {
                InputStream stream = null;
                try {
                    stream = resource.getInputStream();
                    loadConfig(Dom4j.readDocument(stream).getRootElement());
                } catch (IOException e) {
                    throw new RuntimeException("Error on parsing rest services config", e);
                } finally {
                    IOUtils.closeQuietly(stream);
                }
            } else {
                log.warn("Resource " + location + " not found, ignore it");
            }
        }
    }

    protected void loadConfig(Element rootElem) {
        for (Element serviceElem : Dom4j.elements(rootElem, "service")) {
            String serviceName = serviceElem.attributeValue("name");
            if (!AppBeans.containsBean(serviceName)) {
                log.error("Service not found: {}", serviceName);
                continue;
            }
            Object service = AppBeans.get(serviceName);
            List<RestMethodInfo> methodInfos = new ArrayList<>();

            for (Element methodElem : Dom4j.elements(serviceElem, "method")) {
                String methodName = methodElem.attributeValue("name");
                List<RestMethodParamInfo> params = new ArrayList<>();
                for (Element paramEl : Dom4j.elements(methodElem, "param")) {
                    params.add(new RestMethodParamInfo(paramEl.attributeValue("name"), paramEl.attributeValue("type")));
                }
                Method method = _findMethod(serviceName, methodName, params);
                if (method != null) {
                    methodInfos.add(new RestMethodInfo(methodName, params, method));
                }
            }

            serviceInfosMap.put(serviceName, new RestServiceInfo(serviceName, methodInfos));
        }
    }

    @Nullable
    protected Method _findMethod(String serviceName, String methodName, List<RestMethodParamInfo> paramInfos) {
        List<Class> paramTypes = new ArrayList<>();
        for (RestMethodParamInfo paramInfo : paramInfos) {
            if (StringUtils.isNotEmpty(paramInfo.getType())) {
                try {
                    paramTypes.add(ClassUtils.forName(paramInfo.getType(), null));
                } catch (ClassNotFoundException e) {
                    log.error("Class {} for method parameter not found. Service: {}, method: {}, param: {}",
                            paramInfo.getType(),
                            serviceName,
                            methodName,
                            paramInfo.getName());
                    return null;
                }
            }
        }

        if (!paramTypes.isEmpty() && paramInfos.size() != paramTypes.size()) {
            log.error("Service method parameters types must be defined for all parameters or for none of them. Service: {}, method: {}",
                    serviceName, methodName);
            return null;
        }

        Object service = AppBeans.get(serviceName);
        Method serviceMethod;
        if (paramTypes.isEmpty()) {
            //trying to guess which method to invoke
            Method[] methods = service.getClass().getMethods();
            List<Method> appropriateMethods = new ArrayList<>();
            for (Method method : methods) {
                if (methodName.equals(method.getName()) && method.getParameterTypes().length == paramInfos.size()) {
                    appropriateMethods.add(method);
                }
            }
            if (appropriateMethods.size() == 1) {
                serviceMethod = appropriateMethods.get(0);
            } else if (appropriateMethods.size() > 1) {
                log.error("There are multiple methods with given argument numbers. Parameters type must be defined. Service: {}, method: {}",
                        serviceName, methodName);
                return null;
            } else {
                log.error("Method not found. Service: {}, method: {}, number of arguments: {}", serviceName, methodName, paramInfos.size());
                return null;
            }
        } else {
            try {
                serviceMethod = service.getClass().getMethod(methodName, paramTypes.toArray(new Class[paramTypes.size()]));
            } catch (NoSuchMethodException e) {
                log.error("Method not found. Service: {}, method: {}, argument types: {}", serviceName, methodName, paramTypes);
                return null;
            }
        }
        return serviceMethod;
    }

    public Collection<RestServiceInfo> getServiceInfos() {
        lock.readLock().lock();
        try {
            checkInitialized();
            return serviceInfosMap.values();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Nullable
    public RestServiceInfo getServiceInfo(String serviceName) {
        lock.readLock().lock();
        try {
            checkInitialized();
            return serviceInfosMap.get(serviceName);
        } finally {
            lock.readLock().unlock();
        }
    }

    public static class RestServiceInfo {
        protected String name;
        protected List<RestMethodInfo> methods = new ArrayList<>();

        public RestServiceInfo(String name, List<RestMethodInfo> methods) {
            this.name = name;
            this.methods = methods;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<RestMethodInfo> getMethods() {
            return methods;
        }

        public void setMethods(List<RestMethodInfo> methods) {
            this.methods = methods;
        }
    }

    public static class RestMethodInfo {
        protected String name;
        protected List<RestMethodParamInfo> params = new ArrayList<>();
        @JsonIgnore
        protected Method method;

        public RestMethodInfo(String name, List<RestMethodParamInfo> params, Method method) {
            this.name = name;
            this.params = params;
            this.method = method;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<RestMethodParamInfo> getParams() {
            return params;
        }

        public void setParams(List<RestMethodParamInfo> params) {
            this.params = params;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }
    }

    public static class RestMethodParamInfo {
        protected String name;
        protected String type;

        public RestMethodParamInfo(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
