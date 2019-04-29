/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.web.gui;

import com.google.common.reflect.TypeToken;
import com.haulmont.chile.core.datatypes.DatatypeRegistry;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.mainwindow.*;
import com.haulmont.cuba.web.gui.components.*;
import com.haulmont.cuba.web.gui.components.mainwindow.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@org.springframework.stereotype.Component(UiComponents.NAME)
public class WebUiComponents implements UiComponents {

    @Inject
    protected ApplicationContext applicationContext;
    @Inject
    protected DatatypeRegistry datatypeRegistry;

    protected Map<String, Class<? extends Component>> classes = new ConcurrentHashMap<>();
    protected Map<Class, String> names = new ConcurrentHashMap<>();

    {
        classes.put(RootWindow.NAME, WebRootWindow.class);
        classes.put(TabWindow.NAME, WebTabWindow.class);
        classes.put(DialogWindow.NAME, WebDialogWindow.class);
        classes.put(Fragment.NAME, WebFragment.class);

        classes.put(HBoxLayout.NAME, WebHBoxLayout.class);
        classes.put(VBoxLayout.NAME, WebVBoxLayout.class);
        classes.put(GridLayout.NAME, WebGridLayout.class);
        classes.put(ScrollBoxLayout.NAME, WebScrollBoxLayout.class);
        classes.put(HtmlBoxLayout.NAME, WebHtmlBoxLayout.class);
        classes.put(FlowBoxLayout.NAME, WebFlowBoxLayout.class);
        classes.put(CssLayout.NAME, WebCssLayout.class);

        classes.put(Button.NAME, WebButton.class);
        classes.put(LinkButton.NAME, WebLinkButton.class);
        classes.put(Label.NAME, WebLabel.class);
        classes.put(Link.NAME, WebLink.class);
        classes.put(CheckBox.NAME, WebCheckBox.class);
        classes.put(GroupBoxLayout.NAME, WebGroupBox.class);
        classes.put(SourceCodeEditor.NAME, WebSourceCodeEditor.class);
        classes.put(TextField.NAME, WebTextField.class);
        classes.put(PasswordField.NAME, WebPasswordField.class);

        classes.put(ResizableTextArea.NAME, WebResizableTextArea.class);
        classes.put(TextArea.NAME, WebTextArea.class);
        classes.put(RichTextArea.NAME, WebRichTextArea.class);
        classes.put(MaskedField.NAME, WebMaskedField.class);

        classes.put(Table.NAME, WebTable.class);
        classes.put(TreeTable.NAME, WebTreeTable.class);
        classes.put(GroupTable.NAME, WebGroupTable.class);
        classes.put(DataGrid.NAME, WebDataGrid.class);
        classes.put(TreeDataGrid.NAME, WebTreeDataGrid.class);
        classes.put(DateField.NAME, WebDateField.class);
        classes.put(TimeField.NAME, WebTimeField.class);
        classes.put(LookupField.NAME, WebLookupField.class);
        classes.put(SearchField.NAME, WebSearchField.class);
        classes.put(PickerField.NAME, WebPickerField.class);
        classes.put(SuggestionField.NAME, WebSuggestionField.class);
        classes.put(SuggestionPickerField.NAME, WebSuggestionPickerField.class);
        classes.put(ColorPicker.NAME, WebColorPicker.class);
        classes.put(LookupPickerField.NAME, WebLookupPickerField.class);
        classes.put(SearchPickerField.NAME, WebSearchPickerField.class);
        classes.put(OptionsGroup.NAME, WebOptionsGroup.class);
        classes.put(CheckBoxGroup.NAME, WebCheckBoxGroup.class);
        classes.put(RadioButtonGroup.NAME, WebRadioButtonGroup.class);
        classes.put(OptionsList.NAME, WebOptionsList.class);
        classes.put(FileUploadField.NAME, WebFileUploadField.class);
        classes.put(FileMultiUploadField.NAME, WebFileMultiUploadField.class);
        classes.put(CurrencyField.NAME, WebCurrencyField.class);
        classes.put(SplitPanel.NAME, WebSplitPanel.class);
        classes.put(Tree.NAME, WebTree.class);
        classes.put(TabSheet.NAME, WebTabSheet.class);
        classes.put(Accordion.NAME, WebAccordion.class);
        classes.put(Calendar.NAME, WebCalendar.class);
        classes.put(Embedded.NAME, WebEmbedded.class);
        classes.put(Image.NAME, WebImage.class);
        classes.put(BrowserFrame.NAME, WebBrowserFrame.class);
        classes.put(Filter.NAME, WebFilter.class);
        classes.put(ButtonsPanel.NAME, WebButtonsPanel.class);
        classes.put(PopupButton.NAME, WebPopupButton.class);
        classes.put(PopupView.NAME, WebPopupView.class);

        classes.put(FieldGroup.NAME, WebFieldGroup.class);
        classes.put(TokenList.NAME, WebTokenList.class);
        classes.put(TwinColumn.NAME, WebTwinColumn.class);
        classes.put(ProgressBar.NAME, WebProgressBar.class);
        classes.put(RowsCount.NAME, WebRowsCount.class);
        classes.put(RelatedEntities.NAME, WebRelatedEntities.class);
        classes.put(BulkEditor.NAME, WebBulkEditor.class);
        classes.put(DatePicker.NAME, WebDatePicker.class);
        classes.put(ListEditor.NAME, WebListEditor.class);
        classes.put(CapsLockIndicator.NAME, WebCapsLockIndicator.class);

        classes.put(Form.NAME, WebForm.class);

        classes.put(EntityLinkField.NAME, WebEntityLinkField.class);

        /* Main window components */

        classes.put(AppMenu.NAME, WebAppMenu.class);
        classes.put(AppWorkArea.NAME, WebAppWorkArea.class);
        classes.put(UserActionsButton.NAME, WebUserActionsButton.class);
        classes.put(LogoutButton.NAME, WebLogoutButton.class);
        classes.put(NewWindowButton.NAME, WebNewWindowButton.class);
        classes.put(UserIndicator.NAME, WebUserIndicator.class);
        classes.put(FoldersPane.NAME, WebFoldersPane.class);
        classes.put(FtsField.NAME, WebFtsField.class);
        classes.put(TimeZoneIndicator.NAME, WebTimeZoneIndicator.class);
        classes.put(SideMenu.NAME, WebSideMenu.class);

        /* Misc */

        classes.put(Timer.NAME, WebTimer.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Component> T create(String name) {
        Class<? extends Component> componentClass = classes.get(name);
        if (componentClass == null) {
            throw new IllegalStateException(String.format("Can't find component class for '%s'", name));
        }

        Constructor<? extends Component> constructor;
        try {
            constructor = componentClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(String.format("Unable to get constructor for '%s' component", name), e);
        }

        try {
            Component instance = constructor.newInstance();
            autowireContext(instance);
            return (T) instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(String.format("Error creating the '%s' component instance", name), e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Component> T create(Class<T> type) {
        String name = names.get(type);
        if (name == null) {
            java.lang.reflect.Field nameField;
            try {
                nameField = type.getField("NAME");
                name = (String) nameField.get(null);
            } catch (NoSuchFieldException | IllegalAccessException ignore) {
            }
            if (name == null)
                throw new DevelopmentException(String.format("Class '%s' doesn't have NAME field", type.getName()));
            else
                names.put(type, name);
        }
        return (T) create(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Component> T create(TypeToken<T> type) {
        T t = (T) create((Class) type.getRawType());
        if (t instanceof HasDatatype) {
            Type[] actualTypeArguments = ((ParameterizedType) type.getType()).getActualTypeArguments();
            if (actualTypeArguments.length == 1 && actualTypeArguments[0] instanceof Class) {
                Class actualTypeArgument = (Class) actualTypeArguments[0];

                ((HasDatatype) t).setDatatype(datatypeRegistry.get(actualTypeArgument));
            }
        }
        return t;
    }

    protected void autowireContext(Component instance) {
        AutowireCapableBeanFactory autowireBeanFactory = applicationContext.getAutowireCapableBeanFactory();
        autowireBeanFactory.autowireBean(instance);

        if (instance instanceof ApplicationContextAware) {
            ((ApplicationContextAware) instance).setApplicationContext(applicationContext);
        }

        if (instance instanceof InitializingBean) {
            try {
                ((InitializingBean) instance).afterPropertiesSet();
            } catch (Exception e) {
                throw new RuntimeException(
                        "Unable to initialize UI Component - calling afterPropertiesSet for " +
                                instance.getClass(), e);
            }
        }
    }

    public void register(String name, Class<? extends Component> componentClass) {
        classes.put(name, componentClass);
    }
}