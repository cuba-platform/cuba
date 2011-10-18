/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 28.05.2010 10:40:13
 *
 * $Id$
 */
package com.haulmont.cuba.report.loaders;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.report.Band;
import com.haulmont.cuba.report.DataSet;
import com.haulmont.cuba.report.DataSetType;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class SqlDataDataLoader extends AbstractDbDataLoader {

    public SqlDataDataLoader(Map<String, Object> params) {
        super(params);
    }

    @Override
    public List<Map<String, Object>> loadData(DataSet dataSet, Band parentBand) {
        List resList = null;
        List<String> outputParameters = null;

        Transaction tx = PersistenceProvider.createTransaction();
        try {
            String query = dataSet.getText();
            if (StringUtils.isBlank(query)) return Collections.emptyList();

            Query select = insertParameters(query, parentBand, DataSetType.SQL);
            outputParameters = parseQueryOutputParametersNames(query);
            resList = select.getResultList();
            tx.commit();
        } finally {
            tx.end();
        }
        return fillOutputData(resList, outputParameters);
    }
}