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

package com.haulmont.cuba.core.app.importexport;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.Security;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Map;

/**
 */
@Component(EntityImportViewBuilderAPI.NAME)
public class EntityImportViewBuilder implements EntityImportViewBuilderAPI{

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected Metadata metadata;

    @Inject
    protected Security security;

    @Override
    public EntityImportView buildFromJson(String json, MetaClass metaClass) {
        JsonParser jsonParser = new JsonParser();
        JsonElement rootElement = jsonParser.parse(json);
        if (!rootElement.isJsonObject()) {
            throw new RuntimeException("Passed json is not a JSON object");
        }
        return buildFromJsonObject(rootElement.getAsJsonObject(), metaClass);
    }

    protected EntityImportView buildFromJsonObject(JsonObject jsonObject, MetaClass metaClass) {
        EntityImportView view = new EntityImportView(metaClass.getJavaClass());

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String propertyName = entry.getKey();
            MetaProperty metaProperty = metaClass.getProperty(propertyName);
            if (metaProperty != null) {
                Range propertyRange = metaProperty.getRange();
                Class<?> propertyType = metaProperty.getJavaType();
                if (propertyRange.isDatatype()) {
                    if (security.isEntityAttrUpdatePermitted(metaClass, propertyName))
                        view.addProperty(propertyName);
                } else if (propertyRange.isClass()) {
                    if (Entity.class.isAssignableFrom(propertyType)) {
                        if (metadataTools.isEmbedded(metaProperty)) {
                            MetaClass propertyMetaClass = metadata.getClass(propertyType);
                            JsonElement propertyJsonObject = entry.getValue();
                            if (!propertyJsonObject.isJsonObject()) {
                                throw new RuntimeException("JsonObject was expected for property " + propertyName);
                            }
                            if (security.isEntityAttrUpdatePermitted(metaClass, propertyName)) {
                                EntityImportView propertyImportView = buildFromJsonObject(propertyJsonObject.getAsJsonObject(), propertyMetaClass);
                                view.addProperty(propertyName, propertyImportView);
                            }
                        } else {
                            MetaClass propertyMetaClass = metadata.getClass(propertyType);
                            if (metaProperty.getType() == MetaProperty.Type.COMPOSITION) {
                                JsonElement propertyJsonObject = entry.getValue();
                                if (!propertyJsonObject.isJsonObject()) {
                                    throw new RuntimeException("JsonObject was expected for property " + propertyName);
                                }
                                if (security.isEntityAttrUpdatePermitted(metaClass, propertyName)) {
                                    EntityImportView propertyImportView = buildFromJsonObject(propertyJsonObject.getAsJsonObject(), propertyMetaClass);
                                    view.addProperty(propertyName, propertyImportView);
                                }

                            } else {
                                if (security.isEntityAttrUpdatePermitted(metaClass, propertyName))
                                    view.addProperty(propertyName, ReferenceImportBehaviour.ERROR_ON_MISSING);
                            }
                        }
                    } else if (Collection.class.isAssignableFrom(propertyType)) {
                        MetaClass propertyMetaClass = metaProperty.getRange().asClass();
                        if (metaProperty.getType() == MetaProperty.Type.COMPOSITION) {
                            JsonElement compositionJsonArray = entry.getValue();
                            if (!compositionJsonArray.isJsonArray()) {
                                throw new RuntimeException("JsonArray was expected for property " + propertyName);
                            }
                            EntityImportView propertyImportView = buildFromJsonArray(compositionJsonArray.getAsJsonArray(), propertyMetaClass);
                            if (security.isEntityAttrUpdatePermitted(metaClass, propertyName))
                                view.addProperty(propertyName, propertyImportView);

                        } else {
                            if (security.isEntityAttrUpdatePermitted(metaClass, propertyName))
                                view.addProperty(propertyName, ReferenceImportBehaviour.ERROR_ON_MISSING);
                        }
                    }
                }
            }
        }

        return view;
    }

    /**
     * Builds a EntityImportView that contains properties from all collection members.
     * If the first member contains the property A, and the second one contains a property B then a result view will contain
     * both properties A and B.
     * @param jsonArray a JsonArray
     * @param metaClass a metaClass of entities that are in the jsonArray
     * @return an EntityImportView
     */
    protected EntityImportView buildFromJsonArray(JsonArray jsonArray, MetaClass metaClass) {
        EntityImportView resultView = new EntityImportView(metaClass.getJavaClass());
        for (JsonElement element : jsonArray.getAsJsonArray()) {
            EntityImportView view = buildFromJsonObject(element.getAsJsonObject(), metaClass);
            view.getProperties().stream()
                    .filter(p -> resultView.getProperty(p.getName()) == null)
                    .forEach(p -> {
                        EntityImportViewProperty propertyCopy = new EntityImportViewProperty(p.getName());
                        propertyCopy.setView(p.getView());
                        propertyCopy.setReferenceImportBehaviour(p.getReferenceImportBehaviour());
                        resultView.addProperty(propertyCopy);
                    });
        }
        return resultView;
    }
}
