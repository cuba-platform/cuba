/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 25.06.2010 17:24:27
 *
 * $Id$
 */
package com.haulmont.cuba.report.loaders;

import com.haulmont.cuba.report.Band;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractDbDataLoader implements DataLoader {
    private static final String QUERY_END = "%%END%%";
    private static final String OUTPUT_PARAMS_PATTERN = "as ([\\w|\\d|_]+\\b)[\\s]*[,|from|" + QUERY_END + "]";

    protected Map<String, Object> params = new HashMap<String, Object>();

    protected AbstractDbDataLoader(Map<String, Object> params) {
        this.params = params;
    }

    protected List<String> parseQueryOutputParametersNames(String query) {
        ArrayList<String> result = new ArrayList<String>();
        if (!query.endsWith(";"))
            query += QUERY_END;
        else
            query = query.substring(0, query.length() - 1) + QUERY_END;
        Pattern namePattern = Pattern.compile(OUTPUT_PARAMS_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = namePattern.matcher(query);

        while (matcher.find()) {
            String group = matcher.group(matcher.groupCount());
            if (group != null)
                result.add(group.trim());
        }
        return result;
    }

    protected void addParentBandParameters(Band parentBand, Map<String, Object> currentParams) {
        if (parentBand != null) {
            String parentBandName = parentBand.getName();

            for (Map.Entry<String, Object> entry : parentBand.getData().entrySet()) {
                currentParams.put(parentBandName + "." + entry.getKey(), entry.getValue());
            }
        }
    }
}