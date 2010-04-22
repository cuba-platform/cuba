/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.04.2010 17:06:21
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import java.util.Map;

public interface QueryMacroHandler {

    String expandMacro(String queryString);

    Map<String, Object> getParams();
}
