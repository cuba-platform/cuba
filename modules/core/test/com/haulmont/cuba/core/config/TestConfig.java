/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 12.01.2009 17:46:51
 *
 * $Id$
 */
package com.haulmont.cuba.core.config;

import com.haulmont.cuba.core.config.defaults.Default;
import com.haulmont.cuba.core.config.defaults.DefaultInteger;
import com.haulmont.cuba.core.config.defaults.DefaultInt;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.type.Factory;
import com.haulmont.cuba.core.config.type.UuidTypeFactory;

import java.util.UUID;

@Prefix("cuba.test.")
@Source(type = SourceType.APP)
public interface TestConfig extends Config
{
    String getStringProp();
    void setStringProp(String value);

    @Default("def_value")
    String getStringPropDef();
    void setStringPropDef(String value);

    Integer getIntegerProp();
    void setIntegerProp(Integer value);

    @DefaultInteger(100)
    Integer getIntegerPropDef();
    void setIntegerPropDef(Integer value);

    Integer getIntegerPropDefRuntime(Integer defaultValue);

    @DefaultInt(0)
    int getIntPropDef();
    void setIntPropDef(int value);

    int getIntPropDefRuntime(int defaultValue);
    void setIntPropDefRuntime(int value);

    Boolean getBooleanProp();
    void setBooleanProp(Boolean value);

    @DefaultBoolean(true)
    Boolean getBooleanPropDef();

    @DefaultBoolean(false)
    boolean getBoolProp();
    void setBoolProp(boolean value);

    @Factory(factory = UuidTypeFactory.class)
    UUID getUuidProp();
    void setUuidProp(UUID value);

    @Source(type = SourceType.DATABASE)
    String getDatabaseProp();
    void setDatabaseProp(String value);
}
