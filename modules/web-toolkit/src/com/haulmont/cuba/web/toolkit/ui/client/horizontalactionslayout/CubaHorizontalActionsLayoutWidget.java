/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.horizontalactionslayout;

import com.haulmont.cuba.web.toolkit.ui.client.orderedactionslayout.CubaOrderedActionsLayoutWidget;

/**
 * @author devyatkin
 * @version $Id$
 */
public class CubaHorizontalActionsLayoutWidget extends CubaOrderedActionsLayoutWidget {

    public static final String CLASSNAME = "v-horizontallayout";

    public CubaHorizontalActionsLayoutWidget(){
        super(CLASSNAME, false);
    }
}
