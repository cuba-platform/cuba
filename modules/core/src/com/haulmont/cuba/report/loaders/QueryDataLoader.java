/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.report.loaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public abstract class QueryDataLoader extends AbstractDbDataLoader {

    protected QueryDataLoader(Map<String, Object> params) {
        super(params);
    }

    protected static class QueryPack {
        private String query;
        private Object[] params;

        protected QueryPack(String query, Object[] params) {
            this.query = query;
            this.params = params;
        }

        public String getQuery() {
            return query;
        }

        public Object[] getParams() {
            return params;
        }
    }

    protected List<Map<String, Object>> fillOutputData(List resList, List<String> parametersNames) {
        List<Map<String, Object>> outputData = new ArrayList<Map<String, Object>>();

        for (Object _resultRecord : resList) {
            Map<String, Object> outputParameters = new HashMap<String, Object>();
            if (_resultRecord instanceof Object[]) {
                Object[] resultRecord = (Object[]) _resultRecord;
                for (Integer i = 0; i < resultRecord.length; i++) {
                    outputParameters.put(parametersNames.get(i), resultRecord[i]);
                }
            } else {
                outputParameters.put(parametersNames.get(0), _resultRecord);
            }
            outputData.add(outputParameters);
        }
        return outputData;
    }
}