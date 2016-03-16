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
 *
 */

package com.haulmont.cuba.desktop.gui;

import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.desktop.gui.components.*;
import com.haulmont.cuba.gui.ComponentPalette;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 */
@org.springframework.stereotype.Component(ComponentsFactory.NAME)
public class DesktopComponentsFactory implements ComponentsFactory {

    private static Map<String, Class<? extends Component>> classes = new HashMap<>();

    private static Map<Class, String> names = new ConcurrentHashMap<>();

    static {
        classes.put(Window.NAME, DesktopWindow.class);
        classes.put(Window.Editor.NAME, DesktopWindow.Editor.class);
        classes.put(Window.Lookup.NAME, DesktopWindow.Lookup.class);

        classes.put(Frame.NAME, DesktopFrame.class);
        classes.put(HBoxLayout.NAME, DesktopHBox.class);
        classes.put(VBoxLayout.NAME, DesktopVBox.class);
        classes.put(GridLayout.NAME, DesktopGridLayout.class);
        classes.put(ScrollBoxLayout.NAME, DesktopScrollBoxLayout.class);
        classes.put(SplitPanel.NAME, DesktopSplitPanel.class);

        classes.put(Button.NAME, DesktopButton.class);
        classes.put(LinkButton.NAME, DesktopLinkButton.class);
        classes.put(Label.NAME, DesktopLabel.class);
        classes.put(CheckBox.NAME, DesktopCheckBox.class);
        classes.put(Link.NAME, DesktopLink.class);

        //Use resizable text area instead of text field
        classes.put(ResizableTextArea.NAME, DesktopResizableTextArea.class);
        classes.put(SourceCodeEditor.NAME, DesktopSourceCodeEditor.class);
        classes.put(PasswordField.NAME, DesktopPasswordField.class);
        classes.put(TextField.NAME, DesktopTextField.class);

        classes.put(DateField.NAME, DesktopDateField.class);
        classes.put(Table.NAME, DesktopTable.class);
        classes.put(GroupTable.NAME, DesktopGroupTable.class);
        classes.put(Tree.NAME, DesktopTree.class);
        classes.put(TreeTable.NAME, DesktopTreeTable.class);
        classes.put(ButtonsPanel.NAME, DesktopButtonsPanel.class);
        classes.put(Filter.NAME, DesktopFilter.class);
        classes.put(FieldGroup.NAME, DesktopFieldGroup.class);
        classes.put(RowsCount.NAME, DesktopRowsCount.class);
        classes.put(PopupButton.NAME, DesktopPopupButton.class);
        classes.put(LookupField.NAME, DesktopLookupField.class);
        classes.put(PickerField.NAME, DesktopPickerField.class);
        classes.put(LookupPickerField.NAME, DesktopLookupPickerField.class);
        classes.put(MaskedField.NAME, DesktopMaskedField.class);

        classes.put(SearchField.NAME, DesktopSearchField.class);
        classes.put(SearchPickerField.NAME, DesktopSearchPickerField.class);

        classes.put(OptionsGroup.NAME, DesktopOptionsGroup.class);
        classes.put(FileUploadField.NAME, DesktopFileUploadField.class);
        classes.put(FileMultiUploadField.NAME, DesktopFileMultiUploadField.class);
        classes.put(TabSheet.NAME, DesktopTabSheet.class);
        classes.put(Embedded.NAME, DesktopEmbedded.class);
        classes.put(WidgetsTree.NAME, DesktopWidgetsTree.class);
        classes.put(GroupBoxLayout.NAME, DesktopGroupBox.class);
        classes.put(ProgressBar.NAME, DesktopProgressBar.class);
        classes.put(TimeField.NAME, DesktopTimeField.class);
        classes.put(TokenList.NAME, DesktopTokenList.class);
        classes.put(RelatedEntities.NAME, DesktopRelatedEntities.class);
        classes.put(BulkEditor.NAME, DesktopBulkEditor.class);
    }

    public static void registerComponent(String element, Class<? extends Component> componentClass) {
        classes.put(element, componentClass);
    }

    public static void registerComponents(ComponentPalette... palettes) {
        for (ComponentPalette palette : palettes) {
            Map<String, Class<? extends Component>> loaders = palette.getComponents();
            for (Map.Entry<String, Class<? extends Component>> loaderEntry : loaders.entrySet()) {
                classes.put(loaderEntry.getKey(), loaderEntry.getValue());
            }
        }
    }

    @Override
    public Component createComponent(String name) {
        final Class<? extends Component> componentClass = classes.get(name);
        if (componentClass == null) {
            throw new IllegalStateException(String.format("Can't find component class for '%s'", name));
        }

        try {
            return componentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Error creating the '" + name + "' component instance", e);
        }
    }

    @Override
    public <T extends Component> T createComponent(Class<T> type) {
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
        return type.cast(createComponent(name));
    }

    @Override
    public Timer createTimer() {
        return new DesktopTimer();
    }
}