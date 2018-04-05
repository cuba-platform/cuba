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

package com.haulmont.cuba.desktop.gui.components;

import com.google.common.base.Strings;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.DesktopResources;
import com.haulmont.cuba.desktop.sys.vcl.CapsLockChangeHandler;
import com.haulmont.cuba.gui.components.CapsLockIndicator;

import javax.swing.*;
import java.awt.*;

public class DesktopCapsLockIndicator extends DesktopAbstractComponent<JPanel> implements CapsLockIndicator,
        CapsLockChangeHandler {

    protected String capsLockOnMessage = null;
    protected String capsLockOffMessage = null;

    protected JLabel iconLabel;
    protected JLabel messageLabel;

    protected Component gap;

    protected DesktopResources resources = App.getInstance().getResources();

    protected static final String CAPS_LOCK_ON_ICON = "components/capslockindicator/capslock-on.png";

    public DesktopCapsLockIndicator() {
        impl = new JPanel();
        impl.setLayout(new BoxLayout(impl, BoxLayout.X_AXIS));
        impl.setPreferredSize(new Dimension(0, DesktopComponentsHelper.BUTTON_HEIGHT));

        iconLabel = new JLabel();
        iconLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        impl.add(Box.createHorizontalGlue());
        impl.add(iconLabel);

        messageLabel = new JLabel();
        messageLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        impl.add(messageLabel);
        impl.add(Box.createHorizontalGlue());

        gap = Box.createHorizontalStrut(5);
    }

    @Override
    public void setCapsLockOnMessage(String capsLockOnMessage) {
        this.capsLockOnMessage = capsLockOnMessage;
    }

    @Override
    public String getCapsLockOnMessage() {
        return capsLockOnMessage;
    }

    @Override
    public void setCapsLockOffMessage(String capsLockOffMessage) {
        this.capsLockOffMessage = capsLockOffMessage;

        // init default state to false, because we don't know the state of caps lock until user starts to type
        showCapsLockStatus(false);
    }

    @Override
    public String getCapsLockOffMessage() {
        return capsLockOffMessage;
    }

    @Override
    public void showCapsLockStatus(boolean isCapsLock) {
        if (isCapsLock) {
            iconLabel.setIcon(resources.getIcon(CAPS_LOCK_ON_ICON));
            messageLabel.setText(capsLockOnMessage);
            if (!Strings.isNullOrEmpty(capsLockOnMessage)) {
                impl.add(gap, 2);
            } else {
                impl.remove(gap);
            }
        } else {
            iconLabel.setIcon(null);
            messageLabel.setText(capsLockOffMessage);
            impl.remove(gap);
        }
    }
}