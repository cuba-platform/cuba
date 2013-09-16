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
