/*
 * Copyright 2017 Henri Kerola / Vaadin
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.haulmont.cuba.web.widgets.client.addons.popupbutton;

import com.vaadin.shared.Connector;
import com.vaadin.shared.annotations.DelegateToWidget;
import com.vaadin.shared.ui.button.ButtonState;

public class PopupButtonState extends ButtonState {

    public boolean popupVisible;
    public Connector popupPositionConnector;

    @DelegateToWidget
    public int direction = 0;
    public boolean buttonClickTogglesPopupVisibility = true;
    public boolean closePopupOnOutsideClick = true;
}
