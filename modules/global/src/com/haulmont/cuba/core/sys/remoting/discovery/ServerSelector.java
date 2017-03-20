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

package com.haulmont.cuba.core.sys.remoting.discovery;

import javax.annotation.Nullable;

/**
 * Interface for selecting a server from a middleware cluster.
 * <p>
 * Always inject or lookup it by name and not by type, because an application project can define several instances
 * of this type to work with different middleware blocks.
 */
public interface ServerSelector {

    /**
     * Default bean name used by the platform code.
     */
    String NAME = "cuba_ServerSelector";

    /**
     * Initialize a context for the current request.
     *
     * @return object that must be passed to {@link #getUrl(Object)}, {@link #success(Object)} and {@link #fail(Object)}
     *  for the current request
     */
    Object initContext();

    /**
     * Get server URL to be used for the current request.
     *
     * @param context obtained by the prior invocation of {@link #initContext()}
     * @return  available URL or null if there are no available servers
     */
    @Nullable
    String getUrl(Object context);

    /**
     * Mark the last obtained server URL as successfully invoked.
     *
     * @param context obtained by the prior invocation of {@link #initContext()}
     */
    void success(Object context);

    /**
     * Mark the last obtained server URL as failed.
     *
     * @param context obtained by the prior invocation of {@link #initContext()}
     */
    void fail(Object context);
}
