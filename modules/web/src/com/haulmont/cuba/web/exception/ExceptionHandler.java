/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 20.05.2009 18:22:40
 *
 * $Id$
 */
package com.haulmont.cuba.web.exception;

import com.vaadin.terminal.Terminal;
import com.haulmont.cuba.web.App;

public interface ExceptionHandler
{
    boolean handle(Terminal.ErrorEvent event, App app);
}
