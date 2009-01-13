/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 12.01.2009 17:51:26
 *
 * $Id$
 */
package com.haulmont.cuba.core.config;

@Prefix("cuba.test.")
@Source(type = SourceType.SYSTEM)
public interface TestPrefixedConfig extends Config
{
    String getStringProp();
    void setStringProp(String value);
}
