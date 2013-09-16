/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.config;

import com.haulmont.cuba.core.config.defaults.Default;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.defaults.DefaultInt;
import com.haulmont.cuba.core.config.defaults.DefaultInteger;
import com.haulmont.cuba.core.config.type.Factory;
import com.haulmont.cuba.core.config.type.UuidTypeFactory;
import com.haulmont.cuba.security.entity.User;

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
}
