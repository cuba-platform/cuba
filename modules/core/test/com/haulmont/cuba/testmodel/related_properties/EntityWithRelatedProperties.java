/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.testmodel.related_properties;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "test$EntityWithRelatedProperties")
@Table(name = "TEST_ENTITY_WITH_RELATED_PROPERTIES")
@NamePattern("%s|name")
public class EntityWithRelatedProperties extends StandardEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "NOT_RELATED_ATTR")
    private String notRelatedAttr;

    @MetaProperty(related = "name,surname")
    public String getNickName() {
        return name + " MegaCool " + surname;
    }

    @MetaProperty(related = "name,surname")
    public String getSomeAttr() {
        return getNickName() + " additional string";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNotRelatedAttr() {
        return notRelatedAttr;
    }

    public void setNotRelatedAttr(String notRelatedAttr) {
        this.notRelatedAttr = notRelatedAttr;
    }
}
