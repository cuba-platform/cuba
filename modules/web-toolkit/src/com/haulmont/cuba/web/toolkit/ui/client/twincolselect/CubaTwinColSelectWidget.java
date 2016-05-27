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

package com.haulmont.cuba.web.toolkit.ui.client.twincolselect;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.vaadin.client.UIDL;
import com.vaadin.client.ui.VButton;
import com.vaadin.client.ui.VTwinColSelect;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CubaTwinColSelectWidget extends VTwinColSelect {

    protected boolean addAllBtnEnabled;

    protected VButton addAll;

    protected VButton removeAll;

    protected HandlerRegistration addAllHandlerRegistration;

    protected HandlerRegistration removeAllHandlerRegistration;

    public CubaTwinColSelectWidget() {
        add.setText(">");
        add.addStyleName("add");
        remove.setText("<");
        remove.addStyleName("remove");
    }

    @Override
    protected DoubleClickListBox createOptionsBox() {
        return new CubaDoubleClickListBox();
    }

    @Override
    protected DoubleClickListBox createSelectionsBox() {
        return new CubaDoubleClickListBox();
    }

    @Override
    public void buildOptions(UIDL uidl) {
        int optionsSelectedIndex = options.getSelectedIndex();
        int selectionsSelectedIndex = selections.getSelectedIndex();
        options.clear();
        selections.clear();

        int selectedOptions = 0;
        int availableOptions = 0;

        for (final Iterator<?> i = uidl.getChildIterator(); i.hasNext(); ) {
            final UIDL optionUidl = (UIDL) i.next();
            if (optionUidl.hasAttribute("selected")) {
                selections.addItem(optionUidl.getStringAttribute("caption"),
                        optionUidl.getStringAttribute("key"));
                if (optionUidl.hasAttribute("style")) {
                    CubaDoubleClickListBox cubaSelections = (CubaDoubleClickListBox) selections;
                    cubaSelections.setOptionClassName(selectedOptions, optionUidl.getStringAttribute("style"));

                }
                selectedOptions++;
            } else {
                options.addItem(optionUidl.getStringAttribute("caption"),
                        optionUidl.getStringAttribute("key"));
                if (optionUidl.hasAttribute("style")) {
                    CubaDoubleClickListBox cubaOptions = (CubaDoubleClickListBox) options;
                    cubaOptions.setOptionClassName(availableOptions, optionUidl.getStringAttribute("style"));

                }
                availableOptions++;
            }
        }

        if (getRows() > 0) {
            options.setVisibleItemCount(getRows());
            selections.setVisibleItemCount(getRows());

        }

        setSelectedIndex(options, optionsSelectedIndex);
        setSelectedIndex(selections, selectionsSelectedIndex);
    }

    protected void setSelectedIndex(ListBox listBox, int index) {
        if (listBox.getItemCount() > 0 && index >= 0) {
            listBox.setSelectedIndex(index);
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        super.onClick(event);
        if (addAllBtnEnabled) {
            if (event.getSource() == addAll) {
                addAll();
            } else if (event.getSource() == removeAll) {
                removeAll();
            }
        }
    }

    private Set<String> moveAllItems(ListBox source, ListBox target) {
        final Set<String> movedItems = new HashSet<String>();
        int size = source.getItemCount();
        for (int i = 0; i < size; i++) {
            movedItems.add(source.getValue(i));
            final String text = source.getItemText(i);
            final String value = source.getValue(i);
            target.addItem(text, value);
            target.setItemSelected(target.getItemCount() - 1, true);
        }
        target.setFocus(true);
        if (source.getItemCount() > 0) {
            target.setSelectedIndex(0);
        }
        source.clear();
        return movedItems;
    }

    protected void addAll() {
        Set<String> movedItems = moveAllItems(options, selections);
        selectedKeys.addAll(movedItems);

        client.updateVariable(paintableId, "selected",
                selectedKeys.toArray(new String[selectedKeys.size()]),
                isImmediate());
    }

    protected void removeAll() {
        moveAllItems(selections, options);
        selectedKeys.clear();
        client.updateVariable(paintableId, "selected",
                selectedKeys.toArray(new String[selectedKeys.size()]),
                isImmediate());
    }

    public boolean isAddAllBtnEnabled() {
        return addAllBtnEnabled;
    }

    public void setAddAllBtnEnabled(boolean addAllBtnEnabled) {
        if (addAllBtnEnabled != this.addAllBtnEnabled) {
            this.addAllBtnEnabled = addAllBtnEnabled;
            if (addAllBtnEnabled) {
                enableAddAllBtn();
            } else {
                disableAddAllBtn();
            }
        }
    }

    protected void enableAddAllBtn() {
        HTML br1 = new HTML("<span/>");
        br1.setStyleName(CLASSNAME + "-deco");
        buttons.add(br1);
        buttons.insert(br1, buttons.getWidgetIndex(add) + 1);
        addAll = new VButton();
        addAll.setText(">>");
        addAll.addStyleName("addAll");
        addAllHandlerRegistration = addAll.addClickHandler(this);
        buttons.insert(addAll, buttons.getWidgetIndex(br1) + 1);

        HTML br2 = new HTML("<span/>");
        br2.setStyleName(CLASSNAME + "-deco");
        buttons.add(br2);

        removeAll = new VButton();
        removeAll.setText("<<");
        removeAll.addStyleName("removeAll");
        removeAllHandlerRegistration = removeAll.addClickHandler(this);
        buttons.add(removeAll);
    }

    protected void disableAddAllBtn() {
        addAll.removeFromParent();
        addAllHandlerRegistration.removeHandler();
        addAll = null;

        removeAll.removeFromParent();
        removeAllHandlerRegistration.removeHandler();
        removeAll = null;
    }

    public class CubaDoubleClickListBox extends DoubleClickListBox {
        public void setOptionClassName(int optionIndex, String className) {
            assert optionIndex >= 0 && options.getItemCount() > optionIndex;
            SelectElement select = getElement().cast();
            Element elem = select.getOptions().getItem(optionIndex);
            elem.addClassName(className);
        }
    }
}