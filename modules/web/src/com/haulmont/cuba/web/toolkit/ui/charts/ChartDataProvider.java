/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 02.09.2010 10:05:34
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui.charts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

public interface ChartDataProvider<T extends Chart> extends Serializable {

    void handleDataRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            T chart
    ) throws ChartException;
}
