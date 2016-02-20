/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

/**
 * @author artamonov
 * @version $Id$
 */
public interface PasswordField extends TextInputField, TextInputField.MaxLengthLimited {

    String NAME = "passwordField";

    /**
     * Return autocomplete attribute value to specify saving it in browser.
     */
    boolean isAutocomplete();

    /**
     * Set autocomplete attribute value to specify saving it in browser.
     * False value disables saving passwords in browser.
     */
    void setAutocomplete(Boolean autocomplete);
}