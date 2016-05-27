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

package com.haulmont.cuba.desktop.sys.validation;

import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public abstract class ValidationAwareAction extends AbstractAction {
    @Override
    public void actionPerformed(final ActionEvent e) {
        final RootPaneContainer window;
        if (e.getSource() instanceof Component) {
            window = DesktopComponentsHelper.getSwingWindow((Component) e.getSource());
        } else {
            window = null;
        }

        ValidationAlertHolder.runIfValid(new Runnable() {
            @Override
            public void run() {
                if (window == null
                        || window.getGlassPane() == null
                        || !window.getGlassPane().isVisible()) {
                    // check modal dialogs on the front of current component
                    actionPerformedAfterValidation(e);
                }
            }
        });
    }

    public abstract void actionPerformedAfterValidation(ActionEvent e);
}