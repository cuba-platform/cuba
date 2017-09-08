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
 */

package com.haulmont.cuba.client.testsupport;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.Session;
import com.haulmont.cuba.core.sys.EntityClassInfo;
import com.haulmont.cuba.core.sys.MetaModelLoader;
import com.haulmont.cuba.core.sys.MetadataLoader;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestMetadataLoader extends MetadataLoader {

    private Map<String, List<String>> packages;

    public TestMetadataLoader(Map<String, List<String>> packages) {
        this.packages = packages;
    }

    @Override
    protected MetaModelLoader createModelLoader(Session session) {
        return new MetaModelLoader(session);
    }

    @Override
    public void loadMetadata() {
        for (Map.Entry<String, List<String>> entry : packages.entrySet()) {
            List<EntityClassInfo> classInfos = entry.getValue().stream()
                    .map(name -> new EntityClassInfo(null, name, false))
                    .collect(Collectors.toList());
            modelLoader.loadModel(entry.getKey(), classInfos);
        }
        for (MetaClass metaClass : session.getClasses()) {
            postProcessClass(metaClass);
            initMetaAnnotations(metaClass);
        }
    }
}
