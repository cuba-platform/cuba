/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 29.09.2010 11:59:02
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;

public interface ScriptingManagerMBean {

    String getRootPath();

    @ManagedOperationParameters({@ManagedOperationParameter(name = "scriptName", description = "")})
    String runGroovyScript(String scriptName);
}
