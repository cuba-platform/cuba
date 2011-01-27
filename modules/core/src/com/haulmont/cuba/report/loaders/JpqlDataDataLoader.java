/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 25.06.2010 16:36:21
 *
 * $Id$
 */
package com.haulmont.cuba.report.loaders;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.report.Band;
import com.haulmont.cuba.report.DataSet;
import com.haulmont.cuba.report.DataSetType;

import java.util.*;

import org.apache.commons.lang.StringUtils;

public class JpqlDataDataLoader extends AbstractDbDataLoader {
    public JpqlDataDataLoader(Map<String, Object> params) {
        super(params);
    }

    public List<Map<String, Object>> loadData(DataSet dataSet, Band parentBand) {
        List<String> outputParameters = null;
        List queryResult = null;
        Transaction tx = Locator.createTransaction();
        try {
            String query = dataSet.getText();
            if (StringUtils.isBlank(query)) return Collections.emptyList();

            outputParameters = parseQueryOutputParametersNames(query);

            query = query.replaceAll("(?i)as [\\w|\\d|_|\\s]+,", ",");//replaces [as alias_name] entries except last
            query = query.replaceAll("(?i)as [\\w|\\d|_]+ *", " ");//replaces last [as alias_name] entry

            Query select = insertParameters(query, parentBand, DataSetType.JPQL);
            queryResult = select.getResultList();
            tx.commit();
        } finally {
            tx.end();
        }
        return fillOutputData(queryResult, outputParameters);
    }
}
