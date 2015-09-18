/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.export;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.gui.components.Frame;

import javax.annotation.Nullable;

/**
 * Generic interace to show data exported from the system.
 *
 * <p/> Use client-specific implementation obtained by
 * {@link com.haulmont.cuba.gui.AppConfig#createExportDisplay(Frame)} or by
 * injection into a screen controller.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface ExportDisplay {

    String NAME = "cuba_ExportDisplay";

    /**
     * Export an arbitrary resource defined by a ExportDataProvider.
     *
     * @param dataProvider resource provider
     * @param resourceName resource name
     * @param format       export format, can be null
     */
    void show(ExportDataProvider dataProvider, String resourceName, @Nullable ExportFormat format);

    /**
     * Export an arbitrary resource defined by a ExportDataProvider.
     *
     * @param dataProvider resource provider
     * @param resourceName resource name
     */
    void show(ExportDataProvider dataProvider, String resourceName);

    /**
     * Export a file from file storage.
     *
     * @param fileDescriptor file descriptor
     * @param format         export format, can be null
     */
    void show(FileDescriptor fileDescriptor, @Nullable ExportFormat format);

    /**
     * Export a file from file storage.
     *
     * @param fileDescriptor file descriptor
     */
    void show(FileDescriptor fileDescriptor);

    /** INTERNAL. Don't call from application code. */
    void setFrame(Frame frame);
}
