/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.WindowManager;

import java.util.List;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public interface BulkEditor extends Component, Component.HasCaption, Component.BelongToFrame, Component.HasIcon {

    String NAME = "bulkEditor";
    String PERMISSION = "cuba.gui.bulkEdit";

    WindowManager.OpenType getOpenType();
    void setOpenType(WindowManager.OpenType openType);

    String getExcludePropertiesRegex();
    void setExcludePropertiesRegex(String excludeRegex);

    ListComponent getListComponent();
    void setListComponent(ListComponent listComponent);

    Map<String, Field.Validator> getFieldValidators();
    void setFieldValidators(Map <String, Field.Validator> fieldValidators);

    List<Field.Validator> getModelValidators();
    void setModelValidators(List<Field.Validator> modelValidators);
}