/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 15:17:56
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout;

import com.haulmont.cuba.gui.components.charts.Chart;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Timer;

public interface ComponentsFactory {

    String NAME = "cuba_ComponentsFactory";

    <T extends Component> T createComponent(String name);

    <T extends Timer> T createTimer();

    <T extends Chart> T createChart(String vendor, String name);
}
