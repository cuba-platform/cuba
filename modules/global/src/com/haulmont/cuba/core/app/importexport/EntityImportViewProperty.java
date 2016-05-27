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

package com.haulmont.cuba.core.app.importexport;

import java.io.Serializable;

public class EntityImportViewProperty implements Serializable {

    protected String name;

    protected EntityImportView view;

    protected ReferenceImportBehaviour referenceImportBehaviour;

    public EntityImportViewProperty(String name) {
        this.name = name;
    }

    public EntityImportViewProperty(String name, EntityImportView view) {
        this.name = name;
        this.view = view;
    }

    public EntityImportViewProperty(String name, ReferenceImportBehaviour referenceImportBehaviour) {
        this.name = name;
        this.referenceImportBehaviour = referenceImportBehaviour;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EntityImportView getView() {
        return view;
    }

    public void setView(EntityImportView view) {
        this.view = view;
    }

    public ReferenceImportBehaviour getReferenceImportBehaviour() {
        return referenceImportBehaviour;
    }

    public void setReferenceImportBehaviour(ReferenceImportBehaviour referenceImportBehaviour) {
        this.referenceImportBehaviour = referenceImportBehaviour;
    }
}