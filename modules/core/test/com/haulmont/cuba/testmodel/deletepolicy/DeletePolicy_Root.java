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

package com.haulmont.cuba.testmodel.deletepolicy;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.List;

@Table(name = "TEST_DELETE_POLICY_ROOT")
@Entity(name = "test$DeletePolicy_Root")
public class DeletePolicy_Root extends StandardEntity {
    private static final long serialVersionUID = 1516955237934477402L;

    @Column(name = "ROOT_FLD")
    protected String rootFld;


    @OnDelete(DeletePolicy.UNLINK)
    @OneToMany(mappedBy = "root")
    protected List<DeletePolicy_OneToMany_First> onetomany;

    @OnDelete(DeletePolicy.UNLINK)
    @JoinTable(name = "TEST_DELETE_POLICY_ROOT_DELETE_POLICY_MANY_TO_MANY_FIRST_LINK",
        joinColumns = @JoinColumn(name = "DELETE_POLICY__ROOT_ID"),
        inverseJoinColumns = @JoinColumn(name = "DELETE_POLICY__MANY_TO_MANY__FIRST_ID"))
    @ManyToMany
    protected List<DeletePolicy_ManyToMany_First> manytomany;

    public void setOnetomany(List<DeletePolicy_OneToMany_First> onetomany) {
        this.onetomany = onetomany;
    }

    public List<DeletePolicy_OneToMany_First> getOnetomany() {
        return onetomany;
    }


    public void setManytomany(List<DeletePolicy_ManyToMany_First> manytomany) {
        this.manytomany = manytomany;
    }

    public List<DeletePolicy_ManyToMany_First> getManytomany() {
        return manytomany;
    }


    public void setRootFld(String rootFld) {
        this.rootFld = rootFld;
    }

    public String getRootFld() {
        return rootFld;
    }


}