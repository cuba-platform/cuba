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

import javax.persistence.*;

@Table(name = "TEST_DELETE_POLICY_ONE_TO_MANY_FIRST")
@Entity(name = "test$DeletePolicy_OneToMany_First")
public class DeletePolicy_OneToMany_First extends StandardEntity {
    private static final long serialVersionUID = -1562606439091454258L;

    @Column(name = "FIRST_FLD")
    protected String firstFld;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROOT_ID")
    protected DeletePolicy_Root root;

    public void setRoot(DeletePolicy_Root root) {
        this.root = root;
    }

    public DeletePolicy_Root getRoot() {
        return root;
    }


    public void setFirstFld(String firstFld) {
        this.firstFld = firstFld;
    }

    public String getFirstFld() {
        return firstFld;
    }


}