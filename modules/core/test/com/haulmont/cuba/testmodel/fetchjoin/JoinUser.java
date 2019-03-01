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

package com.haulmont.cuba.testmodel.fetchjoin;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.haulmont.cuba.core.entity.StandardEntity;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.InheritanceType;
import javax.persistence.DiscriminatorType;
import javax.persistence.Inheritance;
import javax.persistence.DiscriminatorColumn;

@DiscriminatorColumn(name = "DTYPE", discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("USR")
@Table(name = "TEST_JOIN_USER")
@Entity(name = "test$JoinUser")
public class JoinUser extends StandardEntity {
    private static final long serialVersionUID = 8413746332270252589L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TYPE_ID")
    protected JoinType type;

    @Column(name = "NAME")
    protected String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public void setType(JoinType type) {
        this.type = type;
    }

    public JoinType getType() {
        return type;
    }
}