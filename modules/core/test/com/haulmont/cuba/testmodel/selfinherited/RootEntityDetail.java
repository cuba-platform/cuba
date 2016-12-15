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

package com.haulmont.cuba.testmodel.selfinherited;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "TEST_ROOT_ENTITY_DETAIL")
@Entity(name = "test$RootEntityDetail")
public class RootEntityDetail extends StandardEntity {

    private static final long serialVersionUID = -3794606860555343620L;

    @Column(name = "INFO")
    protected String info;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MASTER_ID")
    protected RootEntity master;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public RootEntity getMaster() {
        return master;
    }

    public void setMaster(RootEntity master) {
        this.master = master;
    }
}
