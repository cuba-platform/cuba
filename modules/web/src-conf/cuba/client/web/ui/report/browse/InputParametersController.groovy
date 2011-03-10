/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 17.05.2010 11:56:57
 *
 * $Id$
 */
package cuba.client.web.ui.report.browse

import com.haulmont.cuba.gui.components.AbstractWindow
import com.haulmont.cuba.gui.components.IFrame
import com.haulmont.cuba.report.Report
import com.haulmont.cuba.report.ReportInputParameter

import com.haulmont.cuba.web.gui.components.WebTextField
import com.haulmont.cuba.gui.components.Component
import com.haulmont.cuba.report.ParameterType
import com.haulmont.cuba.web.gui.components.WebDateField

import com.haulmont.cuba.web.gui.components.WebLabel
import com.haulmont.cuba.web.gui.components.WebGridLayout
import com.haulmont.cuba.gui.components.Button

import com.haulmont.cuba.gui.components.ActionAdapter
import com.haulmont.cuba.gui.components.Field

import com.haulmont.cuba.gui.WindowManager
import com.haulmont.cuba.gui.components.Window

import com.haulmont.cuba.gui.components.Window.Lookup.Handler
import com.haulmont.cuba.web.gui.components.WebActionsField
import com.haulmont.cuba.gui.components.ActionsField
import com.haulmont.cuba.web.gui.components.WebComponentsHelper
import com.vaadin.ui.AbstractSelect

import com.haulmont.cuba.core.entity.Entity

import com.haulmont.cuba.core.global.MetadataProvider
import com.haulmont.cuba.web.gui.components.WebCheckBox
import com.haulmont.cuba.gui.components.validators.DoubleValidator
import com.vaadin.data.Validator
import com.haulmont.chile.core.model.Instance
import com.haulmont.cuba.core.global.MessageProvider
import com.haulmont.cuba.web.app.ui.report.ReportHelper
import com.haulmont.cuba.gui.components.LookupField
import com.haulmont.cuba.web.gui.components.WebLookupField
import org.apache.commons.lang.StringUtils
import com.haulmont.cuba.web.gui.data.EnumerationContainer
import com.haulmont.cuba.gui.components.CaptionMode

public class InputParametersController extends AbstractWindow {

    def InputParametersController(IFrame frame) {
        super(frame);
    }

    private Report report
    private Entity linkedEntity

    private WebGridLayout grid
    private int number = 0
    private HashMap<String, Field> parameterComponents = new HashMap<String, Field>()

    protected void init(Map<String, Object> params) {
        super.init(params);
        report = (Report) params['param$report']
        linkedEntity = (Entity) params['param$entity']

        if (report) {
            grid = (WebGridLayout) getComponent('parametersGrid')

            report = getDsContext().getDataService().reload(report, 'report.edit')
            grid.setRows(report.inputParameters?.size() ?: 0)
            for (ReportInputParameter parameter: report.getInputParameters()) {
                createComponent(parameter)
            }

            Button printReportButton = getComponent('printReport')
            def printReport = [
                    actionPerform: {Component component ->
                        if (report != null) {
                            try {
                                Map<String, Object> collectedParams = collectParameters(parameterComponents)
                                ReportHelper.printReport(report, collectedParams)
                            } catch (com.haulmont.cuba.gui.components.ValidationException e) {
                            } catch (Validator.InvalidValueException e) {
                                showNotification(getMessage('input.requiredParametersNotSet'), IFrame.NotificationType.WARNING)
                            }
                        }
                    }
            ]
            printReportButton.action = new ActionAdapter('input.printReport', messagesPack, printReport)
        }
    }

    private Map collectParameters(HashMap<String, Field> parameterComponents) throws com.haulmont.cuba.gui.components.ValidationException {
        def parameters = [:]
        for (String paramName: parameterComponents.keySet()) {
            Field _field = parameterComponents[paramName]
            _field.validate()
            Object value = _field.getValue()
            parameters[paramName] = value
        }
        return parameters
    }

    //todo: reimplement this method

    private void createComponent(ReportInputParameter parameter) {
        Field field = (Field) fieldCreationMapping.get(parameter.type).call(parameter)
        if (parameter.required) {
            field.setRequired(true)
        }

        WebLabel label = new WebLabel()
        label.setValue(parameter.name)
        parameterComponents.put(parameter.alias, field)
        field.setId(parameter.alias)
        field.setWidth("200px")
        grid.add(label, 0, number)
        grid.add(field, 1, number)
        number++
    }

    private def createDateField = { ReportInputParameter parameter ->
        return new WebDateField();
    }

    private def createCheckBoxField = { ReportInputParameter parameter ->
        return new WebCheckBox()
    }

    private def createTextField = { ReportInputParameter parameter ->
        return new WebTextField();
    }

    private def createNumericField = { ReportInputParameter parameter ->
        WebTextField wtf = new WebTextField();
        wtf.addValidator(new DoubleValidator())
        return wtf
    }

    private def createEnumLookup = { ReportInputParameter parameter ->
        final LookupField lookupField = new WebLookupField()
        String enumClassName = parameter.getEnumerationClass()
        if (StringUtils.isNotEmpty(enumClassName)) {
            Class enumClass = Class.forName(enumClassName)
            if (enumClass != null) {
                def optionsList = new ArrayList( Arrays.asList( enumClass.getEnumConstants() ));

                lookupField.setOptionsList(optionsList);
                lookupField.setCaptionMode(CaptionMode.ITEM);
            }
        }

        return lookupField
    }

    private def createLookupField = { Boolean isMulti, ReportInputParameter parameter ->
        final WebActionsField waf = new WebActionsField()

        final com.haulmont.chile.core.model.MetaClass entityMetaClass = MetadataProvider.getSession().getClass(parameter.entityMetaClass)
        Class clazz = entityMetaClass.getJavaClass();

        if (linkedEntity && clazz && clazz.isAssignableFrom(linkedEntity.getClass())) {
            setValueToWebActionsField(waf, linkedEntity)
        }

        def action = [
                actionPerform: {Component _component ->
                    def handler = [
                            handleLookup: {Collection items ->
                                if (items && items.size() > 0) {
                                    if (isMulti) {
                                        setValueToWebActionsField(waf, items)
                                    }
                                    else {
                                        Entity entity = items.iterator().next();
                                        setValueToWebActionsField(waf, entity)
                                    }
                                }
                            }
                    ] as Handler
                    //todo: use also custom browser
                    String alias = parameter.screen
                    if (alias && !"".equals(alias)) {
                        Window w = openLookup(alias, handler, WindowManager.OpenType.DIALOG, [:])
                        w.setHeight("400px")
                    } else {
                        Window w = openLookup('report$commonLookup', handler, WindowManager.OpenType.DIALOG, (Map<String, Object>) ['class': entityMetaClass])
                        w.setHeight("400px")
                    }
                }
        ]

        waf.enableButton(ActionsField.LOOKUP, true)
        waf.addAction(new ActionAdapter(ActionsField.LOOKUP, action))

        return waf
    }

    private def setValueToWebActionsField(WebActionsField waf, Object object) {
        AbstractSelect abstractSelect = (AbstractSelect) WebComponentsHelper.unwrap(waf.getLookupField())
        abstractSelect.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_EXPLICIT)
        if (!abstractSelect.containsId(object)) {
            abstractSelect.addItem(object)
            if (object instanceof Entity)
                abstractSelect.setItemCaption(object, ((Instance) object).getInstanceName())
            else if (object instanceof Collection)
                abstractSelect.setItemCaption(object, MessageProvider.getMessage(getClass(), 'input.entitiesList'))
        }
        abstractSelect.setValue(object)
    }

    def Map<ParameterType, Closure> fieldCreationMapping = new HashMap<ParameterType, Closure>();
    {
        fieldCreationMapping.put(ParameterType.DATE, createDateField)
        fieldCreationMapping.put(ParameterType.TEXT, createTextField)
        fieldCreationMapping.put(ParameterType.ENTITY, createLookupField.curry(false))
        fieldCreationMapping.put(ParameterType.ENTITY_LIST, createLookupField.curry(true))
        fieldCreationMapping.put(ParameterType.BOOLEAN, createCheckBoxField)
        fieldCreationMapping.put(ParameterType.NUMERIC, createNumericField)
        fieldCreationMapping.put(ParameterType.ENUMERATION, createEnumLookup)
    }

}