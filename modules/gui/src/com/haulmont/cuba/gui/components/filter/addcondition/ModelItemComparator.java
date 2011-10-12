/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components.filter.addcondition;

import java.util.Comparator;

/**
* <p>$Id$</p>
*
* @author krivopustov
*/
public class ModelItemComparator implements Comparator<ModelItem> {
    @Override
    public int compare(ModelItem mi1, ModelItem mi2) {
        return mi1.getCaption().compareTo(mi2.getCaption());
    }
}
