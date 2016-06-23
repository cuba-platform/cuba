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

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.importexport.EntityImportView;

/**
 * Class that is used for building an {@link EntityImportView} based on the JSON object that represents an entity.
 */
public interface EntityImportViewBuilderAPI {

    String NAME = "cuba_EntityImportViewBuilderAPI";

    /**
     * Builds an {@link EntityImportView} that contains all fields that are presented in the JSON object.
     * <p>
     * All references will be added to the view as a {@link com.haulmont.cuba.core.app.importexport.ReferenceImportBehaviour#ERROR_ON_MISSING}
     * behavior. All references that have a @Composition annotation will be added to the view with a property that has a
     * {@link com.haulmont.cuba.core.app.importexport.EntityImportViewProperty}. This means that compositions will be
     * persisted during the import.
     *
     * @param json      a string that represents a JSON object
     * @param metaClass a MetaClass of the entity
     * @return an EntityImportView
     */
    EntityImportView buildFromJson(String json, MetaClass metaClass);
}
