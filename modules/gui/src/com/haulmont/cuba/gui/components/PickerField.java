/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 05.03.2009 12:31:23
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.gui.WindowManager;

public interface PickerField extends Field {

    String NAME = "pickerField";

    CaptionMode getCaptionMode();
    void setCaptionMode(CaptionMode captionMode);

    String getCaptionProperty();
    void setCaptionProperty(String captionProperty);

    MetaClass getMetaClass();
    void setMetaClass(MetaClass metaClass);

    String getLookupScreen();
    void setLookupScreen(String lookupScreen);

    WindowManager.OpenType getLookupScreenOpenType();
    void setLookupScreenOpenType(WindowManager.OpenType lookupScreenOpenType);

    void setPickerButtonCaption(String caption);

    void setPickerButtonIcon(String iconName);

    void setClearButtonCaption(String caption);

    void setClearButtonIcon(String iconName);

    ValueProvider getValueProvider();
    void setValueProvider(ValueProvider valueProvider);
}
