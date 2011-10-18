/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 31.05.2010 11:55:39
 *
 * $Id$
 */
package com.haulmont.cuba.report.loaders;

import com.haulmont.cuba.core.global.ScriptingProvider;
import com.haulmont.cuba.report.Band;
import com.haulmont.cuba.report.DataSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
*   Runs groovy script from data set. Script must return List<Map<String, Object>>    
*
*/
public class GroovyDataLoader implements DataLoader {
    private Map<String, Object> params = new HashMap<String, Object>();

    public GroovyDataLoader(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public List<Map<String, Object>> loadData(DataSet dataSet, Band parentBand) {
        String script = dataSet.getText();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dataSet", dataSet);
        params.put("parentBand", parentBand);
        params.put("params", this.params);
        return ScriptingProvider.evaluateGroovy(script, params);
    }
}
