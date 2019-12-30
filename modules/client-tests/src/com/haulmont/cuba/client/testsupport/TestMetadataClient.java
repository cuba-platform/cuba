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

package com.haulmont.cuba.client.testsupport;

import com.haulmont.cuba.core.global.ExtendedEntities;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.sys.DatatypeRegistryImpl;
import com.haulmont.cuba.core.sys.MetadataImpl;
import com.haulmont.cuba.core.sys.MetadataLoader;

import java.util.List;
import java.util.Map;

public class TestMetadataClient extends MetadataImpl {

    protected Map<String, List<String>> packages;

    public TestMetadataClient(Map<String, List<String>> packages, TestViewRepositoryClient viewRepository, GlobalConfig globalConfig) {
        this.packages = packages;

        this.viewRepository = viewRepository;
        viewRepository.setMetadata(this);

        extendedEntities = new ExtendedEntities(this);
        tools = new TestMetadataTools(this);
        datatypeRegistry = new DatatypeRegistryImpl();
        config = globalConfig;
    }

    @Override
    protected void initMetadata() {
        MetadataLoader metadataLoader = new TestMetadataLoader(packages);
        ((TestMetadataLoader) metadataLoader).setDatatypeRegistry(datatypeRegistry);
        metadataLoader.loadMetadata();
        rootPackages = metadataLoader.getRootPackages();
        this.session = metadataLoader.getSession();
    }
}