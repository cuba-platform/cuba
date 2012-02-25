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

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.report.Band;
import com.haulmont.cuba.report.DataSet;
import com.haulmont.cuba.report.DataSetType;
import com.haulmont.cuba.report.exception.ReportDataLoaderException;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class JpqlDataDataLoader extends QueryDataLoader {
    public JpqlDataDataLoader(Map<String, Object> params) {
        super(params);
    }

    protected QueryPack prepareQuery(String query, Band parentBand) {
        Map<String, Object> currentParams = new HashMap<String, Object>();
        if (params != null) currentParams.putAll(params);

        //adds parameters from parent bands hierarchy
        while (parentBand != null) {
            addParentBandParameters(parentBand, currentParams);
            parentBand = parentBand.getParentBand();
        }

        List<Object> values = new ArrayList<Object>();
        int i = 1;
        for (Map.Entry<String, Object> entry : currentParams.entrySet()) {
            //replaces ${alias} marks with ? and remembers their positions
            String alias = "${" + entry.getKey() + "}";
            String regexp = "\\$\\{" + entry.getKey() + "\\}";
            //todo: another regexp to remove parameter
            String deleteRegexp = "(?i)(and)?(or)? ?[\\w|\\d|\\.|\\_]+ ?(=|>=|<=|like) ?\\$\\{" + entry.getKey() + "\\}";

            if (entry.getValue() == null) {
                query = query.replaceAll(deleteRegexp, "");
            } else if (query.contains(alias)) {
                values.add(entry.getValue());
                query = query.replaceAll(regexp, "?" + i++);
            }
        }

        query = query.trim();
        if (query.endsWith("where")) query = query.replace("where", "");

        return new QueryPack(query, values.toArray());
    }

    protected Query insertParameters(String query, Band parentBand, DataSetType dataSetType) {
        QueryPack pack = prepareQuery(query, parentBand);

        boolean inserted = pack.getParams().length > 0;
        EntityManager em = PersistenceProvider.getEntityManager();
        Query select = DataSetType.SQL.equals(dataSetType) ? em.createNativeQuery(pack.getQuery()) : em.createQuery(pack.getQuery());
        if (inserted) {
            //insert parameters to their position
            int i = 1;
            for (Object value : pack.getParams()) {
                select.setParameter(i++, value instanceof Entity ? ((Entity) value).getId() : value);
            }
        }
        return select;
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
