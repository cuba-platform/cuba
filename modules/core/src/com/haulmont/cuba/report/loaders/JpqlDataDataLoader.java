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

import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.report.Band;
import com.haulmont.cuba.report.DataSet;
import com.haulmont.cuba.report.DataSetType;
import com.haulmont.cuba.report.exception.ReportDataLoaderException;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JpqlDataDataLoader extends QueryDataLoader {
    public JpqlDataDataLoader(Map<String, Object> params) {
        super(params);
    }

    @Override
    public List<Map<String, Object>> loadData(DataSet dataSet, Band parentBand) {
        List<String> outputParameters = null;
        List queryResult = null;
        Transaction tx = PersistenceProvider.createTransaction();
        try {
            String query = dataSet.getText();
            if (StringUtils.isBlank(query)) return Collections.emptyList();

            outputParameters = parseQueryOutputParametersNames(query);

            query = query.replaceAll("(?i)as [\\w|\\d|_|\\s]+,", ",");//replaces [as alias_name] entries except last
            query = query.replaceAll("(?i)as [\\w|\\d|_]+ *", " ");//replaces last [as alias_name] entry

            Query select = insertParameters(query, parentBand, DataSetType.JPQL);
            queryResult = select.getResultList();
            tx.commit();
        } catch (Exception e) {
            throw new ReportDataLoaderException(e);
        } finally {
            tx.end();
        }
        return fillOutputData(queryResult, outputParameters);
    }
}
