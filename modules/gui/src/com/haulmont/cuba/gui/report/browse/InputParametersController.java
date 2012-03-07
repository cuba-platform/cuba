/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 17.05.2010 11:56:57
 *
 * $Id$
 */
package com.haulmont.cuba.gui.report.browse;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.DateField.Resolution;
import com.haulmont.cuba.gui.components.validators.DoubleValidator;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.report.ReportHelper;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.report.ParameterType;
import com.haulmont.cuba.report.Report;
import com.haulmont.cuba.report.ReportInputParameter;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import java.util.*;

public class InputParametersController extends AbstractWindow {

    private interface FieldCreator {
        Field createField(ReportInputParameter parameter);
    }

    private ComponentsFactory cFactory = AppConfig.getFactory();

    public InputParametersController(IFrame frame) {
        super(frame);
    }

    private Report report;
    private Entity linkedEntity;

    @Inject
    private GridLayout parametersGrid;
    private int number = 0;
    private HashMap<String, Field> parameterComponents = new HashMap<String, Field>();
    private Map<ParameterType, FieldCreator> fieldCreationMapping = new HashMap<ParameterType, FieldCreator>();
    {
        fieldCreationMapping.put(ParameterType.BOOLEAN, new CheckBoxCreator());
        fieldCreationMapping.put(ParameterType.DATE, new DataFieldCreator());
        fieldCreationMapping.put(ParameterType.ENTITY, new SingleFieldCreator());
        fieldCreationMapping.put(ParameterType.ENUMERATION, new EnumFieldCreator());
        fieldCreationMapping.put(ParameterType.TEXT, new TextFieldCreator());
        fieldCreationMapping.put(ParameterType.NUMERIC, new NumericFieldCreator());
        fieldCreationMapping.put(ParameterType.ENTITY_LIST, new MultiFieldCreator());
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        report = (Report) params.get("param$report");
        linkedEntity = (Entity) params.get("param$entity");

        if (report != null) {
            report = getDsContext().getDataService().reload(report, "report.edit");
            if (report.getInputParameters() != null)
                parametersGrid.setRows(report.getInputParameters().size());
            else
                parametersGrid.setRows(0);

            for (ReportInputParameter parameter: report.getInputParameters()) {
                createComponent(parameter);
            }
        }
    }

    @SuppressWarnings("unused")
    public void printReport() {
        if (report != null) {
            try {
                validate();
                Map<String, Object> collectedParams = collectParameters(parameterComponents);
                ReportHelper.printReport(report, collectedParams);
            } catch (com.haulmont.cuba.gui.components.RequiredValueMissingException e) {
                showNotification(getMessage("input.requiredParametersNotSet"), IFrame.NotificationType.WARNING);
            } catch (ValidationException e) {
                showNotification(getMessage("input.requiredParametersNotSet"), IFrame.NotificationType.WARNING);
            }
        }
    }

    private Map<String, Object> collectParameters(HashMap<String, Field> parameterComponents)
            throws com.haulmont.cuba.gui.components.ValidationException {

        Map<String, Object> parameters = new HashMap<String, Object>();
        for (String paramName : parameterComponents.keySet()) {
            Field _field = parameterComponents.get(paramName);
            _field.validate();
            Object value = _field.getValue();
            parameters.put(paramName, value);
        }
        return parameters;
    }

    //todo: reimplement this method

    private void createComponent(ReportInputParameter parameter) {
        Field field = fieldCreationMapping.get(parameter.getType()).createField(parameter);
        field.setId(parameter.getAlias());
        field.setWidth("250px");
        field.setFrame(this);
        field.setEditable(true);

        parameterComponents.put(parameter.getAlias(), field);
        if (parameter.getRequired()) {
            field.setRequired(true);
        }

        Label label = cFactory.createComponent(Label.NAME);
        label.setValue(parameter.getLocName());

        parametersGrid.add(label, 0, number);
        parametersGrid.add(field, 1, number);
        number++;
    }

    private class DataFieldCreator implements FieldCreator {

        @Override
        public Field createField(ReportInputParameter parameter) {
            DateField dateField = cFactory.createComponent(DateField.NAME);
            dateField.setResolution(Resolution.DAY);
            return dateField;
        }
    }

    private class CheckBoxCreator implements FieldCreator {

        @Override
        public Field createField(ReportInputParameter parameter) {
            return cFactory.createComponent(CheckBox.NAME);
        }
    }

    private class TextFieldCreator implements FieldCreator {

        @Override
        public Field createField(ReportInputParameter parameter) {
            return cFactory.createComponent(TextField.NAME);
        }
    }

    private class NumericFieldCreator implements FieldCreator {

        @Override
        public Field createField(ReportInputParameter parameter) {
            TextField textField = cFactory.createComponent(TextField.NAME);
            textField.addValidator(new DoubleValidator());
            return textField;
        }
    }

    private class EnumFieldCreator implements FieldCreator {

        @Override
        public Field createField(ReportInputParameter parameter) {
            LookupField lookupField = cFactory.createComponent(LookupField.NAME);
            String enumClassName = parameter.getEnumerationClass();
            if (StringUtils.isNotEmpty(enumClassName)) {
                Class enumClass;
                try {
                    enumClass = Class.forName(enumClassName);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                if (enumClass != null) {
                    Object[] constants = enumClass.getEnumConstants();
                    List<Object> optionsList = new ArrayList<Object>();
                    Collections.addAll(optionsList, constants);

                    lookupField.setOptionsList(optionsList);
                    lookupField.setCaptionMode(CaptionMode.ITEM);
                }
            }
            return lookupField;
        }
    }

    private class SingleFieldCreator implements FieldCreator {

        @Override
        public Field createField(ReportInputParameter parameter) {
            PickerField pickerField = cFactory.createComponent(PickerField.NAME);
            final com.haulmont.chile.core.model.MetaClass entityMetaClass =
                    MetadataProvider.getSession().getClass(parameter.getEntityMetaClass());
            Class clazz = entityMetaClass.getJavaClass();

            pickerField.setMetaClass(entityMetaClass);

            PickerField.LookupAction pickerlookupAction = pickerField.addLookupAction();
            pickerlookupAction.setLookupScreenOpenType(WindowManager.OpenType.DIALOG);

            String alias = parameter.getScreen();

            if (StringUtils.isNotEmpty(alias)) {
                pickerlookupAction.setLookupScreen(alias);
                pickerlookupAction.setLookupScreenParams(Collections.<String, Object>emptyMap());
            } else {
                pickerlookupAction.setLookupScreen("report$commonLookup");
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("class", entityMetaClass);

                pickerlookupAction.setLookupScreenParams(params);
            }

            if ((linkedEntity != null) && (clazz != null) && (clazz.isAssignableFrom(linkedEntity.getClass())))
                pickerField.setValue(linkedEntity);

            return pickerField;
        }
    }

    private class MultiFieldCreator implements FieldCreator {

        @Override
        public Field createField(ReportInputParameter parameter) {
            TokenList tokenList = cFactory.createComponent(TokenList.NAME);
            final com.haulmont.chile.core.model.MetaClass entityMetaClass =
                    MetadataProvider.getSession().getClass(parameter.getEntityMetaClass());

            DsBuilder builder = new DsBuilder(getDsContext());
            CollectionDatasource cds = builder
                    .setRefreshMode(CollectionDatasource.RefreshMode.NEVER)
                    .setId("entities_" + parameter.getAlias())
                    .setMetaClass(entityMetaClass)
                    .setViewName(View.LOCAL)
                    .buildCollectionDatasource();

            cds.refresh();

            tokenList.setDatasource(cds);
            tokenList.setEditable(true);
            tokenList.setLookup(true);
            tokenList.setLookupOpenMode(WindowManager.OpenType.DIALOG);
//            tokenList.setHeight("150px");
            String alias = parameter.getScreen();

            if (StringUtils.isNotEmpty(alias)) {
                tokenList.setLookupScreen(alias);
                tokenList.setLookupScreenParams(Collections.<String, Object>emptyMap());
            } else {
                tokenList.setLookupScreen("report$commonLookup");
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("class", entityMetaClass);
                tokenList.setLookupScreenParams(params);
            }

            tokenList.setAddButtonCaption(MessageProvider.getMessage(TokenList.class, "actions.Select"));
            tokenList.setSimple(true);

            return tokenList;
        }
    }
}