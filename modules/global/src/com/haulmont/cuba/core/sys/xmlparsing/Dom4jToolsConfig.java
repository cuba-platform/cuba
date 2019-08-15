/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.core.sys.xmlparsing;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultInt;
import com.haulmont.cuba.core.config.defaults.DefaultLong;

/**
 * @see Dom4jTools
 */
@Source(type = SourceType.APP)
public interface Dom4jToolsConfig extends Config {

    /**
     * @return maximum number of SAXParser instances available for concurrent use.
     */
    @DefaultInt(100)
    @Property("cuba.dom4j.maxPoolSize")
    int getMaxPoolSize();

    /**
     * @return timeout to borrow SAXParser instance from object pool.
     */
    @DefaultLong(10000)
    @Property("cuba.dom4j.maxBorrowWaitMillis")
    long getMaxBorrowWaitMillis();
}
