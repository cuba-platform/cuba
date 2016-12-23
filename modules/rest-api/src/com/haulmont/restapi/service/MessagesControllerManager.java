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
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.restapi.common.RestControllerUtils;
import com.haulmont.restapi.controllers.MessagesController;
import com.haulmont.restapi.exception.RestAPIException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.*;

/**
 * Class that executes business logic required by the {@link MessagesController}.
 */
@Component("cuba_LocalizationControllerManager")
public class MessagesControllerManager {

    @Inject
    protected RestControllerUtils restControllerUtils;

    @Inject
    protected MessageTools messageTools;

    @Inject
    protected Metadata metadata;

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected Messages messages;

    public Map<String, String> getLocalizationForEntity(String entityName) {
        MetaClass metaClass = restControllerUtils.getMetaClass(entityName);
        return getLocalizationForEntity(metaClass);
    }

    public Map<String, String> getLocalizationForAllEntities() {
        Map<String, String> locMap = new TreeMap<>();
        metadata.getSession().getClasses().forEach(metaClass -> locMap.putAll(getLocalizationForEntity(metaClass)));
        return locMap;
    }

    protected Map<String, String> getLocalizationForEntity(MetaClass metaClass) {
        Map<String, String> locMap = new TreeMap<>();
        locMap.put(metaClass.getName(), messageTools.getEntityCaption(metaClass));
        metaClass.getProperties().forEach(metaProperty -> {
            String msgKey = metaClass.getName() + "." + metaProperty.getName();
            String msgValue = messageTools.getPropertyCaption(metaProperty);
            locMap.put(msgKey, msgValue);
        });
        return locMap;
    }

    public Map<String, String> getLocalizationForAllEnums() {
        Map<String, String> locMap = new TreeMap<>();
        metadataTools.getAllEnums().forEach(enumClass -> locMap.putAll(getLocalizationForEnum(enumClass)));
        return locMap;
    }

    public Map<String, String> getLocalizationForEnum(String enumClassName) {
        Class<?> enumClass;
        try {
            enumClass = Class.forName(enumClassName);
        } catch (ClassNotFoundException e) {
            throw new RestAPIException("Enum not found", "Enum with class name " + enumClassName + " not found", HttpStatus.NOT_FOUND);
        }
        return getLocalizationForEnum(enumClass);
    }

    public Map<String, String> getLocalizationForEnum(Class enumClass) {
        Map<String, String> locMap = new TreeMap<>();
        locMap.put(enumClass.getName(), messages.getMessage(enumClass, enumClass.getSimpleName()));
        Object[] enumConstants = enumClass.getEnumConstants();
        for (Object enumConstant : enumConstants) {
            Enum enumValue = (Enum) enumConstant;
            String msgKey = enumClass.getName() + "." + enumValue.name();
            String msgValue = messages.getMessage(enumValue);
            locMap.put(msgKey, msgValue);
        }
        return locMap;
    }
}
