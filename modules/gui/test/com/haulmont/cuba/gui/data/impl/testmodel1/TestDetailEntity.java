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

package com.haulmont.cuba.gui.data.impl.testmodel1;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import org.apache.commons.lang.ObjectUtils;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "test$DetailEntity")
public class TestDetailEntity extends BaseUuidEntity {

    @Column(name = "NAME")
    private String detailName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MASTER_ID")
    private TestMasterEntity master;

    @Embedded
    private TestEmbeddableEntity embeddable;

    @OneToMany(mappedBy = "detail")
    @Composition
    private Set<TestPartEntity> parts;

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        String o = this.detailName;
        this.detailName = detailName;
        if(!ObjectUtils.equals(o, detailName))
            propertyChanged("detailName", o, detailName);
    }

    public TestMasterEntity getMaster() {
        return master;
    }

    public void setMaster(TestMasterEntity master) {
        TestMasterEntity o = this.master;
        this.master = master;
        if(!ObjectUtils.equals(o, master))
            propertyChanged("master", o, master);
    }

    public TestEmbeddableEntity getEmbeddable() {
        return embeddable;
    }

    public void setEmbeddable(TestEmbeddableEntity embeddable) {
        TestEmbeddableEntity o = this.embeddable;
        this.embeddable = embeddable;
        if(!ObjectUtils.equals(o, embeddable))
            propertyChanged("embeddable", o, embeddable);
    }

    public Set<TestPartEntity> getParts() {
        return parts;
    }

    public void setParts(Set<TestPartEntity> parts) {
        Set<TestPartEntity> o = this.parts;
        this.parts = parts;
        if(!ObjectUtils.equals(o, parts))
            propertyChanged("parts", o, parts);
    }
}