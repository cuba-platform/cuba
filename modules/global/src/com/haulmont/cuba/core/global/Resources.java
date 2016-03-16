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
 */
public interface Resources extends ResourceLoader {

    String NAME = "cuba_Resources";

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
