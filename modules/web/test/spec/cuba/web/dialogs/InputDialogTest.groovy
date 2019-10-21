/*
 * Copyright (c) 2008-2019 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package spec.cuba.web.dialogs

import com.google.common.base.Strings
import com.haulmont.chile.core.datatypes.DatatypeRegistry
import com.haulmont.chile.core.datatypes.impl.*
import com.haulmont.cuba.gui.Dialogs
import com.haulmont.cuba.gui.app.core.inputdialog.DialogActions
import com.haulmont.cuba.gui.app.core.inputdialog.InputDialog
import com.haulmont.cuba.gui.app.core.inputdialog.InputParameter
import com.haulmont.cuba.gui.components.*
import com.haulmont.cuba.gui.components.inputdialog.InputDialogAction
import com.haulmont.cuba.gui.screen.Screen
import com.haulmont.cuba.web.testmodel.sales.Status
import com.haulmont.cuba.web.testmodel.sample.GoodInfo
import spec.cuba.web.UiScreenSpec

import static com.haulmont.cuba.gui.app.core.inputdialog.InputDialog.*
import static com.haulmont.cuba.gui.app.core.inputdialog.InputDialog.InputDialogResult.ActionType.*
import static com.haulmont.cuba.gui.app.core.inputdialog.InputParameter.*
import static com.haulmont.cuba.gui.components.inputdialog.InputDialogAction.action
import static com.haulmont.cuba.gui.screen.FrameOwner.WINDOW_CLOSE_ACTION
import static com.haulmont.cuba.gui.screen.UiControllerUtils.getScreenContext

class InputDialogTest extends UiScreenSpec {

    Dialogs dialogs
    Screen mainWindow

    @SuppressWarnings("GroovyAssignabilityCheck")
    void setup() {
        exportScreensPackages(['com.haulmont.cuba.gui.app'])

        mainWindow = showMainWindow()

        dialogs = getScreenContext(mainWindow).getDialogs()
    }

    def "input parameter ids should be different"() {

        when: "the same id is used"
        dialogs.createInputDialog(mainWindow)
                .withParameters(
                        parameter("same"),
                        parameter("same"),
                        parameter("not the same"))
                .show()
        then:
        thrown(IllegalArgumentException)

        when: "different ids are used"
        InputDialog dialog = dialogs.createInputDialog(mainWindow)
                .withParameters(
                        parameter("not the same 1"),
                        parameter("not the same 2"),
                        parameter("not the same 3"))
                .build()
        then:
        dialog.show()
    }

    def "input parameter types are presented"() {

        when: "all types are used"
        InputDialog dialog = dialogs.createInputDialog(mainWindow)
                .withParameters(
                        parameter("default"),
                        stringParameter("string"),
                        intParameter("int"),
                        doubleParameter("double"),
                        longParameter("long"),
                        bigDecimalParameter("bigDecimal"),
                        booleanParameter("boolean"),
                        entityParameter("entity", GoodInfo),
                        timeParameter("time"),
                        dateParameter("date"),
                        dateTimeParameter("dateTime"))
                .show()
        then:
        def form = (Form) dialog.getWindow().getComponentNN("form")

        def defaultField = (TextField) form.getComponentNN("default")
        defaultField.getDatatype().getClass() == StringDatatype

        def stringField = (TextField) form.getComponentNN("string")
        stringField.getDatatype().getClass() == StringDatatype

        def intField = (TextField) form.getComponentNN("int")
        intField.getDatatype().getClass() == IntegerDatatype

        def doubleField = (TextField) form.getComponentNN("double")
        doubleField.getDatatype().getClass() == DoubleDatatype

        def bigDecimalField = (TextField) form.getComponentNN("bigDecimal")
        bigDecimalField.getDatatype().getClass() == BigDecimalDatatype

        (CheckBox) form.getComponentNN("boolean")
        (PickerField) form.getComponentNN("entity")

        def timeField = (TimeField) form.getComponentNN("time")
        timeField.getDatatype().getClass() == TimeDatatype

        def dateField = (DateField) form.getComponentNN("date")
        dateField.getDatatype().getClass() == DateDatatype

        def dateTimeField = (DateField) form.getComponentNN("dateTime")
        dateTimeField.getDatatype().getClass() == DateTimeDatatype
    }

    def "default actions are created"() {

        when: "YES NO CANCEL are created"
        InputDialog dialog = dialogs.createInputDialog(mainWindow)
                .withParameters(
                        parameter("default"),
                        stringParameter("string"))
                .withActions(DialogActions.YES_NO_CANCEL)
                .show()
        then:
        def actionsLayout = (HBoxLayout) dialog.getWindow().getComponentNN("actionsLayout")
        actionsLayout.ownComponents.size() == 4

        // YES action
        def yesBtn = (Button) actionsLayout.getComponent(1) // because 0 - spacer
        def yesAction = yesBtn.getAction() as DialogAction
        yesAction.getType() == DialogAction.Type.YES

        // NO action
        def noBtn = (Button) actionsLayout.getComponent(2)
        def noAction = (DialogAction) noBtn.getAction()
        noAction.getType() == DialogAction.Type.NO

        // CANCEL action
        def cancelBtn = (Button) actionsLayout.getComponent(3)
        def cancelAction = (DialogAction) cancelBtn.getAction()
        cancelAction.getType() == DialogAction.Type.CANCEL
    }

    def "default actions with result handler"() {

        given:

        def goodInfo = new GoodInfo()
        def defaultString = "default value"

        when: "dialog uses result handler"
        InputDialog dialog = dialogs.createInputDialog(mainWindow)
                .withParameters(
                        parameter("string").withDefaultValue(defaultString),
                        entityParameter("entity", GoodInfo).withDefaultValue(goodInfo))
                .withActions(DialogActions.YES_NO, { result ->
                    switch (result.getCloseActionType()) {
                        case YES:
                            assert result.getValue("string") == defaultString
                            assert result.getValue("entity") == goodInfo
                            assert result.getCloseAction() == INPUT_DIALOG_YES_ACTION
                            break
                        case NO:
                            assert result.getCloseAction() == INPUT_DIALOG_NO_ACTION
                            break
                    }
                })
                .show()
        then:
        def yesBtn = getButtonFromDialog(dialog, 1) // because 0 - spacer
        yesBtn.getAction().actionPerform(yesBtn)

        !screens.getOpenedScreens().getActiveScreens().contains(dialog)

        dialog.show() // we can show again because in this case we don't use code in Subscribe events
        def noBtn = getButtonFromDialog(dialog, 2)
        noBtn.getAction().actionPerform(noBtn)

        !screens.getOpenedScreens().getActiveScreens().contains(dialog)
    }

    def "custom input dialog actions"() {

        given:

        def dateValue = new Date()
        def stringValue = "Default value"

        when: "created custom action"
        InputDialog dialog = dialogs.createInputDialog(mainWindow)
                .withParameters(
                        parameter("string").withDefaultValue(stringValue),
                        dateParameter("date").withDefaultValue(dateValue))
                .withActions(
                        action("ok").withHandler({
                            InputDialogAction.InputDialogActionPerformed event ->
                                InputDialog dialog = event.getInputDialog()

                                assert dialog.getValue("string") == stringValue
                                assert dialog.getValue("date") == dateValue
                        }),
                        action("cancel").withHandler({
                            InputDialogAction.InputDialogActionPerformed event ->
                                event.getInputDialog().close(INPUT_DIALOG_CANCEL_ACTION)
                        }))
                .show()

        then:
        def actionsLayout = (HBoxLayout) dialog.getWindow().getComponentNN("actionsLayout")
        actionsLayout.getComponents().size() == 3

        def okBtn = (Button) actionsLayout.getComponent(1) // because 0 - spacer
        okBtn.getAction().actionPerform(okBtn)

        def cancelBtn = (Button) actionsLayout.getComponent(2)
        cancelBtn.getAction().actionPerform(cancelBtn)

        !screens.getOpenedScreens().getActiveScreens().contains(dialog)
    }

    def "open with close listener"() {

        when: "check closing with OK action"
        def dialog = (InputDialog) createDialogWithCloseListener()

        then:
        def okBtn = getButtonFromDialog(dialog, 1) // because 0 - spacer
        okBtn.getAction().actionPerform(okBtn)

        !screens.getOpenedScreens().getActiveScreens().contains(dialog)

        when: "check closing with CANCEL action"
        def cancelDialog = (InputDialog) createDialogWithCloseListener()

        then:
        def cancelBtn = getButtonFromDialog(cancelDialog, 2)
        cancelBtn.getAction().actionPerform(cancelBtn)

        !screens.getOpenedScreens().getActiveScreens().contains(dialog)
    }

    protected InputDialog createDialogWithCloseListener() {
        def bigDecimalValue = 1234

        return dialogs.createInputDialog(mainWindow)
                .withParameters(
                        bigDecimalParameter("bigDecimal").withDefaultValue(bigDecimalValue),
                        booleanParameter("boolean").withDefaultValue(true))
                .withCloseListener({ event ->
                    if (event.getCloseAction() == INPUT_DIALOG_OK_ACTION) {
                        assert event.getValue("bigDecimal") == bigDecimalValue
                        assert event.getValue("boolean") == true
                    } else {
                        assert event.getCloseAction() == INPUT_DIALOG_CANCEL_ACTION
                    }
                })
                .show()
    }

    def "input parameter with custom field"() {

        given:

        def customValue = "default value"
        def dateTimeValue = new Date()

        when: "get value from custom field"
        InputDialog dialog = dialogs.createInputDialog(mainWindow)
                .withParameters(
                        dateTimeParameter("dateTime").withDefaultValue(dateTimeValue),
                        new InputParameter("custom")
                            .withField({
                                TextField field = uiComponents.create(TextField)
                                field.setValue(customValue)
                                return field}))
                .withCloseListener({ event ->
                    if (event.getCloseAction() == INPUT_DIALOG_OK_ACTION) {
                        assert event.getValue("dateTime") == dateTimeValue
                        assert event.getValue("custom") == customValue
                    }})
                .show()
        then:
        def okBtn = getButtonFromDialog(dialog, 1) // because 0 - spacer
        okBtn.getAction().actionPerform(okBtn)
    }

    def "field validation"() {
        def datatypeRegistry = cont.getBean(DatatypeRegistry)

        when: "custom field has incorrect value"
        InputDialog dialog = dialogs.createInputDialog(mainWindow)
                .withParameters(
                        dateParameter("date"),
                        new InputParameter("custom")
                                .withField({
                                    TextField field = uiComponents.create(TextField)
                                    field.setValue("sda")
                                    field.setDatatype(datatypeRegistry.getNN(Integer))
                                    return field}))
                .show()
        then:
        def okBtn = getButtonFromDialog(dialog, 1) // because 0 - spacer
        okBtn.getAction().actionPerform(okBtn)

        // shouldn't be closed
        screens.getOpenedScreens().getActiveScreens().contains(dialog)

        when: "field is required"
        InputDialog reqDialog = dialogs.createInputDialog(mainWindow)
                .withParameters(intParameter("int").withRequired(true))
                .show()

        then:
        def reqOkBtn = getButtonFromDialog(reqDialog, 1) // because 0 - spacer
        reqOkBtn.getAction().actionPerform(reqOkBtn)

        // shouldn't be closed
        screens.getOpenedScreens().getActiveScreens().contains(reqDialog)
    }

    def "validator and default DialogActions"() {

        when: "create dialog with default actions and custom validator"
        InputDialog dialog = dialogs.createInputDialog(mainWindow)
                .withParameters(
                    stringParameter("phoneField"),
                    stringParameter("addressField"))
                .withValidator({ context ->
                    def phone = (String) context.getValue("phoneField")
                    def address = (String) context.getValue("addressField")
                    if (Strings.isNullOrEmpty(phone) && Strings.isNullOrEmpty(address)) {
                        return ValidationErrors.of("Phone or Address should be filled")
                    }
                    return ValidationErrors.none()
                })
                .show()
        then:
        def okBtn = getButtonFromDialog(dialog, 1)
        okBtn.getAction().actionPerform(okBtn)

        // shouldn't be closed
        screens.getOpenedScreens().getActiveScreens().contains(dialog)
    }

    def "validator and validationRequired in InputDialogAction"() {

        when: "validator and validationRequired in InputDialogAction"
        def targetValue = 100100
        def date = new Date(targetValue)

        InputDialog dialogA = dialogs.createInputDialog(mainWindow)
                .withParameters(
                        stringParameter("descriptionField"),
                        dateParameter("expirationDate").withDefaultValue(date))
                .withActions(
                        action("okAction")
                                .withHandler({ event ->
                                    event.getInputDialog().close(WINDOW_CLOSE_ACTION)
                                }),
                        action("cancelAction")
                                .withValidationRequired(false)
                                .withHandler({ event ->
                                    event.getInputDialog().close(WINDOW_CLOSE_ACTION)
                                }))
                .withValidator({ values ->
                    def expDate = (Date) values.getValue("expirationDate")
                    if (expDate.getTime() < targetValue + 1) {
                        return ValidationErrors.of("Wrong expiration date")
                    }
                    return ValidationErrors.none()
                })
                .show()

        then:
        def okBtnA = getButtonFromDialog(dialogA, 1)
        okBtnA.getAction().actionPerform(okBtnA)
        // shouldn't be closed
        screens.getOpenedScreens().getActiveScreens().contains(dialogA)

        def cancelBtnA = getButtonFromDialog(dialogA, 2)
        cancelBtnA.getAction().actionPerform(cancelBtnA)

        // must be closed
        !screens.getOpenedScreens().getActiveScreens().contains(dialogA)
    }

    def "enum input parameter"() {

        when: "validator and validationRequired in InputDialogAction"

        InputDialog dialog = dialogs.createInputDialog(mainWindow)
                .withParameters(enumParameter("enumField", Status)
                .withDefaultValue(Status.OK))
                .withActions(DialogActions.OK_CANCEL, { result ->
                    switch (result.closeActionType) {
                        case OK:
                            Status status = result.getValue("enumField")
                            assert status == Status.OK
                            break
                    }
                })
                .show()

        then:
        def okBtnA = getButtonFromDialog(dialog, 1)
        okBtnA.getAction().actionPerform(okBtnA)

        // must be closed
        !screens.getOpenedScreens().getActiveScreens().contains(dialog)
    }

    protected static Button getButtonFromDialog(InputDialog dialog, int index) {
        def actionsLayout = (OrderedContainer) dialog.getWindow().getComponentNN("actionsLayout")
        return (Button) actionsLayout.getComponent(index) // 0 - spacer
    }
}