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

package com.haulmont.cuba.testmodel.multiplelinks;

import com.haulmont.cuba.core.entity.BaseLongIdEntity;

import javax.persistence.*;

@Table(name = "TEST_MULTI_LINK_ENTITY")
@Entity(name = "test$MultiLinkEntity")
public class MultiLinkEntity extends BaseLongIdEntity {
    private static final long serialVersionUID = -826856596780392051L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "A_ID")
    protected LinkEntity a;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "B_ID")
    protected LinkEntity b;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "C_ID")
    protected LinkEntity c;

    public LinkEntity getA() {
        return a;
    }

    public void setA(LinkEntity a) {
        this.a = a;
    }

    public LinkEntity getB() {
        return b;
    }

    public void setB(LinkEntity b) {
        this.b = b;
    }

    public LinkEntity getC() {
        return c;
    }

    public void setC(LinkEntity c) {
        this.c = c;
    }
}
