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

package com.haulmont.restapi.data;

import com.haulmont.chile.core.model.MetaClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MetaClassInfo {
    public String entityName;
    public List<MetaPropertyInfo> properties = new ArrayList<>();

    public MetaClassInfo(MetaClass metaClass) {
        this.entityName = metaClass.getName();

        properties.addAll(metaClass.getProperties().stream()
                .map(MetaPropertyInfo::new)
                .collect(Collectors.toList()));
    }
}
