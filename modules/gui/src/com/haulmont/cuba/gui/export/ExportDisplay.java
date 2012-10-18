/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.export;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.gui.components.IFrame;

import javax.annotation.Nullable;

/**
 * Generic interace to show data exported from the system.
 * <br>Use one of client-specific implementations.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface ExportDisplay {
    String NAME = "cuba_ExportDisplay";

    void show(ExportDataProvider dataProvider, String resourceName, @Nullable ExportFormat format);

    void show(ExportDataProvider dataProvider, String resourceName);

    void show(FileDescriptor fileDescriptor, @Nullable ExportFormat format);

    void setFrame(IFrame frame);

    void show(FileDescriptor fileDescriptor);
}
