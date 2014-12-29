/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.cuba.core.entity.AbstractSearchFolder;
import com.haulmont.cuba.core.entity.Folder;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.presentations.Presentations;

import javax.annotation.Nullable;

/**
 * Interface to be implemented by classes with client-specific behaviour that cannot be placed into
 * {@link com.haulmont.cuba.gui.components.filter.FilterDelegate}
 * @author gorbunkov
 * @version $Id$
 */
public interface FilterHelper {
    String NAME = "cuba_filterHelper";

    void setLookupNullSelectionAllowed(LookupField lookupField, boolean value);

    /**
     * Saves a folder to a FoldersPane
     * @return saved folder or null if foldersPane not found
     */
    AbstractSearchFolder saveFolder(AbstractSearchFolder folder);

    void openFolderEditWindow(boolean isAppFolder, AbstractSearchFolder folder, Presentations presentations, Runnable commitHandler);

    boolean isFolderActionsEnabled();

    void initConditionsDragAndDrop(Tree tree, ConditionsTree conditions);

    Object getFoldersPane();

    void removeFolderFromFoldersPane(Folder folder);

    boolean isTableActionsEnabled();
}
