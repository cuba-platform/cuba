/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
