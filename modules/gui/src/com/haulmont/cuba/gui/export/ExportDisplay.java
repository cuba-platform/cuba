/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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
