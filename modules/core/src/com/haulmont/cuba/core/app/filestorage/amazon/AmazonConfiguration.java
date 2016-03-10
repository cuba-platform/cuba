/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.filestorage.amazon;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultInt;

/**
 * @author degtyarjov
 * @version $Id$
 */
@Source(type = SourceType.DATABASE)
public interface AmazonConfiguration extends Config {

    @Property("cuba.amazonS3.accessKey")
    String getAccessKey();

    @Property("cuba.amazonS3.secretAccessKey")
    String getSecretAccessKey();

    @Property("cuba.amazonS3.region")
    String getRegionName();

    @Property("cuba.amazonS3.bucket")
    String getBucket();

    @Property("cuba.amazonS3.chunkSize")
    @DefaultInt(8192)
    int getChunkSize();
}
