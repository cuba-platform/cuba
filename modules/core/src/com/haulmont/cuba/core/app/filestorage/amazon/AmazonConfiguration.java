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

package com.haulmont.cuba.core.app.filestorage.amazon;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultInt;

/**
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
