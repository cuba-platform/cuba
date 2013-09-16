/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import org.springframework.core.io.ResourceLoader;

import javax.annotation.Nullable;
import java.io.InputStream;

/**
 * Central infrastructure interface for loading resources.
 *
 * Searches for a resource according to the following rules:
 * <ul>
 *     <li/> If the given location represents an URL, searches for this URL.
 *     <li/> If the given location starts from <code>classpath:</code> prefix, searches for a classpath resource.
 *     <li/> If not an URL, try to find a file below the <code>conf</code> directory using the given location
 *     as relative path. If a file found, uses this file.
 *     <li/> Otherwise searches for a classpath resource for the given location.
 * </ul>
 *
 * @author krivopustov
 * @version $Id$
 */
public interface Resources extends ResourceLoader {

    public static final String NAME = "cuba_Resources";

    /**
     * Searches for a resource according to the rules explained in {@link Resources} and returns the resource as stream
     * if found. The returned stream should be closed after use.
     * @param location  resource location
     * @return          InputStream or null if the resource is not found
     */
    @Nullable
    InputStream getResourceAsStream(String location);

    /**
     * Searches for a resource according to the rules explained in {@link Resources} and returns the resource as string
     * if found.
     * @param location  resource location
     * @return          resource content or null if the resource is not found
     */
    @Nullable
    String getResourceAsString(String location);
}
