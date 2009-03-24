/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Gorodnov
 * Created: 06.03.2009 17:33:50
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.data;

import com.itmill.toolkit.data.Container;

public interface TreeTableContainer extends Container.Hierarchical {

    boolean hasCaption(Object itemId);

    String getCaption(Object itemId);

    void setCaption(Object itemId, String caption);

}
