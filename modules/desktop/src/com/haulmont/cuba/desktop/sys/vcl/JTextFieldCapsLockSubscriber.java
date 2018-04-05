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

package com.haulmont.cuba.desktop.sys.vcl;

import com.haulmont.cuba.gui.components.CapsLockIndicator;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class JTextFieldCapsLockSubscriber {

    protected Map<JTextField, CapsLockChangeListener> listenerMap = new HashMap<>();

    public void subscribe(JTextField field, CapsLockIndicator capsLockIndicator) {
        CapsLockChangeListener listener = createTextFieldListener(capsLockIndicator);
        field.addKeyListener(listener);
        field.addFocusListener(listener);

        listenerMap.put(field, listener);
    }

    public void unsubscribe(JTextField unsubscribeField) {
        for (JTextField field : listenerMap.keySet()) {
            if (field.equals(unsubscribeField)) {
                field.removeKeyListener(listenerMap.get(field));
                field.removeFocusListener(listenerMap.get(field));
                listenerMap.remove(field);
                break;
            }
        }
    }

    protected CapsLockChangeListener createTextFieldListener(CapsLockIndicator capsLockIndicator) {
        return new CapsLockChangeListener(capsLockIndicator);
    }

    protected class CapsLockChangeListener implements FocusListener, KeyListener {

        protected CapsLockIndicator capsLockIndicator;
        protected Boolean capsLock;

        public CapsLockChangeListener(CapsLockIndicator capsLockIndicator) {
            this.capsLockIndicator = capsLockIndicator;
        }

        @Override
        public void focusGained(FocusEvent e) {
        }

        @Override
        public void focusLost(FocusEvent e) {
            capsLock = null;
            showCapsLockStatus(false);
        }

        @Override
        public void keyTyped(KeyEvent e) {
            char charCode = e.getKeyChar();

            if (charCode == 0) {
                return;
            }

            if (Character.toLowerCase(charCode) == Character.toUpperCase(charCode)) {
                return;
            }

            capsLock = (Character.toLowerCase(charCode) == charCode && e.isShiftDown())
                    || (Character.toUpperCase(charCode) == charCode && !e.isShiftDown());

            showCapsLockStatus(capsLock);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 20 && capsLock != null) {
                capsLock = !capsLock;

                showCapsLockStatus(capsLock);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        protected void showCapsLockStatus(boolean isCapsLock) {
            if (capsLockIndicator instanceof CapsLockChangeHandler) {
                ((CapsLockChangeHandler) capsLockIndicator).showCapsLockStatus(isCapsLock);
            }
        }
    }
}