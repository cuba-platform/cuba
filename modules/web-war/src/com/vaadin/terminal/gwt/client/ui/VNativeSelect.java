/*
 * Copyright 2010 IT Mill Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.vaadin.terminal.gwt.client.ui;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.haulmont.cuba.toolkit.gwt.client.TextSelectionManager;
import com.vaadin.terminal.gwt.client.BrowserInfo;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.Util;

import java.util.ArrayList;
import java.util.Iterator;

public class VNativeSelect extends VOptionGroupBase implements Field, TextSelectionManager {

    public static final String CLASSNAME = "v-select";

    protected TooltipListBox select;

    public VNativeSelect() {
        super(new TooltipListBox(false), CLASSNAME);
        select = (TooltipListBox) optionsContainer;
        select.setSelect(this);
        select.setVisibleItemCount(1);
        select.addChangeHandler(this);
        select.setStyleName(CLASSNAME + "-select");

    }

    public boolean allowTextSelection() {
        return true;
    }

    @Override
    protected void buildOptions(UIDL uidl) {
        select.setClient(client);
        select.setEnabled(!isDisabled() && !isReadonly());
        select.clear();
        if (isNullSelectionAllowed() && !isNullSelectionItemAvailable()) {
            // can't unselect last item in singleselect mode
            select.addItem("", null);
        }
        boolean selected = false;
        for (final Iterator i = uidl.getChildIterator(); i.hasNext();) {
            final UIDL optionUidl = (UIDL) i.next();
            select.addItem(optionUidl.getStringAttribute("caption"), optionUidl
                    .getStringAttribute("key"));
            if (optionUidl.hasAttribute("selected")) {
                select.setItemSelected(select.getItemCount() - 1, true);
                selected = true;
            }
        }
        if (!selected && !isNullSelectionAllowed()) {
            // null-select not allowed, but value not selected yet; add null and
            // remove when something is selected
            select.insertItem("", null, 0);
            select.setItemSelected(0, true);
        }
        if (BrowserInfo.get().isIE6()) {
            // lazy size change - IE6 uses naive dropdown that does not have a
            // proper size yet
            Util.notifyParentOfSizeChange(this, true);
        }
    }

    @Override
    protected String[] getSelectedItems() {
        final ArrayList<String> selectedItemKeys = new ArrayList<String>();
        for (int i = 0; i < select.getItemCount(); i++) {
            if (select.isItemSelected(i)) {
                selectedItemKeys.add(select.getValue(i));
            }
        }
        return selectedItemKeys.toArray(new String[selectedItemKeys.size()]);
    }

    @Override
    public void onChange(ChangeEvent event) {

        if (select.isMultipleSelect()) {
            client.updateVariable(id, "selected", getSelectedItems(),
                    isImmediate());
        } else {
            client.updateVariable(id, "selected", new String[] { ""
                    + getSelectedItem() }, isImmediate());
        }
        if (!isNullSelectionAllowed() && "null".equals(select.getValue(0))) {
            // remove temporary empty item
            select.removeItem(0);
        }
    }

    @Override
    public void setHeight(String height) {
        select.setHeight(height);
        super.setHeight(height);
    }

    @Override
    public void setWidth(String width) {
        select.setWidth(width);
        super.setWidth(width);
    }

    @Override
    protected void setTabIndex(int tabIndex) {
        ((TooltipListBox) optionsContainer).setTabIndex(tabIndex);
    }

    public void focus() {
        select.setFocus(true);
    }

}