/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 07.09.2010 16:53:23
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components.charts;

import com.haulmont.cuba.gui.components.charts.Chart;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.toolkit.ui.charts.ChartComponent;

public abstract class WebAbstractChart<T extends ChartComponent>
        extends WebAbstractComponent<T>
        implements Chart {

    private static final long serialVersionUID = 7222268861924207093L;

    public boolean getHasLegend() {
        return component.getHasLegend();
    }

    public void setHasLegend(boolean needLegend) {
        component.setHasLegend(needLegend);
    }

    public String getCaption() {
        return component.getCaption();
    }

    public void setCaption(String caption) {
        component.setCaption(caption);
    }

    public String getDescription() {
        return null;
    }

    public void setDescription(String description) {
    }

    @Override
    public void setExpandable(boolean expandable) {
        //ignore
    }

    @Override
    public boolean isExpandable() {
        return false;
    }

    @Override
    public void setWidth(String width) {
        try {
            component.setChartWidth(Integer.parseInt(width));
        } catch (NumberFormatException e) {
            //do nothing
        }
    }

    @Override
    public void setHeight(String height) {
        try {
            component.setChartHeight(Integer.parseInt(height));
        } catch (NumberFormatException e) {
            //do nothing
        }
    }
}
