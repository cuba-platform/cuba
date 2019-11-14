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

package com.haulmont.cuba.gui.app.core.inputdialog;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.DatatypeRegistry;
import com.haulmont.chile.core.datatypes.impl.*;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.actions.picker.ClearAction;
import com.haulmont.cuba.gui.actions.picker.LookupAction;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.options.ContainerOptions;
import com.haulmont.cuba.gui.components.inputdialog.InputDialogAction;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataComponents;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.theme.ThemeConstants;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@UiDescriptor("inputdialog.xml")
@UiController("inputDialog")
public class InputDialog extends Screen {

    /**
     * A {@link CloseAction} used when the user clicks "OK" button and fields validation is successful.
     */
    public static final CloseAction INPUT_DIALOG_OK_ACTION = new StandardCloseAction("inputDialogOk");

    /**
     * A {@link CloseAction} used when the user clicks "CANCEL" button.
     */
    public static final CloseAction INPUT_DIALOG_CANCEL_ACTION = new StandardCloseAction("inputDialogCancel");

    /**
     * A {@link CloseAction} used when the user clicks "YES" button and fields validation is successful.
     */
    public static final CloseAction INPUT_DIALOG_YES_ACTION = new StandardCloseAction("inputDialogYes");

    /**
     * A {@link CloseAction} used when the user clicks "NO" button.
     */
    public static final CloseAction INPUT_DIALOG_NO_ACTION = new StandardCloseAction("inputDialogNo");


    @Inject
    protected UiComponents uiComponents;

    @Inject
    protected DatatypeRegistry datatypeRegistry;

    @Inject
    protected Metadata metadata;

    @Inject
    protected Actions actions;

    @Inject
    protected Messages messages;

    @Inject
    protected Icons icons;

    @Inject
    protected ScreenValidation screenValidation;

    @Inject
    protected ThemeConstants theme;

    @Inject
    protected PersistenceManagerService persistenceManagerService;

    @Inject
    protected DataComponents dataComponents;

    @Inject
    protected Form form;

    @Inject
    protected HBoxLayout actionsLayout;

    protected List<InputParameter> parameters = new ArrayList<>(2);
    protected List<Action> actionsList = new ArrayList<>(2);

    protected DialogActions dialogActions = DialogActions.OK_CANCEL;
    protected List<String> fieldIds;

    protected Consumer<InputDialogCloseEvent> closeListener;
    protected Consumer<InputDialogResult> resultHandler;
    protected Function<ValidationContext, ValidationErrors> validator;

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        initParameters();
        if (actionsList.isEmpty()) {
            initDialogActions();
        } else {
            initActions(actionsList);
        }
    }

    @Subscribe
    protected void onAfterClose(AfterCloseEvent event) {
        if (closeListener != null) {
            closeListener.accept(new InputDialogCloseEvent(getValues(), event.getCloseAction()));
        }
    }

    /**
     * Returns value from parameter by its id.
     *
     * @param parameterId parameter id
     * @return parameter value
     * @throws IllegalArgumentException exception if wrong parameter id is sent
     */
    @SuppressWarnings("unchecked")
    public <T> T getValue(String parameterId) {
        Component component = form.getComponentNN(parameterId);
        if (component instanceof Field) {
            return (T) ((Field) component).getValue();
        }

        throw new IllegalArgumentException("InputDialog doesn't contains parameter with id: " + parameterId);
    }

    /**
     * Returns optional value from parameter by its id.
     *
     * @param parameterId parameter id
     * @return optional parameter value
     */
    public <T> Optional<T> getOptional(String parameterId) {
        return Optional.ofNullable(getValue(parameterId));
    }

    /**
     * @return dialog window in which you can set dialog properties (e.g. modal, resizable, etc)
     */
    public DialogWindow getDialogWindow() {
        return (DialogWindow) getWindow();
    }

    /**
     * Returns values from parameters. String - parameter id, Object - parameter value.
     *
     * @return values
     */
    public Map<String, Object> getValues() {
        ParamsMap paramsMap = ParamsMap.of();

        for (String id : fieldIds) {
            Component component = form.getComponentNN(id);
            paramsMap.pair(id, ((Field) component).getValue());
        }

        return paramsMap.create();
    }

    /**
     * Add input parameter to the dialog. Input parameter will be represented as a field.
     *
     * @param parameter input parameter that will be added to the dialog
     */
    public void setParameter(InputParameter parameter) {
        parameters.add(parameter);
    }

    /**
     * Sets input parameters.
     *
     * @param parameters input parameters
     */
    public void setParameters(InputParameter... parameters) {
        this.parameters.addAll(Arrays.asList(parameters));
    }

    /**
     * @return input parameters from dialog
     */
    public List<InputParameter> getParameters() {
        return parameters;
    }

    /**
     * Add close listener to the dialog.
     *
     * @param listener close listener to add
     */
    public void setCloseListener(Consumer<InputDialogCloseEvent> listener) {
        this.closeListener = listener;
    }

    /**
     * @return close listener
     */
    public Consumer<InputDialogCloseEvent> getCloseListener() {
        return closeListener;
    }

    /**
     * Sets dialog actions. If there is no actions are set input dialog will use {@link DialogActions#OK_CANCEL}.
     *
     * @param actions actions
     * @see InputDialogAction
     */
    public void setActions(InputDialogAction... actions) {
        this.actionsList.addAll(Arrays.asList(actions));
    }

    /**
     * @return actions list
     */
    public List<Action> getActions() {
        return actionsList;
    }

    /**
     * Sets predefined dialog actions. By default if there is no actions are input dialog will use
     * {@link DialogActions#OK_CANCEL}.
     *
     * @param actions actions
     */
    public void setDialogActions(DialogActions actions) {
        this.dialogActions = actions;
    }

    /**
     * Returns predefined dialog actions. {@link DialogActions#OK_CANCEL} by default.
     *
     * @return dialog actions
     */
    public DialogActions getDialogActions() {
        return dialogActions;
    }

    /**
     * Sets handler for dialog actions (e.g. OK, CANCEL, etc) that are used in the dialog. Handler is invoked after
     * close event and can be used instead of {@link #setCloseListener(Consumer)}.
     * <p>
     * Note, it is worked only with {@link #setDialogActions(DialogActions)}. Custom actions are not handled.
     *
     * @param resultHandler result handler
     */
    public void setResultHandler(Consumer<InputDialogResult> resultHandler) {
        this.resultHandler = resultHandler;
    }

    /**
     * @return result handler
     */
    @Nullable
    public Consumer<InputDialogResult> getResultHandler() {
        return resultHandler;
    }

    /**
     * Validates form components and conditions from custom validation supplier and show errors.
     *
     * @return true if validation is successful
     */
    public boolean isValid() {
        ValidationErrors validationErrors = screenValidation.validateUiComponents(form);
        if (validator != null) {
            ValidationErrors errors = validator.apply(new ValidationContext(getValues(), this));
            validationErrors.addAll(errors == null ? ValidationErrors.none() : errors);
        }

        if (!validationErrors.isEmpty()) {
            screenValidation.showValidationErrors(this, validationErrors);
            return false;
        }
        return true;
    }

    /**
     * Sets additional handler for field validation. It takes values map and must return {@link ValidationErrors}
     * instance. Returned validation errors will be shown with another errors from fields.
     *
     * @param validator validator
     */
    public void setValidator(Function<ValidationContext, ValidationErrors> validator) {
        this.validator = validator;
    }

    /**
     * @return additional field validator
     */
    public Function<ValidationContext, ValidationErrors> getValidator() {
        return validator;
    }

    @SuppressWarnings("unchecked")
    protected void initParameters() {
        fieldIds = new ArrayList<>(parameters.size());

        for (InputParameter parameter : parameters) {
            if (fieldIds.contains(parameter.getId())) {
                throw new IllegalArgumentException("InputDialog cannot contain parameters with the same id: '" + parameter.getId() + "'");
            }

            Field field;
            if (parameter.getField() != null) {
                field = parameter.getField().get();
            } else {
                field = createField(parameter);
                field.setCaption(parameter.getCaption());
                field.setValue(parameter.getDefaultValue());
                field.setRequired(parameter.isRequired());
            }
            field.setId(parameter.getId());

            fieldIds.add(field.getId());
            form.add(field);
        }
    }

    @SuppressWarnings("unchecked")
    protected Field createField(InputParameter parameter) {
        if (parameter.getEntityClass() != null) {
            return createEntityField(parameter);
        } else if (parameter.getEnumClass() != null) {
            return createEnumField(parameter);
        }

        Datatype datatype = null;
        if (parameter.getDatatypeJavaClass() != null) {
            datatype = datatypeRegistry.get(parameter.getDatatypeJavaClass());
        } else if (parameter.getDatatype() != null) {
            datatype = parameter.getDatatype();
        }

        if (datatype == null) {
            Field field = createFieldByClass(parameter.getDatatypeJavaClass());
            if (field != null) {
                return field;
            }
            datatype = datatypeRegistry.getNN(String.class);
        }

        if (datatype instanceof NumberDatatype
                || datatype instanceof StringDatatype) {
            TextField field = uiComponents.create(TextField.NAME);
            field.setWidthFull();
            field.setDatatype(datatype);
            return field;
        } else if (datatype instanceof DateDatatype) {
            DateField dateField = uiComponents.create(DateField.NAME);
            dateField.setDatatype(datatype);
            dateField.setResolution(DateField.Resolution.DAY);
            return dateField;
        } else if (datatype instanceof DateTimeDatatype) {
            DateField dateField = uiComponents.create(DateField.NAME);
            dateField.setDatatype(datatype);
            dateField.setResolution(DateField.Resolution.MIN);
            return dateField;
        } else if (datatype instanceof TimeDatatype) {
            TimeField timeField = uiComponents.create(TimeField.NAME);
            timeField.setDatatype(datatype);
            return timeField;
        } else if (datatype instanceof LocalDateDatatype) {
            DateField dateField = uiComponents.create(DateField.NAME);
            dateField.setDatatype(datatype);
            dateField.setResolution(DateField.Resolution.DAY);
            return dateField;
        } else if (datatype instanceof LocalDateTimeDatatype) {
            DateField dateField = uiComponents.create(DateField.NAME);
            dateField.setDatatype(datatype);
            dateField.setResolution(DateField.Resolution.MIN);
            return dateField;
        } else if (datatype instanceof LocalTimeDatatype) {
            TimeField timeField = uiComponents.create(TimeField.NAME);
            timeField.setDatatype(datatype);
            return timeField;
        } else if (datatype instanceof OffsetDateTimeDatatype) {
            DateField dateField = uiComponents.create(DateField.NAME);
            dateField.setDatatype(datatype);
            dateField.setResolution(DateField.Resolution.MIN);
            return dateField;
        } else if (datatype instanceof OffsetTimeDatatype) {
            TimeField timeField = uiComponents.create(TimeField.NAME);
            timeField.setDatatype(datatype);
            return timeField;
        } else if (datatype instanceof BooleanDatatype) {
            return uiComponents.create(CheckBox.NAME);
        } else {
            throw new IllegalArgumentException("InputDialog doesn't support datatype: " + datatype.getClass());
        }
    }

    @SuppressWarnings("unchecked")
    protected Field createEntityField(InputParameter parameter) {
        MetaClass metaClass = metadata.getClassNN(parameter.getEntityClass());
        Action lookupAction = actions.create(LookupAction.ID);
        Action clearAction = actions.create(ClearAction.ID);

        if (persistenceManagerService.useLookupScreen(metaClass.getName())) {
            PickerField pickerField = uiComponents.create(PickerField.NAME);
            pickerField.setMetaClass(metadata.getClass(parameter.getEntityClass()));
            pickerField.addAction(lookupAction);
            pickerField.addAction(clearAction);
            pickerField.setWidthFull();
            return pickerField;
        } else {
            LookupPickerField lookupPickerField = uiComponents.create(LookupPickerField.NAME);
            lookupPickerField.addAction(lookupAction);
            lookupPickerField.addAction(clearAction);
            lookupPickerField.setWidthFull();

            CollectionContainer container = dataComponents.createCollectionContainer(parameter.getEntityClass());
            CollectionLoader loader = dataComponents.createCollectionLoader();
            loader.setQuery("select e from " + metaClass.getName() + " e");
            loader.setView(View.MINIMAL);
            loader.setContainer(container);
            loader.load();

            lookupPickerField.setOptions(new ContainerOptions(container));
            return lookupPickerField;
        }
    }

    @SuppressWarnings("unchecked")
    protected Field createEnumField(InputParameter parameter) {
        LookupField lookupField = uiComponents.create(LookupField.NAME);
        lookupField.setOptionsEnum(parameter.getEnumClass());
        lookupField.setWidthFull();
        return lookupField;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    protected Field createFieldByClass(@Nullable Class datatypeJavaClass) {
        if (datatypeJavaClass == null) {
            return null;
        }

        if (datatypeJavaClass.isAssignableFrom(FileDescriptor.class)) {
            FileUploadField fileUploadField = uiComponents.create(FileUploadField.NAME);
            fileUploadField.setShowFileName(true);
            fileUploadField.setShowClearButton(true);
            return fileUploadField;
        }
        return null;
    }

    protected void initActions(List<Action> actions) {
        for (Action action : actions) {
            Button button = uiComponents.create(Button.NAME);
            button.setAction(action);
            button.setId(action.getId() + "Btn");

            if (action instanceof DialogAction) {
                DialogAction.Type type = ((DialogAction) action).getType();
                button.setId(type.getId() + "Btn");
                button.setCaption(messages.getMainMessage(type.getMsgKey()));

                String iconPath = icons.get(type.getIconKey());
                button.setIcon(iconPath);
            }

            actionsLayout.add(button);
        }
    }

    protected void initDialogActions() {
        List<Action> actions = new ArrayList<>(2);
        switch (dialogActions) {
            case OK:
                actions.add(createDialogAction(DialogAction.Type.OK, INPUT_DIALOG_OK_ACTION));
                break;
            case YES_NO:
                actions.add(createDialogAction(DialogAction.Type.YES, INPUT_DIALOG_YES_ACTION));
                actions.add(createDialogAction(DialogAction.Type.NO, INPUT_DIALOG_NO_ACTION));
                break;
            case OK_CANCEL:
                actions.add(createDialogAction(DialogAction.Type.OK, INPUT_DIALOG_OK_ACTION));
                actions.add(createDialogAction(DialogAction.Type.CANCEL, INPUT_DIALOG_CANCEL_ACTION));
                break;
            case YES_NO_CANCEL:
                actions.add(createDialogAction(DialogAction.Type.YES, INPUT_DIALOG_YES_ACTION));
                actions.add(createDialogAction(DialogAction.Type.NO, INPUT_DIALOG_NO_ACTION));
                actions.add(createDialogAction(DialogAction.Type.CANCEL, INPUT_DIALOG_CANCEL_ACTION));
                break;
        }
        initActions(actions);
    }

    protected DialogAction createDialogAction(DialogAction.Type type, CloseAction closeAction) {
        DialogAction dialogAction = new DialogAction(type);
        if (type == DialogAction.Type.OK || type == DialogAction.Type.YES) {
            dialogAction.withHandler(event -> {
                if (isValid()) {
                    fireCloseAndResultEvents(closeAction);
                }
            });
        } else {
            dialogAction.withHandler(event -> fireCloseAndResultEvents(closeAction));
        }
        return dialogAction;
    }

    protected void fireCloseAndResultEvents(CloseAction closeAction) {
        close(closeAction);

        if (resultHandler != null) {
            resultHandler.accept(new InputDialogResult(getValues(), closeAction));
        }
    }

    /**
     * Event sent to a listener added using {@code withCloseListener()} method of the input dialog builder.
     */
    public static class InputDialogCloseEvent {
        protected CloseAction closeAction;
        protected Map<String, Object> values;

        public InputDialogCloseEvent(Map<String, Object> values, CloseAction closeAction) {
            this.values = values;
            this.closeAction = closeAction;
        }

        /**
         * @return close action
         */
        public CloseAction getCloseAction() {
            return closeAction;
        }

        /**
         * Checks that the dialog was closed with the given {@code outcome}.
         */
        public boolean closedWith(DialogOutcome outcome) {
            return outcome.getCloseAction().equals(closeAction);
        }

        /**
         * Returns values from parameters. Key - parameter id, Value - parameter value.
         *
         * @return values
         */
        public Map<String, Object> getValues() {
            return values;
        }

        /**
         * @param parameterId parameter id
         * @return parameter value
         */
        @SuppressWarnings("unchecked")
        @Nullable
        public <T> T getValue(String parameterId) {
            return (T) values.get(parameterId);
        }

        /**
         * Returns optional value from parameter by its id.
         *
         * @param parameterId parameter id
         * @return optional parameter value
         */
        public <T> Optional<T> getOptional(String parameterId) {
            return Optional.ofNullable(getValue(parameterId));
        }
    }

    /**
     * Describes result of handler that can be used with {@link DialogActions} in the input dialog.
     *
     * @see Dialogs.InputDialogBuilder#withActions(DialogActions, Consumer)
     */
    public static class InputDialogResult {

        public enum ActionType {
            OK, CANCEL, YES, NO
        }

        protected Map<String, Object> values;
        protected CloseAction closeAction;

        public InputDialogResult(Map<String, Object> values, CloseAction closeAction) {
            this.values = values;
            this.closeAction = closeAction;
        }

        /**
         * Returns values from parameters. String - parameter id, Object - parameter value.
         *
         * @return values
         */
        public Map<String, Object> getValues() {
            return values;
        }

        /**
         * @param parameterId parameter id
         * @return parameter value
         */
        @SuppressWarnings("unchecked")
        @Nullable
        public <T> T getValue(String parameterId) {
            return (T) values.get(parameterId);
        }

        /**
         * Returns optional value from parameter by its id.
         *
         * @param parameterId parameter id
         * @return optional parameter value
         */
        public <T> Optional<T> getOptional(String parameterId) {
            return Optional.ofNullable(getValue(parameterId));
        }

        /**
         * @return close action
         * @see #INPUT_DIALOG_OK_ACTION
         * @see #INPUT_DIALOG_CANCEL_ACTION
         * @see #INPUT_DIALOG_YES_ACTION
         * @see #INPUT_DIALOG_NO_ACTION
         */
        public CloseAction getCloseAction() {
            return closeAction;
        }

        /**
         * Returns result action which was clicked in the dialog, e.g. OK, CANCEL, etc.
         *
         * @return dialog result
         */
        public ActionType getCloseActionType() {
            if (closeAction.equals(INPUT_DIALOG_OK_ACTION)) {
                return ActionType.OK;
            } else if (closeAction.equals(INPUT_DIALOG_NO_ACTION)) {
                return ActionType.NO;
            } else if (closeAction.equals(INPUT_DIALOG_YES_ACTION)) {
                return ActionType.YES;
            } else {
                return ActionType.CANCEL;
            }
        }
    }

    /**
     * Describes input dialog validation context.
     */
    public static class ValidationContext {

        protected Map<String, Object> values;
        protected InputDialog source;

        public ValidationContext(Map<String, Object> values, InputDialog source) {
            this.values = values;
            this.source = source;
        }

        /**
         * Returns values from parameters. String - parameter id, Object - parameter value.
         *
         * @return values
         */
        public Map<String, Object> getValues() {
            return values;
        }

        /**
         * @param parameterId parameter id
         * @return parameter value
         */
        @SuppressWarnings("unchecked")
        @Nullable
        public <T> T getValue(String parameterId) {
            return (T) values.get(parameterId);
        }

        /**
         * @param parameterId parameter id
         * @return optional parameter value
         */
        public <T> Optional<T> getOptional(String parameterId) {
            return Optional.ofNullable(getValue(parameterId));
        }

        /**
         * @return input dialog
         */
        public InputDialog getSource() {
            return source;
        }
    }
}
