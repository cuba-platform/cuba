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

@NamePattern("%s|secondFld")
@Table(name = "TEST_DELETE_POLICY_ONE_TO_ONE_SECOND")
@Entity(name = "test$DeletePolicyOneToOne_Second")
public class DeletePolicy_OneToOne_Second extends StandardEntity {
    private static final long serialVersionUID = 6232779177277089522L;

    @Column(name = "SECOND_FLD")
    protected String secondFld;

    @OnDelete(DeletePolicy.UNLINK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIRST_ID")
    protected DeletePolicy_OneToOne_First first;

    public DeletePolicy_OneToOne_First getFirst() {
        return first;
    }

    public void setFirst(DeletePolicy_OneToOne_First first) {
        this.first = first;
    }

    public void setSecondFld(String secondFld) {
        this.secondFld = secondFld;
    }

    public String getSecondFld() {
        return secondFld;
    }
}