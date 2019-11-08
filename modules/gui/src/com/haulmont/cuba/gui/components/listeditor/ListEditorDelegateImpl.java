/*
 * Copyright (c) 2008-2016 Haulmont.
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

package com.haulmont.cuba.gui.components.listeditor;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.haulmont.bali.events.EventHub;
import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.datatypes.DatatypeRegistry;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.data.Options;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.screen.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component(ListEditorDelegate.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ListEditorDelegateImpl<V> implements ListEditorDelegate<V> {
    /* Beans */
    protected UiComponents uiComponents;

    protected Field actualField;

    protected List<V> value;

    protected Options<V> options;
    protected Function<? super V, String> optionCaptionProvider;

    protected Supplier<Map<String, Object>> editorParamsSupplier;
    protected String editorWindowId = "list-editor-popup";

    protected ListEditor.ItemType itemType;
    protected String entityName;
    protected String lookupScreen;
    protected boolean useLookupField;

    protected Map<String, V> optionsMap; // todo where it is written ?

    protected String entityJoinClause;
    protected String entityWhereClause;
    protected Class<? extends Enum> enumClass;
    protected TimeZone timeZone;
    protected List<Consumer<? super V>> validators;

    protected TextField<String> displayValuesField;
    protected HBoxLayout layout;
    protected Button clearBtn;

    protected boolean displayDescription = true;

    protected boolean editable = true;

    private EventHub eventHub;

    @Inject
    protected DatatypeRegistry datatypeRegistry;

    @Inject
    protected Logger log;

    @Inject
    public void setUiComponents(UiComponents uiComponents) {
        this.uiComponents = uiComponents;
    }

    @PostConstruct
    public void init() {
        layout = uiComponents.create(HBoxLayout.class);
        layout.setStyleName("c-listeditor-layout");
        layout.setWidth("100%");

        displayValuesField = uiComponents.create(TextField.NAME);
        displayValuesField.setStyleName("c-listeditor-text");
        displayValuesField.setEditable(false);

        initDisplayValuesFieldValueChangeListener();

        Button openEditorBtn = uiComponents.create(Button.class);
        openEditorBtn.setIconFromSet(CubaIcon.PICKERFIELD_LOOKUP);
        openEditorBtn.setStyleName("c-listeditor-button");
        openEditorBtn.setCaption("");
        openEditorBtn.addClickListener(e -> openEditor());

        layout.add(displayValuesField);
        layout.add(openEditorBtn);
        layout.expand(displayValuesField);
    }

    /**
     * displayValuesField may be editable, e.g. for IN condition in the Filter component. For such cases,
     * we should parse the string value and transform it to values list
     */
    protected void initDisplayValuesFieldValueChangeListener() {
        displayValuesField.addValueChangeListener(valueChangeEvent -> {
            //only handle cases when user directly modified the field value
            if (valueChangeEvent.isUserOriginated()) {
                String strValue = valueChangeEvent.getValue();
                List<Object> values = new ArrayList<>();
                if (!Strings.isNullOrEmpty(strValue)) {
                    List<String> parts = Splitter.on(",").trimResults().splitToList(strValue);
                    parts.forEach(value -> {
                        Object typedValue = null;
                        try {
                            switch (itemType) {
                                case STRING:
                                    typedValue = value;
                                    break;
                                case INTEGER:
                                    typedValue = datatypeRegistry.getNN(Integer.class).parse(value);
                                    break;
                                case BIGDECIMAL:
                                    typedValue = datatypeRegistry.getNN(BigDecimal.class).parse(value);
                                    break;
                                case LONG:
                                    typedValue = datatypeRegistry.getNN(Long.class).parse(value);
                                    break;
                                case DOUBLE:
                                    typedValue = datatypeRegistry.getNN(Double.class).parse(value);
                                    break;
                                default:
                                    typedValue = null;
                            }
                        } catch (ParseException e) {
                            log.error("Invalid value {}", value);
                        }
                        if (typedValue != null) {
                            values.add(typedValue);
                        }
                    });
                }
                actualField.setValue(values);
            }
        });
    }

    protected void openEditor() {
        Map<String, Object> params = new HashMap<>();
        params.put("itemType", itemType);
        params.put("entityName", entityName);
        params.put("useLookupField", useLookupField);
        params.put("options", options);
        params.put("optionCaptionProvider", optionCaptionProvider);
        params.put("enumClass", enumClass);
        params.put("lookupScreen", lookupScreen);
        params.put("entityJoinClause", entityJoinClause);
        params.put("entityWhereClause", entityWhereClause);
        params.put("values", getValue());
        params.put("editable", editable);
        params.put("timeZone", timeZone);
        params.put("validators", validators);

        if (editorParamsSupplier != null) {
            Map<String, Object> additionalParams = getEditorParamsSupplier().get();
            if (additionalParams != null) {
                params.putAll(additionalParams);
            }
        }

        ScreenContext screenContext = ComponentsHelper.getScreenContext(actualField);

        Screen screen = screenContext.getScreens().create(editorWindowId, OpenMode.DIALOG, new MapScreenOptions(params));
        screen.addAfterCloseListener(event -> {
            CloseAction closeAction = event.getCloseAction();
            if (closeAction instanceof StandardCloseAction) {
                String actionId = ((StandardCloseAction) closeAction).getActionId();
                ListEditorWindowController listEditorWindow = (ListEditorWindowController) screen;
                if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                    //noinspection unchecked
                    actualField.setValue((listEditorWindow).getValue());
                }

                ListEditor.EditorCloseEvent editorCloseEvent =
                        new ListEditor.EditorCloseEvent(actionId, listEditorWindow);
                getEventHub().publish(ListEditor.EditorCloseEvent.class, editorCloseEvent);
            }
        });
        screen.show();
    }

    protected EventHub getEventHub() {
        if (eventHub == null) {
            eventHub = new EventHub();
        }
        return eventHub;
    }

    @Override
    public void setActualField(Field actualField) {
        this.actualField = actualField;
    }

    @Override
    public HBoxLayout getLayout() {
        return layout;
    }

    @Override
    public List<V> getValue() {
        return value;
    }

    @Override
    public void setValue(List<V> newValue) {
        this.value = newValue;
        String strValue = null;
        if (newValue != null) {
            List<String> captions;
            if (optionsMap != null) {
                captions = new ArrayList<>();
                for (Map.Entry<String, V> entry : optionsMap.entrySet()) {
                    if (newValue.indexOf(entry.getValue()) != -1) {
                        captions.add(entry.getKey());
                    }
                }
            } else {
                //noinspection unchecked
                captions = newValue.stream()
                        .map(o -> ListEditorHelper.getValueCaption(o, itemType, timeZone, (Function<Object, String>) optionCaptionProvider))
                        .collect(Collectors.toList());
            }
            strValue = Joiner.on(", ").join(captions);
        }
        displayValuesField.setValue(strValue);
        if (displayDescription) {
            displayValuesField.setDescription(strValue);
        }
    }

    @Override
    public ListEditor.ItemType getItemType() {
        return itemType;
    }

    @Override
    public void setItemType(ListEditor.ItemType itemType) {
        this.itemType = itemType;
    }

    @Override
    public String getEntityName() {
        return entityName;
    }

    @Override
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    @Override
    public String getLookupScreen() {
        return lookupScreen;
    }

    @Override
    public void setLookupScreen(String lookupScreen) {
        this.lookupScreen = lookupScreen;
    }

    @Override
    public boolean isUseLookupField() {
        return useLookupField;
    }

    @Override
    public void setUseLookupField(boolean useLookupField) {
        this.useLookupField = useLookupField;
    }

    @Override
    public Class<? extends Enum> getEnumClass() {
        return enumClass;
    }

    @Override
    public void setEnumClass(Class<? extends Enum> enumClass) {
        this.enumClass = enumClass;
    }

    public boolean isDisplayDescription() {
        return displayDescription;
    }

    @Override
    public void setDisplayDescription(boolean displayDescription) {
        this.displayDescription = displayDescription;
    }

    @Override
    public String getEntityJoinClause() {
        return entityJoinClause;
    }

    @Override
    public void setEntityJoinClause(String entityJoinClause) {
        this.entityJoinClause = entityJoinClause;
    }

    @Override
    public String getEntityWhereClause() {
        return entityWhereClause;
    }

    @Override
    public void setEntityWhereClause(String entityWhereClause) {
        this.entityWhereClause = entityWhereClause;
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
        if (clearBtn != null) {
            clearBtn.setEnabled(editable);
        }
    }

    @Override
    public void setClearButtonVisible(boolean visible) {
        if (visible && (layout.indexOf(clearBtn) == -1)) {
            addClearBtn();
        }
        if (!visible && (clearBtn != null)) {
            layout.remove(clearBtn);
        }
    }

    @Override
    public boolean isClearButtonVisible() {
        return layout.indexOf(clearBtn) != -1;
    }

    @Override
    public TextField getDisplayValuesField() {
        return displayValuesField;
    }

    protected void addClearBtn() {
        clearBtn = uiComponents.create(Button.class);
        clearBtn.setIconFromSet(CubaIcon.PICKERFIELD_CLEAR);
        clearBtn.setStyleName("c-listeditor-button");
        clearBtn.setCaption("");
        clearBtn.setAction(new BaseAction("clear")
                .withCaption("Clear")
                .withHandler(event ->
                        actualField.setValue(null)
                ));

        layout.add(clearBtn);
    }

    @Override
    public void setEditorWindowId(String windowId) {
        editorWindowId = windowId;
    }

    @Override
    public String getEditorWindowId() {
        return editorWindowId;
    }

    @Override
    public Subscription addEditorCloseListener(Consumer<ListEditor.EditorCloseEvent> listener) {
        return getEventHub().subscribe(ListEditor.EditorCloseEvent.class, listener);
    }

    @Override
    public void removeEditorCloseListener(Consumer<ListEditor.EditorCloseEvent> listener) {
        getEventHub().unsubscribe(ListEditor.EditorCloseEvent.class, listener);
    }

    @Override
    public void setEditorParamsSupplier(Supplier<Map<String, Object>> paramsSupplier) {
        editorParamsSupplier = paramsSupplier;
    }

    @Override
    public Supplier<Map<String, Object>> getEditorParamsSupplier() {
        return editorParamsSupplier;
    }

    @Override
    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public TimeZone getTimeZone() {
        return timeZone;
    }

    @Override
    public void setOptions(Options<V> options) {
        this.options = options;
    }

    @Override
    public Options<V> getOptions() {
        return options;
    }

    @Override
    public void setOptionCaptionProvider(Function<? super V, String> optionCaptionProvider) {
        this.optionCaptionProvider = optionCaptionProvider;
    }

    @Override
    public Function<? super V, String> getOptionCaptionProvider() {
        return optionCaptionProvider;
    }

    @Override
    public void addListItemValidator(Consumer<? super V> validator) {
        if (validators == null) {
            validators = new ArrayList<>();
        }
        validators.add(validator);
    }

    @Override
    public List<Consumer<? super V>> getListItemValidators() {
        return validators;
    }

    @Override
    public boolean isDisplayValuesFieldEditable() {
        return displayValuesField.isEditable();
    }

    @Override
    public void setDisplayValuesFieldEditable(boolean displayValuesFieldEditable) {
        displayValuesField.setEditable(displayValuesFieldEditable);
    }
}