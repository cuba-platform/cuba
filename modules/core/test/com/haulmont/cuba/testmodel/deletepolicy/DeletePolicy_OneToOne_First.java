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

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;

@NamePattern("%s|firstFld")
@Table(name = "TEST_DELETE_POLICY_ONE_TO_ONE_FIRST")
@Entity(name = "test$DeletePolicyOneToOne_First")
public class DeletePolicy_OneToOne_First extends StandardEntity {
    private static final long serialVersionUID = 6107554609928541090L;

    @Column(name = "FIRST_FLD")
    protected String firstFld;

    @OnDelete(DeletePolicy.UNLINK)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "first")
    protected DeletePolicy_OneToOne_Second second;

    public void setSecond(DeletePolicy_OneToOne_Second second) {
        this.second = second;
    }

    public DeletePolicy_OneToOne_Second getSecond() {
        return second;
    }

    public void setFirstFld(String firstFld) {
        this.firstFld = firstFld;
    }

    public String getFirstFld() {
        return firstFld;
    }
}