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

import com.haulmont.bali.util.Dom4j;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.parsers.SAXParser;

/**
 * Object factory for SAXParser object pool.
 */
public class SAXParserObjectFactory extends BasePooledObjectFactory<SAXParser> {

    private static final Logger log = LoggerFactory.getLogger(SAXParserObjectFactory.class);

    @Override
    public SAXParser create() throws Exception {
        return Dom4j.getParser();
    }

    @Override
    public PooledObject<SAXParser> wrap(SAXParser obj) {
        return new DefaultPooledObject<>(obj);
    }

}
