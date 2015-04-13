/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.statistics;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.DoubleDatatype;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import com.haulmont.cuba.web.app.ui.jmxinstance.edit.JmxInstanceEditor;
import com.haulmont.cuba.web.jmx.JmxControlAPI;
import com.haulmont.cuba.web.jmx.JmxControlException;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

/**
 * @author krivenko
 * @version $Id$
 */
public class StatisticsWindow extends AbstractWindow {

    private static final long serialVersionUID = 9164872304110482393L;

    @Inject
    protected GroupTable paramsTable;

    @Inject
    protected GroupDatasource statisticsDs;

    @Inject
    protected JmxControlAPI jmxControlAPI;

    @Inject
    protected Label localJmxField;

    @Inject
    protected LookupPickerField jmxConnectionField;

    protected JmxInstance localJmxInstance;

    @Inject
    protected CollectionDatasource<JmxInstance, UUID> jmxInstancesDs;

    @Inject
    protected Timer valuesTimer;

    @Inject
    protected Metadata metadata;

    private MetaClass parameterClass;

    private int timerDelay = 5000;

    @Override
    public void init(Map<String, Object> params) {
        parameterClass = metadata.getClass(PerformanceParameter.class);

        statisticsDs.addListener(new DsListenerAdapter<PerformanceParameter>() {
            private static final long serialVersionUID = 8971263564912983432L;

            private Formatter kilobyteFormatter = new KilobyteFormatter();
            private DoubleDatatype doubleDatatype = Datatypes.get(DoubleDatatype.NAME);
            @Override
            public void valueChanged(PerformanceParameter source, String property, @Nullable Object prevValue, @Nullable Object value) {
                if ("Uptime".equals(source.getParameterName()) && "currentLongValue".equals(property)) {
                    long uptime = (long)value;
                    //propagate uptime to calculate uptime average
                    for (Datasource ds : getDsContext().getAll()) {
                        if (ds.getMetaClass().equals(parameterClass)) {
                            CollectionDatasource<PerformanceParameter, UUID> statDs = (CollectionDatasource<PerformanceParameter, UUID>) ds;
                            for (PerformanceParameter param : statDs.getItems()) {
                                if (param.getShowUptime()) param.setUptime(uptime);
                            }
                        }
                    }
                }
                else if ("Memory".equals(source.getParameterGroup())) {
                    if ("currentLongValue".equals(property)) {
                        source.setCurrentStringValue(kilobyteFormatter.format(value));
                    }
                    else if ("average1m".equals(property)) {
                        source.setAverage1mStringValue(kilobyteFormatter.format(value));
                    }
                }
                else {
                    if ("currentDoubleValue".equals(property)) {
                        source.setCurrentStringValue(doubleDatatype.format((Double) value));
                    }
                    else if ("average1m".equals(property)) {
                        source.setAverage1mStringValue(doubleDatatype.format((Double) value));
                    }
                }
            }
        });
        initJMXTable();
        setNode(jmxConnectionField.<JmxInstance>getValue());
        valuesTimer.setDelay(timerDelay);
    }

    protected void initJMXTable() {
        localJmxInstance = jmxControlAPI.getLocalInstance();

        jmxInstancesDs.refresh();
        jmxConnectionField.setValue(localJmxInstance);
        jmxConnectionField.setRequired(true);
        jmxConnectionField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                try {
                    setNode(jmxConnectionField.<JmxInstance>getValue());
                }
                catch (JmxControlException e) {
                    JmxInstance jmxInstance = jmxConnectionField.getValue();
                    showNotification(messages.getMessage("com.haulmont.cuba.web.app.ui.jmxcontrol", "unableToConnectToInterface"), NotificationType.WARNING);
                    if (jmxInstance != localJmxInstance)
                        jmxConnectionField.setValue(localJmxInstance);
                }
            }
        });

        jmxConnectionField.removeAllActions();

        jmxConnectionField.addAction(new PickerField.LookupAction(jmxConnectionField) {
            @Override
            public void afterCloseLookup(String actionId) {
                jmxInstancesDs.refresh();
            }
        });

        jmxConnectionField.addAction(new AbstractAction("actions.Add") {
            @Override
            public void actionPerform(Component component) {
                final JmxInstanceEditor instanceEditor = openEditor("sys$JmxInstance.edit", new JmxInstance(), WindowManager.OpenType.DIALOG);
                instanceEditor.addListener(new CloseListener() {
                    @Override
                    public void windowClosed(String actionId) {
                        if (COMMIT_ACTION_ID.equals(actionId)) {
                            jmxInstancesDs.refresh();
                            jmxConnectionField.setValue(instanceEditor.getItem());
                        }
                    }
                });
            }

            @Override
            public String getIcon() {
                return "icons/plus-btn.png";
            }
        });

        localJmxField.setValue(jmxControlAPI.getLocalNodeName());
        localJmxField.setEditable(false);

    }

    public void onRefresh(Timer timer) {
        statisticsDs.refresh();
    }

    protected void setNode(JmxInstance currentNode) {
        statisticsDs.clear();

        int avgInterval = 60 * 1000 / timerDelay;
        Map<String, Object> constantParams = ParamsMap.of("node", currentNode, "avgInterval",avgInterval);
        statisticsDs.refresh(constantParams);
        statisticsDs.groupBy(new Object[]{new MetaPropertyPath(parameterClass, parameterClass.getProperty("parameterGroup"))});
        paramsTable.expandAll();
    }

    public void onMonitorThreads() {
        openWindow("threadsMonitoringWindow", WindowManager.OpenType.NEW_TAB, ParamsMap.of("node", jmxConnectionField.<JmxInstance>getValue()));
    }

    protected static class KilobyteFormatter implements Formatter {
        @Override
        public String format(Object value) {
            String res=null;
            if (value instanceof Long) {
                long longValue = (long)value;
                res = String.format("%d KB",longValue/1024);
            }
            else if (value instanceof Double) {
                double doubleValue = (double)value;
                res = String.format("%.0f KB",doubleValue/1024);
            }
            return res !=null ? res : value.toString();
        }
    }
}
