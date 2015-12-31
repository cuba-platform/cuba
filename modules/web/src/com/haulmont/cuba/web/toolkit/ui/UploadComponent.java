/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.ui.Component;

/**
 * @author gorelov
 * @version $Id$
 */
public interface UploadComponent extends Component {
    String getAccept();
    void setAccept(String accept);

    void setDescription(String description);
}
