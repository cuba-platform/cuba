/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 02.09.2010 10:06:44
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui.charts.jfree;

import com.haulmont.cuba.web.toolkit.ui.charts.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.*;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

public class JFreeChartDataProvider implements ChartDataProvider<JFreeChart> {
    private static final long serialVersionUID = -8688971084440222503L;

    private Log log = LogFactory.getLog(JFreeChartDataProvider.class);

    public void handleDataRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            JFreeChart chart
    ) throws ChartException {
        org.jfree.chart.JFreeChart jFreeChart;

        if (chart instanceof PieChart) {
            jFreeChart = createPieChart((JFreePieChart) chart);
        } else if (chart instanceof BarChart) {
            jFreeChart = createBarChart((JFreeBarChart) chart);
        } else if (chart instanceof LineChart) {
            jFreeChart = createLineChart((JFreeLineChart) chart);
        } else {
            log.warn(String.format("This data provider doesn't support a chart type for class: %s",
                    chart.getClass()));
            return;
        }

        try {
            OutputStream out = null;
            try {
                out = response.getOutputStream();

                if (jFreeChart != null) {
                    TextTitle chartTitle = jFreeChart.getTitle();
                    if (chartTitle != null) {
                        chartTitle.setFont(new Font(null, 0, 20));
                    }

                    response.setContentType("image/png");
                    ChartUtilities.writeChartAsPNG(out, jFreeChart, chart.getChartWidth(), chart.getChartHeight());
                }
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        } catch (IOException e) {
            throw new ChartException(e);
        }
    }

    private org.jfree.chart.JFreeChart createPieChart(JFreePieChart chart) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        Iterator it = chart.getColumnPropertyIds().iterator();
        Object valuePropertyId = it.next();

        for (final Object itemId : chart.getRowIds()) {
            dataset.setValue(chart.getRowCaption(itemId), chart.getColumnValue(itemId, valuePropertyId));
        }

        org.jfree.chart.JFreeChart result;
        if (chart.is3D()) {
            result = ChartFactory.createPieChart3D(
                    getChartTitle(chart),
                    dataset,
                    chart.isLegend(),
                    false,
                    false
            );
        } else {
            result = ChartFactory.createPieChart(
                    getChartTitle(chart),
                    dataset,
                    chart.isLegend(),
                    false,
                    false
            );
        }

        PiePlot plot = (PiePlot) result.getPlot();
        plot.setIgnoreNullValues(chart.isIgnoreNullValues());
        plot.setIgnoreZeroValues(chart.isIgnoreZeroValues());
        plot.setOutlineVisible(false);

        return result;
    }

    private org.jfree.chart.JFreeChart createBarChart(JFreeBarChart chart) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (final Object itemId : chart.getRowIds()) {
            for (Object categoryPropertyId : chart.getColumnPropertyIds()) {
                dataset.addValue(
                        chart.getColumnValue(itemId, categoryPropertyId),
                        chart.getRowCaption(itemId),
                        chart.getColumnCaption(categoryPropertyId)
                );
            }
        }

        org.jfree.chart.JFreeChart result;
        if (chart.is3D()) {
            result = ChartFactory.createBarChart3D(
                    getChartTitle(chart),
                    chart.getColumnAxisLabel(),
                    chart.getValueAxisLabel(),
                    dataset,
                    convertChartOrientation(chart.getOrientation()),
                    chart.isLegend(),
                    false,
                    false
            );
        } else {
            result = ChartFactory.createBarChart(
                    getChartTitle(chart),
                    chart.getColumnAxisLabel(),
                    chart.getValueAxisLabel(),
                    dataset,
                    convertChartOrientation(chart.getOrientation()),
                    chart.isLegend(),
                    false,
                    false
            );
        }

        return result;
    }

    private org.jfree.chart.JFreeChart createLineChart(JFreeLineChart chart) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (final Object itemId : chart.getRowIds()) {
            for (Object categoryPropertyId : chart.getColumnPropertyIds()) {
                dataset.addValue(
                        chart.getColumnValue(itemId, categoryPropertyId),
                        chart.getRowCaption(itemId),
                        chart.getColumnCaption(categoryPropertyId)
                );
            }
        }

        org.jfree.chart.JFreeChart result = ChartFactory.createLineChart(
                getChartTitle(chart),
                chart.getColumnAxisLabel(),
                chart.getValueAxisLabel(),
                dataset,
                convertChartOrientation(chart.getOrientation()),
                chart.isLegend(),
                false,
                false
        );

        return result;
    }

    private String getChartTitle(JFreeChart chart) {
        return chart.getCaption() == null ? "" : chart.getCaption();
    }

    private static PlotOrientation convertChartOrientation(Chart.Orientation orientation) {
        return orientation == Chart.Orientation.VERTICAL
                ? PlotOrientation.VERTICAL : PlotOrientation.HORIZONTAL;
    }
}
