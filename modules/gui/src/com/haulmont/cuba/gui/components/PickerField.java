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

public interface PickerField extends Field {
    CaptionMode getCaptionMode();
    void setCaptionMode(CaptionMode captionMode);

    String getCaptionProperty();
    void setCaptionProperty(String captionProperty);

    MetaClass getMetaClass();
    void setMetaClass(MetaClass metaClass);

    void setPickerButtonCaption(String caption);

    void setPickerButtonIcon(String iconName);

    void setClearButtonCaption(String caption);

    void setClearButtonIcon(String iconName);
}
