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
package com.haulmont.cuba.core.config;

import com.haulmont.cuba.core.config.defaults.Default;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.defaults.DefaultInt;
import com.haulmont.cuba.core.config.defaults.DefaultInteger;
import com.haulmont.cuba.core.config.type.*;
import com.haulmont.cuba.security.entity.RoleType;
import com.haulmont.cuba.security.entity.User;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Source(type = SourceType.APP)
public interface TestConfig extends Config
{
    @Property("cuba.test.stringProp")
    String getStringProp();
    void setStringProp(String value);

    @Property("cuba.test.stringPropDef")
    @Default("def_value")
    String getStringPropDef();
    void setStringPropDef(String value);

    @Property("cuba.test.integerProp")
    Integer getIntegerProp();
    void setIntegerProp(Integer value);

    @Property("cuba.test.integerPropDef")
    @DefaultInteger(100)
    Integer getIntegerPropDef();
    void setIntegerPropDef(Integer value);

    @Property("cuba.test.integerPropDefRuntime")
    Integer getIntegerPropDefRuntime(Integer defaultValue);

    @Property("cuba.test.intPropDef")
    @DefaultInt(0)
    int getIntPropDef();
    void setIntPropDef(int value);

    @Property("cuba.test.intPropDefRuntime")
    int getIntPropDefRuntime(int defaultValue);
    void setIntPropDefRuntime(int value);

    @Property("cuba.test.booleanProp")
    Boolean getBooleanProp();
    void setBooleanProp(Boolean value);

    @Property("cuba.test.booleanPropDef")
    @DefaultBoolean(true)
    Boolean getBooleanPropDef();

    @Property("cuba.test.boolProp")
    @DefaultBoolean(false)
    boolean getBoolProp();
    void setBoolProp(boolean value);

    @Property("cuba.test.uuidProp")
    @Factory(factory = UuidTypeFactory.class)
    UUID getUuidProp();
    void setUuidProp(UUID value);

    @Property("cuba.test.databaseProp")
    @Source(type = SourceType.DATABASE)
    String getDatabaseProp();
    void setDatabaseProp(String value);

    @Property("cuba.test.adminUser")
    @Default("sec$User-60885987-1b61-4247-94c7-dff348347f93")
    User getAdminUser();

    @Property("cuba.test.roleTypeProp")
    @Default("STANDARD")
    RoleType getRoleTypeProp();

    @Property("cuba.test.dateProp")
    @Default("2013-12-12 00:00:00.000")
    @Factory(factory = DateFactory.class)
    Date getDateProp();

    @Property("cuba.test.integerListProp")
    @Default("1 2 3")
    @Factory(factory = IntegerListTypeFactory.class)
    List<Integer> getIntegerListProp();

    @Property("cuba.test.stringListProp")
    @Default("aaa|bbb|ccc")
    @Factory(factory = StringListTypeFactory.class)
    List<String> getStringListProp();

    @Property("cuba.test.stringNotFoundGetterProp")
    String getStringNotFoundGetterProp();
    void setStringNotFoundGetProp(String value);
}
