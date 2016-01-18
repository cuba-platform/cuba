/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.filestorage.amazon;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.defaults.DefaultInt;

/**
 * @author degtyarjov
 * @version $Id$
 */
public interface AmazonConfiguration extends Config {
    @Property("cuba.amazon.s3.accessKey")
    String getAccessKey();

    @Property("cuba.amazon.s3.secretAccessKey")
    String getSecretAccessKey();

    @Property("cuba.amazon.s3.region")
    String getRegionName();

    @Property("cuba.amazon.s3.bucket")
    String getBucket();

    @Property("cuba.amazon.s3.chunkSize")
    @DefaultInt(8192)
    int getChunkSize();
}
