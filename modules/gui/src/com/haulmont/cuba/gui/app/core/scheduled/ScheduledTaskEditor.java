/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.scheduled;

import com.haulmont.cuba.core.app.SchedulingService;
import com.haulmont.cuba.core.app.scheduled.MethodInfo;
import com.haulmont.cuba.core.app.scheduled.MethodParameterInfo;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.ScheduledTask;
import com.haulmont.cuba.core.entity.ScheduledTaskDefinedBy;
import com.haulmont.cuba.core.entity.SchedulingType;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ScheduledTaskEditor extends AbstractEditor<ScheduledTask> {

    @Inject
    protected LookupField beanNameField;

    @Inject
    protected LookupField methodNameField;

    @Inject
    protected LookupField userNameField;

    @Inject
    protected OptionsGroup definedByField;

    @Inject
    protected TextField classNameField;

    @Inject
    protected TextField scriptNameField;

    @Inject
    protected Label beanNameLabel;

    @Inject
    protected Label methodNameLabel;

    @Inject
    protected Label classNameLabel;

    @Inject
    protected Label scriptNameLabel;

    @Inject
    protected Container methodParamsBox;

    @Inject
    protected Datasource<ScheduledTask> taskDs;

    @Inject
    protected SchedulingService service;

    @Inject
    protected ComponentsFactory componentsFactory;

    @Inject
    protected OptionsGroup schedulingTypeField;

    @Inject
    protected TextField cronField;

    @Inject
    protected TextField periodField;

    @Inject
    protected DateField startDateField;

    @Inject
    protected Label cronLabel;

    @Inject
    protected Label periodLabel;

    @Inject
    protected Label startDateLabel;

    @Inject
    protected ThemeConstants themeConstants;

    @Inject
    protected LinkButton cronHelpButton;

    @Inject
    protected BoxLayout cronHbox;

    @Inject
    protected BoxLayout methodNameHbox;

    //List holds an information about methods of selected bean
    protected List<MethodInfo> availableMethods = new ArrayList<>();

    protected void show(Component... components) {
        for (Component component : components) {
            component.setVisible(true);
        }
    }

    protected void hide(Component... components) {
        for (Component component : components) {
            component.setVisible(false);
        }
    }

    protected void hideAll() {
        hide(classNameField, classNameLabel, scriptNameField, scriptNameLabel, beanNameField, beanNameLabel,
                methodNameField, methodNameLabel, methodNameHbox, methodParamsBox);
    }

    protected void clear(Field... fields) {
        for (Field component : fields) {
            component.setValue(null);
        }
    }

    @Override
    public void init(Map<String, Object> params) {
        schedulingTypeField.setOptionsList(Arrays.asList(SchedulingType.values()));
        schedulingTypeField.addValueChangeListener(e -> setSchedulingTypeField((SchedulingType) e.getValue()));

        definedByField.setOptionsList(Arrays.asList(ScheduledTaskDefinedBy.values()));
        definedByField.addValueChangeListener(e -> {
            if (ScheduledTaskDefinedBy.BEAN == e.getValue()) {
                clear(classNameField, scriptNameField);
                hideAll();
                show(beanNameField, beanNameLabel, methodNameField, methodNameLabel, methodNameHbox, methodParamsBox);
            } else if (ScheduledTaskDefinedBy.CLASS == e.getValue()) {
                clear(beanNameField, methodNameField, scriptNameField);
                hideAll();
                show(classNameField, classNameLabel);
            } else if (ScheduledTaskDefinedBy.SCRIPT == e.getValue()) {
                clear(beanNameField, methodNameField, classNameField);
                hideAll();
                show(scriptNameField, scriptNameLabel);
            } else {
                clear(beanNameField, methodNameField, classNameField, scriptNameField);
                hideAll();
            }
        });

        Map<String, List<MethodInfo>> availableBeans = service.getAvailableBeans();
        beanNameField.setOptionsList(new ArrayList<>(availableBeans.keySet()));
        beanNameField.addValueChangeListener(e -> {
            methodNameField.setValue(null);
            if (e.getValue() == null) {
                methodNameField.setOptionsList(Collections.emptyList());
            } else {
                availableMethods = availableBeans.get(e.getValue());

                if (availableMethods != null) {
                    HashMap<String, Object> optionsMap = new HashMap<>();
                    for (MethodInfo availableMethod : availableMethods) {
                        optionsMap.put(availableMethod.getMethodSignature(), availableMethod);
                    }
                    methodNameField.setOptionsMap(optionsMap);
                }
            }
        });

        methodNameField.addValueChangeListener(e -> {
            clearMethodParamsGrid();
            if (e.getValue() != null) {
                createMethodParamsGrid((MethodInfo) e.getValue());
            }

            String methodName = (e.getValue() != null) ? ((MethodInfo) e.getValue()).getName() : null;
            taskDs.getItem().setMethodName(methodName);

            List<MethodParameterInfo> methodParams = (e.getValue() != null) ?
                    ((MethodInfo) e.getValue()).getParameters() : Collections.<MethodParameterInfo>emptyList();
            taskDs.getItem().updateMethodParameters(methodParams);
        });

        userNameField.setOptionsList(service.getAvailableUsers());
    }

    protected void setSchedulingTypeField(SchedulingType value) {
        if (SchedulingType.CRON == value) {
            hide(periodField, periodLabel, startDateField, startDateLabel);
            clear(periodField, startDateField);
            show(cronField, cronLabel, cronHelpButton, cronHbox);
        } else {
            hide(cronField, cronLabel, cronHelpButton, cronHbox);
            clear(cronField);
            show(periodField, periodLabel, startDateField, startDateLabel);
        }
    }


    @Override
    protected void initNewItem(ScheduledTask item) {
        item.setDefinedBy(ScheduledTaskDefinedBy.BEAN);
        item.setSchedulingType(SchedulingType.PERIOD);
    }

    @Override
    public void setItem(Entity item) {
        super.setItem(item);

        if (StringUtils.isNotEmpty(getItem().getMethodName())) {
            setInitialMethodNameValue(getItem());
        }
    }

    /**
     * Method reads values of methodName and parameters from item,
     * finds appropriate MethodInfo object in methodInfoField's optionsList
     * and sets found value to methodInfoField
     */
    protected void setInitialMethodNameValue(ScheduledTask task) {
        if (availableMethods == null)
            return;

        List<MethodParameterInfo> methodParamInfos = task.getMethodParameters();
        MethodInfo currentMethodInfo = new MethodInfo(task.getMethodName(), methodParamInfos);
        for (MethodInfo availableMethod : availableMethods) {
            if (currentMethodInfo.definitionEquals(availableMethod)) {
                availableMethod.setParameters(currentMethodInfo.getParameters());
                methodNameField.setValue(availableMethod);
                break;
            }
        }
    }

    protected void createMethodParamsGrid(MethodInfo methodInfo) {
        GridLayout methodParamsGrid = componentsFactory.createComponent(GridLayout.class);
        methodParamsGrid.setSpacing(true);
        methodParamsGrid.setColumns(2);

        int rowsCount = 0;

        for (final MethodParameterInfo parameterInfo : methodInfo.getParameters()) {
            Label nameLabel = componentsFactory.createComponent(Label.class);
            nameLabel.setValue(parameterInfo.getType().getSimpleName() + " " + parameterInfo.getName());

            TextField valueTextField = componentsFactory.createComponent(TextField.class);
            valueTextField.setWidth(themeConstants.get("cuba.gui.ScheduledTaskEditor.valueTextField.width"));
            valueTextField.setValue(parameterInfo.getValue());

            valueTextField.addValueChangeListener(e -> {
                parameterInfo.setValue(e.getValue());
                MethodInfo selectedMethod = methodNameField.getValue();
                taskDs.getItem().updateMethodParameters(selectedMethod.getParameters());
            });

            methodParamsGrid.setRows(++rowsCount);
            methodParamsGrid.add(nameLabel, 0, rowsCount - 1);
            methodParamsGrid.add(valueTextField, 1, rowsCount - 1);
        }
        methodParamsBox.add(methodParamsGrid);
    }

    protected void clearMethodParamsGrid() {
        for (Component component : methodParamsBox.getComponents()) {
            methodParamsBox.remove(component);
        }
    }

    public void getCronHelp() {
        getDialogParams().setModal(false);
        getDialogParams().setWidth(500);
        showMessageDialog("Cron", getMessage("cronDescription"), MessageType.CONFIRMATION_HTML);
    }
}