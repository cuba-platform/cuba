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

package com.haulmont.cuba.testmodel.beanvalidation;

import com.haulmont.cuba.core.entity.StandardEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity(name = "test$ValidatedEntity")
@Table(name = "TEST_VALIDATED_ENTITY")
public class ValidatedEntity extends StandardEntity {
    @Length(min = 5, max = 2147483647)
    @NotNull
    @Column(name = "NAME")
    protected String name;

    @Embedded
    @Valid
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "EE_NAME"))
    })
    protected EmbeddedValidatedEntity embeddedValidatedEntity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EmbeddedValidatedEntity getEmbeddedValidatedEntity() {
        return embeddedValidatedEntity;
    }

    public void setEmbeddedValidatedEntity(EmbeddedValidatedEntity embeddedValidatedEntity) {
        this.embeddedValidatedEntity = embeddedValidatedEntity;
    }
}
