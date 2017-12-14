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

package com.haulmont.cuba.testmodel.softdelete_notfounddeleted;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@NamePattern("%s|name")
@Table(name = "TEST_SOFT_DELETE_PROJECT")
@Entity(name = "test$SoftDelete_Project")
public class SoftDelete_Project extends StandardEntity {
    private static final long serialVersionUID = -1229075630102208231L;

    @Column(name = "NAME")
    protected String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "A_VALUE_ID")
    protected SoftDelete_TaskValue aValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TASK_ID")
    protected SoftDelete_Task task;

    public SoftDelete_TaskValue getAValue() {
        return aValue;
    }

    public void setAValue(SoftDelete_TaskValue aValue) {
        this.aValue = aValue;
    }


    public SoftDelete_Task getTask() {
        return task;
    }

    public void setTask(SoftDelete_Task task) {
        this.task = task;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}