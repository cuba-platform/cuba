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

package com.haulmont.cuba.testmodel.cascadedelete;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "test$CascadeEntity")
@Table(name = "TEST_CASCADE_ENTITY")
@NamePattern("%s|name")
public class CascadeEntity extends StandardEntity {

    private static final long serialVersionUID = -5934642550137679651L;

    @Column(name = "NAME")
    protected String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FATHER_ID")
    protected CascadeEntity father;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIRST_CHILD_ID")
    protected CascadeEntity firstChild;

    @OneToMany(mappedBy = "father")
    @OnDelete(DeletePolicy.CASCADE)
    protected Set<CascadeEntity> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CascadeEntity getFather() {
        return father;
    }

    public void setFather(CascadeEntity father) {
        this.father = father;
    }

    public CascadeEntity getFirstChild() {
        return firstChild;
    }

    public void setFirstChild(CascadeEntity firstChild) {
        this.firstChild = firstChild;
    }

    public Set<CascadeEntity> getChildren() {
        return children;
    }

    public void setChildren(Set<CascadeEntity> children) {
        this.children = children;
    }
}
