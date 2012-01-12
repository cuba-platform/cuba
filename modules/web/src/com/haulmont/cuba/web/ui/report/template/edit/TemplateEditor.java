/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.ui.report.template.edit;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.report.Report;
import com.haulmont.cuba.report.ReportTemplate;
import com.haulmont.cuba.web.app.FileDownloadHelper;
import com.haulmont.cuba.web.filestorage.WebExportDisplay;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class TemplateEditor extends BasicEditor {
    private static final long serialVersionUID = 2000883633888106921L;
    private static final String DEFAULT_TEMPLATE_CODE = "DEFAULT";

    private ReportTemplate template;

    @Inject
    private Button templatePath;

    @Inject
    private TextField customClass;

    @Inject
    private FileUploadField uploadTemplate;

    private Map<ReportTemplate, ArrayList<FileDescriptor>> deletedContainer;
    private List<FileDescriptor> deletedList = new ArrayList<FileDescriptor>();

    @Override
    public void setItem(Entity item) {
        template = (ReportTemplate)item;
        if (StringUtils.isEmpty(template.getCode())) {
            Report report = template.getReport();
            if (report != null) {
                if ((report.getTemplates() == null) || (report.getTemplates().size() == 0)) {
                    template.setCode(DEFAULT_TEMPLATE_CODE);
                    template.setDefaultFlag(true);
                } else
                    template.setCode("Template_" + Integer.toString(report.getTemplates().size()));
            }
        }
        super.setItem(template);

        template = (ReportTemplate) getItem();
        enableCustomProps(template.getCustomFlag());

        FileDescriptor templateDescriptor = template.getTemplateFileDescriptor();
        if (templateDescriptor != null)
            templatePath.setCaption(templateDescriptor.getName());
    }

    private void enableCustomProps(boolean customEnabled) {
        templatePath.setEnabled(!customEnabled);
        uploadTemplate.setEnabled(!customEnabled);
        customClass.setEnabled(customEnabled);
    }

    public TemplateEditor(IFrame frame) {
        super(frame);

        getDialogParams().setWidth(490);
    }

    @Override
    @SuppressWarnings({"serial", "unchecked"})
    public void init(Map<String, Object> params) {
        super.init(params);

        deletedContainer = (java.util.Map) params.get("param$deletedContainer");

        CheckBox custom = getComponent("customFlag");
        custom.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                Boolean isCustom = Boolean.TRUE.equals(value);
                enableCustomProps(isCustom);
            }
        });

        FileUploadField.Listener uploadListener = new FileUploadField.Listener() {

            @Override
            public void uploadStarted(Event event) {
                uploadTemplate.setEnabled(false);
            }

            @Override
            public void uploadFinished(Event event) {
                uploadTemplate.setEnabled(true);
            }

            @Override
            public void uploadSucceeded(Event event) {
                FileUploadingAPI fileUploading = AppContext.getBean(FileUploadingAPI.NAME);

                FileDescriptor templateDescriptor = new com.haulmont.cuba.core.entity.FileDescriptor();
                templateDescriptor.setName(uploadTemplate.getFileName());
                templateDescriptor.setExtension(FileDownloadHelper.getFileExt(uploadTemplate.getFileName()));

                File file = fileUploading.getFile(uploadTemplate.getFileId());
                templateDescriptor.setSize((int) file.length());

                templateDescriptor.setCreateDate(TimeProvider.currentTimestamp());
                saveFile(fileUploading, templateDescriptor, uploadTemplate);
                templatePath.setCaption(templateDescriptor.getName());

                if (template.getTemplateFileDescriptor() != null)
                    deletedList.add(template.getTemplateFileDescriptor());
                template.setTemplateFileDescriptor(templateDescriptor);

                showNotification(MessageProvider.getMessage(TemplateEditor.class,
                        "templateEditor.uploadSuccess"), IFrame.NotificationType.HUMANIZED);
            }

            @Override
            public void uploadFailed(Event event) {
                showNotification(MessageProvider.getMessage(TemplateEditor.class,
                        "templateEditor.uploadUnsuccess"), IFrame.NotificationType.WARNING);
            }

            @Override
            public void updateProgress(long readBytes, long contentLength) {
            }
        };
        uploadTemplate.addListener(uploadListener);

        templatePath.setAction(new AbstractAction("report.template") {
            @Override
            public void actionPerform(Component component) {
                if (template.getTemplateFileDescriptor() != null) {
                    WebExportDisplay display = new WebExportDisplay();
                    display.show(template.getTemplateFileDescriptor());
                }
            }
        });
    }

    private void saveFile(FileUploadingAPI fileUploading, FileDescriptor templateDescriptor,
                          FileUploadField uploadTemplate) {
        try {
            fileUploading.putFileIntoStorage(uploadTemplate.getFileId(), templateDescriptor);
        } catch (FileStorageException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean commit() {
        boolean result = super.commit();
        if (result) {
            if (deletedContainer.get(template) == null)
                deletedContainer.put(template, new ArrayList<FileDescriptor>());
            List<FileDescriptor> deletedFilesList = deletedContainer.get(template);
            deletedFilesList.addAll(deletedList);
        }
        return result;
    }

    @Override
    public boolean commit(boolean validate) {
        boolean result = super.commit(validate);
        if (result && (deletedList.size() > 0)) {
            if (deletedContainer.get(template) == null)
                deletedContainer.put(template, new ArrayList<FileDescriptor>());
            List<FileDescriptor> deletedFilesList = deletedContainer.get(template);
            deletedFilesList.addAll(deletedList);
        }
        return result;
    }

    @Override
    public void commitAndClose() {
        if (!template.getCustomFlag()) {
            template.setCustomClass("");
        }
        if (commit(true))
            close(COMMIT_ACTION_ID);
    }
}
