/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

import javax.annotation.Nullable;

/**
 * This service is intended for fetching resources from the core layer.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface ResourceService {

    String NAME = "cuba_ResourceService";

    /**
     * Fetches a resource from the core layer.
     * @param name resource name in the ScriptingProvider notation
     * @return resource content as string
     */
    @Nullable
    String getResourceAsString(String name);
}
