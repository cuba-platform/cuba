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

package com.haulmont.cuba.testmodel.fetchjoin;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Entity(name = "test$JoinE")
@Table(name = "TEST_JOIN_E")
public class JoinE extends StandardEntity {
    private static final long serialVersionUID = -3466111524910701424L;

    @Column(name = "NAME")
    protected String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_ID")
    protected JoinF f;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JoinF getF() {
        return f;
    }

    public void setF(JoinF f) {
        this.f = f;
    }
}
