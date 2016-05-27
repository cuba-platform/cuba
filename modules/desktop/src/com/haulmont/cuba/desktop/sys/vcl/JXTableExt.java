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

package com.haulmont.cuba.desktop.sys.vcl;

import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

public class JXTableExt extends JXTable implements FocusableTable {

    protected TableFocusManager focusManager = new TableFocusManager(this);

    @Override
    protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
        // ctrl shift keys are not handled by table
        if (focusManager.isDisabledKeys(e)) {
            return false;
        }

        if (focusManager.processKeyBinding(ks, e, condition, pressed))
            return true;
        else
            return super.processKeyBinding(ks, e, condition, pressed);
    }

    @Override
    protected void processFocusEvent(FocusEvent e) {
        focusManager.processFocusEvent(e);

        super.processFocusEvent(e);
    }

    @Override
    public TableFocusManager getFocusManager() {
        return focusManager;
    }

    @Override
    public void setFocusManager(TableFocusManager focusManager) {
        this.focusManager = focusManager;
    }
}