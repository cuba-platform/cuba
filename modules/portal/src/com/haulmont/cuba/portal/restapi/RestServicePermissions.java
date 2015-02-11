/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.restapi;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;
import org.springframework.core.io.Resource;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class holds an information about services that allowed to be invoked with REST API.
 * Configuration is loaded from {@code *-rest-services.xml} files.
 *
 * @author gorbunkov
 * @version $Id$
 */
@ManagedBean(RestServicePermissions.NAME)
public class RestServicePermissions {

    public static final String NAME = "cuba_RestServicePermissions";

    public static final String CUBA_REST_SERVICES_CONFIG_PROP_NAME = "cuba.restServicesConfig";

    @Inject
    protected Resources resources;

    protected static Log log = LogFactory.getLog(RestServicePermissions.class);

    protected Map<String, Set<String>> serviceMethods = new ConcurrentHashMap<>();

    protected volatile boolean initialized;

    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Checks whether method of service is allowed to be invoked with REST API
     */
    public boolean isPermitted(String serviceName, String methodName) {
        lock.readLock().lock();
        try {
            checkInitialized();
            Set<String> methods = serviceMethods.get(serviceName);
            return methods != null && methods.contains(methodName);
        } finally {
            lock.readLock().unlock();
        }
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
                    throw new RuntimeException(e);
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

            for (Element methodElem : Dom4j.elements(serviceElem, "method")) {
                String methodName = methodElem.attributeValue("name");
                addServiceMethod(serviceName, methodName);
            }
        }
    }

    protected void addServiceMethod(String serviceName, String methodName) {
        Set<String> methods = serviceMethods.get(serviceName);
        if (methods == null) {
            methods = new HashSet<>();
            serviceMethods.put(serviceName, methods);
        }
        methods.add(methodName);
    }

}
