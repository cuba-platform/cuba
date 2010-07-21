/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 28.05.2010 17:11:57
 *
 * $Id$
 */
package com.haulmont.cuba.report.loaders;

import com.haulmont.cuba.report.DataSet;
import com.haulmont.cuba.report.Band;

import java.util.List;
import java.util.Map;

public interface DataLoader {
    List<Map<String, Object>> loadData(DataSet dataSet, Band parentBand);
}
