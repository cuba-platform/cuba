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

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.bali.db.ResultSetHandler;
import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.sys.persistence.DbTypeConverter;
import com.haulmont.cuba.core.sys.persistence.DbmsType;
import com.haulmont.cuba.report.Band;
import com.haulmont.cuba.report.DataSet;
import com.haulmont.cuba.report.exception.ReportDataLoaderException;
import org.apache.commons.lang.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SqlDataDataLoader extends QueryDataLoader {

    public SqlDataDataLoader(Map<String, Object> params) {
        super(params);
    }

    @Override
    public List<Map<String, Object>> loadData(DataSet dataSet, Band parentBand) {
        List resList;
        List<String> outputParameters;

        String query = dataSet.getText();
        if (StringUtils.isBlank(query)) return Collections.emptyList();

        QueryPack pack = prepareQuery(query, parentBand);

        QueryRunner runner = new QueryRunner(Locator.getDataSource());
        try {
            resList = runner.query(pack.getQuery(), pack.getParams(), new ResultSetHandler<List>() {
                @Override
                public List handle(ResultSet rs) throws SQLException {
                    List<Object[]> resList = new ArrayList<Object[]>();
                    DbTypeConverter typeConverter = DbmsType.getCurrent().getTypeConverter();

                    while (rs.next()) {
                        Object[] values = new Object[rs.getMetaData().getColumnCount()];
                        for (int columnIndex = 0; columnIndex < rs.getMetaData().getColumnCount(); columnIndex++) {
                            values[columnIndex] = typeConverter.getJavaObject(rs, columnIndex + 1);
                        }
                        resList.add(values);
                    }

                    return resList;
                }
            });
        } catch (SQLException e) {
            throw new ReportDataLoaderException(e);
        }

        outputParameters = parseQueryOutputParametersNames(query);
        return fillOutputData(resList, outputParameters);
    }
}