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

public interface ScriptingMBean {

    String getRootPath();

    String runGroovyScript(String scriptName);
}
