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

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.haulmont.cuba.core.Locator;

public class ResourceRepository implements ResourceRepositoryMBean
{
    private String rootPath;

    private Map<String, Object> repository = new ConcurrentHashMap<String, Object>();

    private static final String MSG_UNABLE_TO_LOAD_RESOURCE = "Unable to load resource %s";

    public void create() {
        String confUrl = System.getProperty("jboss.server.config.url");
        if (confUrl == null)
            throw new IllegalStateException("Environment variable jboss.server.config.url is not set");
        rootPath = URI.create(confUrl).getPath() + "/";
    }

    public void start() {
        loadSystemProperties();
    }

    public ResourceRepository getImplementation() {
        return this;
    }

    public String getContent() {
        List<String> list = new ArrayList<String>(repository.keySet());
        Collections.sort(list);
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s).append("<br>");
        }
        return sb.toString();
    }

    public void evict(String name) {
        repository.remove(name);
    }

    public void evictAll() {
        repository.clear();
    }

    /**
     * Loads resource into cache as byte array and returns it
     * @param name resource file name relative to resources root (jboss/server/default/conf)
     * @return resource as stream
     */
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

    /**
     * Loads resource into cache as String and returns it
     * @param name resource file name relative to resources root (jboss/server/default/conf)
     * @return String resource
     */
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

    public String loadSystemProperties() {
        String confUrl = System.getProperty("jboss.server.config.url");
        try {
            StringBuilder sb = new StringBuilder();

            String fileName = URI.create(confUrl).getPath() + "system.properties";
            File file = new File(fileName);
            if (file.exists()) {
                InputStream is = new FileInputStream(fileName);
                Properties props;
                try {
                    props = new Properties();
                    props.load(is);
                } finally {
                    is.close();
                }

                for (Map.Entry<Object, Object> entry : props.entrySet()) {
                    if ("".equals(entry.getValue())) {
                        System.getProperties().remove(entry.getKey());
                    }
                    else {
                        System.getProperties().put(entry.getKey(), entry.getValue());
                    }
                }
                sb.append("Properties from ").append(fileName).append(" loaded succesfully\n\n");
            }
            else {
                sb.append("File ").append(fileName).append(" not found\n\n");
            }

            List<String> strings = new ArrayList<String>(System.getProperties().size());
            for (Map.Entry<Object, Object> entry : System.getProperties().entrySet()) {
                strings.add(entry.getKey().toString() + "=" + entry.getValue().toString());
            }
            Collections.sort(strings);
            sb.append("Current system properties:\n\n");
            for (String s : strings) {
                sb.append(StringEscapeUtils.escapeHtml(s)).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }
}
