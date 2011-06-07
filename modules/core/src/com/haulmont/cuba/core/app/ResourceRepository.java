/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.12.2008 15:12:37
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.GlobalConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DEPRECATED!<br/>
 * Use {@link com.haulmont.cuba.core.global.ScriptingProvider} to load resources and cache them only when necessary.
 */
@ManagedBean(ResourceRepositoryAPI.NAME)
@Deprecated
public class ResourceRepository implements ResourceRepositoryMBean, ResourceRepositoryAPI
{
    private Log log = LogFactory.getLog(ResourceRepository.class);

    private String rootPath;

    private Map<String, Object> repository = new ConcurrentHashMap<String, Object>();

    private static final String MSG_UNABLE_TO_LOAD_RESOURCE = "Unable to load resource %s";

    @Inject
    public ResourceRepository(ConfigProvider configProvider) {
        String confDir = configProvider.doGetConfig(GlobalConfig.class).getConfDir();
        rootPath = confDir + "/";
    }

    public ResourceRepositoryAPI getAPI() {
        return this;
    }

    public String printContent() {
        List<String> list = new ArrayList<String>(repository.keySet());
        Collections.sort(list);
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s).append("\n");
        }
        return sb.toString();
    }

    public void evict(String name) {
        repository.remove(name);
    }

    public void evictAll() {
        repository.clear();
    }

    public boolean resourceExists(String name) {
        File file = new File(rootPath + name);
        return file.exists();
    }

    public InputStream getResAsStream(String name) {
        Object value = repository.get(name);
        if (value != null) {
            if (value instanceof byte[]) {
                return new ByteArrayInputStream((byte[]) value);
            }
            else if (value instanceof String) {
                return new ByteArrayInputStream(((String) value).getBytes());
            }
            else
                throw new IllegalStateException("Invalid cached resource type: " + value.getClass());
        }
        else {
            byte[] buffer = loadBytes(name);
            repository.put(name, buffer);
            return new ByteArrayInputStream(buffer);
        }
    }

    public String getResAsString(String name) {
        Object value = repository.get(name);
        if (value != null) {
            if (value instanceof String) {
                return (String) value;
            }
            else if (value instanceof byte[]) {
                return new String((byte[]) value);
            }
            else
                throw new IllegalStateException("Invalid cached resource type: " + value.getClass());
        }
        else {
            String str = loadString(name);
            repository.put(name, str);
            return str;
        }
    }

    private byte[] loadBytes(String name) {
        String path = rootPath + name;
        try {
            return FileUtils.readFileToByteArray(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(String.format(MSG_UNABLE_TO_LOAD_RESOURCE, name), e);
        }
    }

    private String loadString(String name) {
        byte[] bytes = loadBytes(name);
        return new String(bytes);
    }
}
