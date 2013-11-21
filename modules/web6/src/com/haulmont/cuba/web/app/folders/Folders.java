/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.folders;

import com.haulmont.cuba.core.entity.AbstractSearchFolder;

/**
 * @author gorbunkov
 * @version $Id$
 */
public interface Folders {
    String NAME = "cuba_Folders";

    void openFolder(AbstractSearchFolder folder);
}
