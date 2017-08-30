/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.restapi.service.filter.testmodel;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

/**
 */
@Entity(name = "test$TestEntity")
@Table(name = "TEST_TEST_ENTITY")
public class TestEntity extends StandardEntity {

    @Column(name = "STRING_FIELD")
    protected String stringField;

    @Column(name = "INT_FIELD")
    protected Integer intField;

    @Column(name = "BOOLEAN_FIELD")
    protected Boolean booleanField;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LINKED_ENTITY_ID")
    protected LinkedTestEntity linkedTestEntity;

    @Column(name = "ENUM_FIELD")
    protected String enumField;

    public String getStringField() {
        return stringField;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }

    public Integer getIntField() {
        return intField;
    }

    public void setIntField(Integer intField) {
        this.intField = intField;
    }

    public Boolean getBooleanField() {
        return booleanField;
    }

    public void setBooleanField(Boolean booleanField) {
        this.booleanField = booleanField;
    }

    public LinkedTestEntity getLinkedTestEntity() {
        return linkedTestEntity;
    }

    public void setLinkedTestEntity(LinkedTestEntity linkedTestEntity) {
        this.linkedTestEntity = linkedTestEntity;
    }

    public TestEnum getEnumField() {
        return TestEnum.fromId(enumField);
    }

    public void setEnumField(TestEnum enumField) {
        this.enumField = enumField == null ? null : enumField.getId();

    }
}
