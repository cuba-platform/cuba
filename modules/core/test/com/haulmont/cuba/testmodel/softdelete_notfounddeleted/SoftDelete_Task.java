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

@NamePattern("%s|message")
@Table(name = "TEST_SOFT_DELETE_TASK")
@Entity(name = "test$SoftDelete_Task")
public class SoftDelete_Task extends StandardEntity {
    private static final long serialVersionUID = 1528766577320321656L;

    @Column(name = "MESSAGE")
    protected String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICE_ID")
    protected SoftDelete_Service service;

    public SoftDelete_Service getService() {
        return service;
    }

    public void setService(SoftDelete_Service service) {
        this.service = service;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}