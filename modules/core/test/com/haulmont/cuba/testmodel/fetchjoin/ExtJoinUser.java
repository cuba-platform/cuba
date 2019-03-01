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

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
@DiscriminatorValue("EXT_USR")
@Table(name = "TEST_EXT_JOIN_USER")
@Entity(name = "test$ExtJoinUser")
public class ExtJoinUser extends JoinUser {
    private static final long serialVersionUID = 6262951748893853414L;
}