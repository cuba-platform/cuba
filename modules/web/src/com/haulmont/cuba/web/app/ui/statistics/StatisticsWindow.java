/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.statistics;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.web.app.ui.jmxinstance.edit.JmxInstanceEditor;
import com.haulmont.cuba.web.jmx.JmxControlAPI;
import com.haulmont.cuba.web.jmx.JmxControlException;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

/**
 * @author krivenko
 * @version $Id$
 */
public class StatisticsWindow extends AbstractWindow {

    @Inject
    protected GroupTable paramsTable;

    @Inject
    protected GroupDatasource<PerformanceParameter, UUID> statisticsDs;

    @Inject
    protected JmxControlAPI jmxControlAPI;

    @Inject
    protected Label localNodeLab;

    @Inject
    protected LookupPickerField jmxConnectionField;

    protected JmxInstance localJmxInstance;

    @Inject
    protected CollectionDatasource<JmxInstance, UUID> jmxInstancesDs;

    @Inject
    protected Timer valuesTimer;

    @Inject
    protected Metadata metadata;

    protected MetaClass parameterClass;

    protected int timerDelay = 5000;
    protected long startTime = System.currentTimeMillis();

    @Override
    public void init(Map<String, Object> params) {
        parameterClass = metadata.getClassNN(PerformanceParameter.class);
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
                } catch (JmxControlException e) {
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

        localNodeLab.setValue(jmxControlAPI.getLocalNodeName());
        localNodeLab.setEditable(false);

    }

    @SuppressWarnings("unused")
    public void onRefresh(Timer timer) {
        statisticsDs.refresh();

        StatisticsDatasource.DurationFormatter formatter = new StatisticsDatasource.DurationFormatter();
        String dur = formatter.format((double) (System.currentTimeMillis() - startTime));
        paramsTable.setColumnCaption("recentStringValue", formatMessage("recentAverage", dur));
    }

    protected void setNode(JmxInstance currentNode) {
        statisticsDs.clear();
        startTime = System.currentTimeMillis();

        Map<String, Object> constantParams = ParamsMap.of("node", currentNode, "refreshPeriod", timerDelay);
        statisticsDs.refresh(constantParams);
        statisticsDs.groupBy(new Object[]{new MetaPropertyPath(parameterClass, parameterClass.getPropertyNN("parameterGroup"))});
        paramsTable.expandAll();
    }

    public void onMonitorThreads() {
        openWindow("threadsMonitoringWindow", WindowManager.OpenType.NEW_TAB,
                ParamsMap.of("node", jmxConnectionField.<JmxInstance>getValue()));
    }
}
