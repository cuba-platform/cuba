/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 07.07.2010 7:52:42
 *
 * $Id$
 */
package com.haulmont.cuba.report.loaders;

import com.haulmont.cuba.report.DataSet;
import com.haulmont.cuba.report.Band;
import com.haulmont.cuba.report.EntityMap;
import com.haulmont.cuba.core.entity.Entity;

import java.util.*;

public class MultiEntityDataLoader extends AbstractDbDataLoader {
    public MultiEntityDataLoader(Map<String, Object> params) {
        super(params);
    }

    @Override
    public List<Map<String, Object>> loadData(DataSet dataSet, Band parentBand) {

        String paramName = dataSet.getListEntitiesParamName();
        Object entities;
        if (params.containsKey(paramName))
            entities = params.get(paramName);
        else
            entities = params.get("entities");

        if (entities == null || !(entities instanceof Collection)) {
            throw new IllegalStateException(
                    "Input parameters don't contain 'entities' param or it isn't a collection");
        }
        Collection<Entity> entitiesList = (Collection) entities;
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        for (Entity entity : entitiesList) {
            resultList.add(new EntityMap(entity));
        }
        return resultList;
    }
}
