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
package com.haulmont.cuba.gui.app.core.file;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import java.text.NumberFormat;

/**
 */
public class FileDownloadHelper {

    public static String makeLink(FileDescriptor fd, boolean newWindow, boolean attachment) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href=\"");
        sb.append(makeUrl(fd, attachment));
        sb.append("\"");
        if (newWindow)
            sb.append(" target=\"_blank\"");
        sb.append(">");
        sb.append(fd.getName());
        sb.append("</a>");
        return sb.toString();
    }

    public static String makeUrl(FileDescriptor fd, boolean attachment) {
        StringBuilder sb = new StringBuilder();
        UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
        sb.append("dispatch/download?")
                .append("s=").append(sessionSource.getUserSession().getId()).append("&")
                .append("f=").append(fd.getId());
        if (attachment)
            sb.append("&a=true");
        return sb.toString();
    }

    public static void initGeneratedColumn(final Table table) {
        final ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.NAME);
        final ExportDisplay exportDisplay = AppBeans.get(ExportDisplay.NAME);

        table.addGeneratedColumn("name", new Table.ColumnGenerator<FileDescriptor>() {
            @Override
            public Component generateCell(final FileDescriptor fd) {
                if (fd == null) {
                    return componentsFactory.createComponent(Label.NAME);
                }

                if (PersistenceHelper.isNew(fd)) {
                    Label label = componentsFactory.createComponent(Label.class);
                    label.setValue(fd.getName());
                    return label;
                } else {
                    Button button = componentsFactory.createComponent(Button.class);
                    button.setStyleName("link");
                    button.setAction(new AbstractAction("download") {
                        @Override
                        public void actionPerform(Component component) {
                            exportDisplay.show(fd);
                        }

                        @Override
                        public String getCaption() {
                            return fd.getName();
                        }
                    });
                    return button;
                }
            }
        });
    }

    public static void initGeneratedColumn(final Table table, final String fileProperty) {
        final ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.NAME);
        final ExportDisplay exportDisplay = AppBeans.get(ExportDisplay.NAME);

        table.addGeneratedColumn(fileProperty + ".name", new Table.ColumnGenerator() {
            @Override
            public Component generateCell(final Entity entity) {
                final FileDescriptor fd = entity.getValueEx(fileProperty);
                if (fd == null) {
                    return componentsFactory.createComponent(Label.class);
                }

                if (PersistenceHelper.isNew(fd)) {
                    Label label = componentsFactory.createComponent(Label.class);
                    label.setValue(fd.getName());
                    return label;
                } else {
                    Button button = componentsFactory.createComponent(Button.class);
                    button.setStyleName("link");
                    button.setAction(new AbstractAction("download") {
                        @Override
                        public void actionPerform(Component component) {
                            exportDisplay.show(fd);
                        }

                        @Override
                        public String getCaption() {
                            return fd.getName();
                        }
                    });
                    return button;
                }
            }
        });
    }

    public static String formatFileSize(long longSize, int decimalPos) {
        Messages messages = AppBeans.get(Messages.NAME);

        NumberFormat fmt = NumberFormat.getNumberInstance();
        if (decimalPos >= 0) {
            fmt.setMaximumFractionDigits(decimalPos);
        }
        final double size = longSize;
        double val = size / (1024 * 1024);
        if (val > 1) {
            return fmt.format(val).concat(" " + messages.getMessage(FileDownloadHelper.class, "fmtMb"));
        }
        val = size / 1024;
        if (val > 10) {
            return fmt.format(val).concat(" " + messages.getMessage(FileDownloadHelper.class, "fmtKb"));
        }
        return fmt.format(size).concat(" " + messages.getMessage(FileDownloadHelper.class, "fmtB"));
    }

    public static String formatFileSize(long fileSize) {
        return formatFileSize(fileSize, 0);
    }
}