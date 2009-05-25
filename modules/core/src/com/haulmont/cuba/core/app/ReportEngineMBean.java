/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.05.2009 18:28:47
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

public interface ReportEngineMBean
{
    String OBJECT_NAME = "haulmont.cuba:service=ReportEngine";

    void create();

    ReportEngineAPI getAPI();
}
