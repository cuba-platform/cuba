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

import javax.persistence.*;
import java.util.List;

@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
@Table(name = "TEST_JOIN_CLASS_TYPE")
@DiscriminatorValue("TST_CLS")
@Entity(name = "test$JoinClassType")
public class JoinClassType extends BaseJoinType {
    private static final long serialVersionUID = -4326065998006020710L;

    @OneToMany(mappedBy = "classType")
    protected List<JoinType> types;

    public void setTypes(List<JoinType> types) {
        this.types = types;
    }

    public List<JoinType> getTypes() {
        return types;
    }
}