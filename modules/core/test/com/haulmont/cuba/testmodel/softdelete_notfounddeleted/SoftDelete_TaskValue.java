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

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "TEST_SOFT_DELETE_TASK_VALUE")
@Entity(name = "test$SoftDelete_TaskValue")
public class SoftDelete_TaskValue extends StandardEntity {
    private static final long serialVersionUID = 1751701829040685430L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TASK_ID")
    protected SoftDelete_Task task;

    public SoftDelete_Task getTask() {
        return task;
    }

    public void setTask(SoftDelete_Task task) {
        this.task = task;
    }
}