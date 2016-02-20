/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.passwordfield;

import com.vaadin.client.ui.VPasswordField;

/**
 * @author petunin
 */
public class CubaPasswordFieldWidget extends VPasswordField {

    public void setAutocomplete(boolean autocomplete) {
        if (autocomplete) {
            getElement().removeAttribute("autocomplete");
        } else {
            getElement().setAttribute("autocomplete", "off");
        }
    }

}
