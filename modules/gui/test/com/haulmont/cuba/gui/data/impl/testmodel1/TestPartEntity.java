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
 *
 */

package com.haulmont.cuba.gui.data.impl.testmodel1;

import com.haulmont.cuba.core.entity.BaseUuidEntity;
import org.apache.commons.lang.ObjectUtils;

import javax.persistence.*;

/**
 *
 */
@Entity(name = "test$PartEntity")
public class TestPartEntity extends BaseUuidEntity {

    @Column(name = "NAME")
    private String partName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DETAIL_ID")
    private TestDetailEntity detail;

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        String o = this.partName;
        this.partName = partName;
        if(!ObjectUtils.equals(o, partName))
            propertyChanged("partName", o, partName);
    }

    public TestDetailEntity getDetail() {
        return detail;
    }

    public void setDetail(TestDetailEntity detail) {
        TestDetailEntity o = this.detail;
        this.detail = detail;
        if(!ObjectUtils.equals(o, detail))
            propertyChanged("detail", o, detail);
    }
}
