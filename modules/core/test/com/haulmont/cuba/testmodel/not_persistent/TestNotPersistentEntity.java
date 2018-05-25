/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.testmodel.not_persistent;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.ReferenceToEntity;

import javax.persistence.Column;
import javax.persistence.Embedded;

@MetaClass(name = "sys$TestNotPersistentEntity")
public class TestNotPersistentEntity extends BaseUuidEntity {

    private static final long serialVersionUID = 3129839116133901601L;

    @MetaProperty
    private String name;

    // JPA annotations are used intentionally

    @Column(name = "info")
    private String info;

    @Embedded
    private ReferenceToEntity embeddedRef;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public ReferenceToEntity getEmbeddedRef() {
        return embeddedRef;
    }

    public void setEmbeddedRef(ReferenceToEntity embeddedRef) {
        this.embeddedRef = embeddedRef;
    }
}
