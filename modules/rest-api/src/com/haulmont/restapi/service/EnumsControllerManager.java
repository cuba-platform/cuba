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

import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.restapi.data.EnumInfo;
import com.haulmont.restapi.data.EnumValueInfo;
import com.haulmont.restapi.exception.RestAPIException;
import org.springframework.http.HttpStatus;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class EnumsControllerManager {
    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected Messages messages;

    public List<EnumInfo> getAllEnumInfos() {
        List<EnumInfo> results = new ArrayList<>();

        metadataTools.getAllEnums().stream()
                .filter(enumClass -> EnumClass.class.isAssignableFrom(enumClass) && enumClass.isEnum())
                .forEach(enumClass -> {
                    List<EnumValueInfo> enumValues = new ArrayList<>();
                    Object[] enumConstants = enumClass.getEnumConstants();
                    for (Object enumConstant : enumConstants) {
                        Enum enumValue = (Enum) enumConstant;
                        EnumValueInfo enumValueInfo = new EnumValueInfo(enumValue.name(), ((EnumClass) enumValue).getId(), messages.getMessage(enumValue));
                        enumValues.add(enumValueInfo);
                    }
                    results.add(new EnumInfo(enumClass.getName(), enumValues));
                });

        return results;
    }

    public EnumInfo getEnumInfo(String enumClassName) {
        Class<?> enumClass;
        try {
            enumClass = Class.forName(enumClassName);
        } catch (ClassNotFoundException e) {
            throw new RestAPIException("Enum not found", "Enum with class name " + enumClassName + " not found", HttpStatus.NOT_FOUND);
        }
        List<EnumValueInfo> enumValues = new ArrayList<>();
        Object[] enumConstants = enumClass.getEnumConstants();
        for (Object enumConstant : enumConstants) {
            Enum enumValue = (Enum) enumConstant;
            EnumValueInfo enumValueInfo = new EnumValueInfo(enumValue.name(), ((EnumClass) enumValue).getId(), messages.getMessage(enumValue));
            enumValues.add(enumValueInfo);
        }
        return new EnumInfo(enumClass.getName(), enumValues);
    }
}
