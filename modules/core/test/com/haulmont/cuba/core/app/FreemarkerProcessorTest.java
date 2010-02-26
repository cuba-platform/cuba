/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 26.02.2010 13:56:32
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.CubaTestCase;
import com.haulmont.cuba.core.Locator;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FreemarkerProcessorTest extends CubaTestCase {

    public void test() {
        ReportEngineAPI reportEngine = Locator.lookup(ReportEngineAPI.NAME);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("number", "111");
        params.put("date", new Date().toString());
        String result = reportEngine.processFreemarkerTemplate("cuba/report/contract1.rtf", params);
        
    }
}
