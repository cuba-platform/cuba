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

package com.haulmont.restapi.service;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.restapi.common.RestControllerUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class that executes business logic required by the {@link com.haulmont.restapi.controllers.LocalizationController}.
 */
@Component("cuba_LocalizationControllerManager")
public class LocalizationControllerManager {

    @Inject
    protected RestControllerUtils restControllerUtils;

    @Inject
    protected MessageTools messageTools;

    @Inject
    protected Metadata metadata;

    public Map<String, String> getLocalizationForEntity(String entityName) {
        MetaClass metaClass = restControllerUtils.getMetaClass(entityName);
        return getStringStringMap(metaClass);
    }

    protected Map<String, String> getStringStringMap(MetaClass metaClass) {
        Map<String, String> locMap = new TreeMap<>();
        locMap.put(metaClass.getName(), messageTools.getEntityCaption(metaClass));
        metaClass.getProperties().forEach(metaProperty -> {
            String msgKey = metaClass.getName() + "." + metaProperty.getName();
            String msgValue = messageTools.getPropertyCaption(metaProperty);
            locMap.put(msgKey, msgValue);
        });
        return locMap;
    }

    public Map<String, String> getLocalizationForAllEntities() {
        Map<String, String> locMap = new TreeMap<>();
        metadata.getSession().getClasses().forEach(metaClass -> locMap.putAll(getStringStringMap(metaClass)));
        return locMap;
    }
}
