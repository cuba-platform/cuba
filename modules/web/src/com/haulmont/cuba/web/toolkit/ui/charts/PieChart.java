/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 07.09.2010 16:48:13
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui.charts;

public interface PieChart extends Chart, Chart.ViewIn3D {
    boolean isIgnoreZeroValues();
    void setIgnoreZeroValues(boolean ignoreZeroValues);

    boolean isIgnoreNullValues();
    void setIgnoreNullValues(boolean ignoreNullValues);
}
