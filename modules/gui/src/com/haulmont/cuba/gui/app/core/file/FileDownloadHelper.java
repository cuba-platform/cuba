/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
 * @author krivopustov
 * @version $Id$
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
        sb.append("dispatch/download?")
                .append("s=").append(AppBeans.get(UserSessionSource.class).getUserSession().getId()).append("&")
                .append("f=").append(fd.getId());
        if (attachment)
            sb.append("&a=true");
        return sb.toString();
    }

    public static void initGeneratedColumn(final Table table) {
        final ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.class);
        final ExportDisplay exportDisplay = AppBeans.get(ExportDisplay.class);

        table.addGeneratedColumn("name", new Table.ColumnGenerator<FileDescriptor>() {
            @Override
            public Component generateCell(final FileDescriptor fd) {
                if (fd == null) {
                    return componentsFactory.createComponent(Label.NAME);
                }

                if (PersistenceHelper.isNew(fd)) {
                    Label label = componentsFactory.createComponent(Label.NAME);
                    label.setValue(fd.getName());
                    return label;
                } else {
                    Button button = componentsFactory.createComponent(Button.NAME);
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
        final ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.class);
        final ExportDisplay exportDisplay = AppBeans.get(ExportDisplay.class);

        table.addGeneratedColumn(fileProperty + ".name", new Table.ColumnGenerator() {
            @Override
            public Component generateCell(final Entity entity) {
                final FileDescriptor fd = entity.getValueEx(fileProperty);
                if (fd == null) {
                    return componentsFactory.createComponent(Label.NAME);
                }

                if (PersistenceHelper.isNew(fd)) {
                    Label label = componentsFactory.createComponent(Label.NAME);
                    label.setValue(fd.getName());
                    return label;
                } else {
                    Button button = componentsFactory.createComponent(Button.NAME);
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