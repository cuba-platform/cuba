/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package cuba.client.web.ui.report.template.edit;

import com.haulmont.cuba.core.app.FileStorageService;
import com.haulmont.cuba.core.app.FileUploadService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.report.ReportTemplate;
import com.haulmont.cuba.web.app.FileDownloadHelper;
import com.haulmont.cuba.web.filestorage.FileDisplay;

import java.io.File;
import java.util.*;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class TemplateEditor extends BasicEditor {
    private static final long serialVersionUID = 2000883633888106921L;

    private ReportTemplate template;

    private Button templatePath;
    private TextField customClass;
    private FileUploadField uploadTemplate;

    private FileDescriptor templateDescriptor;

    private Map deletedContainer;
    private List deletedList = new ArrayList();

    @Override
    public void setItem(Entity item) {
        super.setItem(item);
        template = (ReportTemplate) getItem();
        enableCustomProps(template.getCustomFlag());

        templateDescriptor = template.getTemplateFileDescriptor();
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
    protected void init(Map<String, Object> params) {
        super.init(params);

        deletedContainer = (java.util.Map) params.get("param$deletedContainer");

        uploadTemplate = getComponent("uploadTemplate");
        templatePath = getComponent("templatePath");
        customClass = getComponent("customClass");

        CheckBox custom = getComponent("customFlag");
        custom.addListener(new ValueListener() {
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                Boolean isCustom = Boolean.TRUE.equals(value);
                enableCustomProps(isCustom);
            }
        });

        FileUploadField.Listener uploadListener = new FileUploadField.Listener() {

            public void uploadStarted(Event event) {
                uploadTemplate.setEnabled(false);
            }

            public void uploadFinished(Event event) {
                uploadTemplate.setEnabled(true);
            }

            public void uploadSucceeded(Event event) {
                FileUploadService uploadService = ServiceLocator.lookup(FileUploadService.NAME);

                templateDescriptor = new com.haulmont.cuba.core.entity.FileDescriptor();
                templateDescriptor.setName(uploadTemplate.getFileName());
                templateDescriptor.setExtension(FileDownloadHelper.getFileExt(uploadTemplate.getFileName()));

                File file = uploadService.getFile(uploadTemplate.getFileId());
                templateDescriptor.setSize((int) file.length());

                templateDescriptor.setCreateDate(TimeProvider.currentTimestamp());
                saveFile(uploadService, uploadTemplate);
                templatePath.setCaption(templateDescriptor.getName());

                if (template.getTemplateFileDescriptor() != null)
                    deletedList.add(template.getTemplateFileDescriptor());
                template.setTemplateFileDescriptor(templateDescriptor);

                showNotification(MessageProvider.getMessage(TemplateEditor.class,
                        "templateEditor.uploadSuccess"), IFrame.NotificationType.HUMANIZED);
            }

            public void uploadFailed(Event event) {
                showNotification(MessageProvider.getMessage(TemplateEditor.class,
                        "templateEditor.uploadUnsuccess"), IFrame.NotificationType.WARNING);
            }

            public void updateProgress(long readBytes, long contentLength) {
            }
        };
        uploadTemplate.addListener(uploadListener);

        templatePath.setAction(new AbstractAction("report.template") {
            public void actionPerform(Component component) {
                if (templateDescriptor != null) {
                    FileDisplay fileDisplay = new FileDisplay(true);
                    fileDisplay.show(templateDescriptor.getName(), templateDescriptor, true);
                }
            }
        });
    }

    private void saveFile(FileUploadService uploadService, FileUploadField uploadTemplate) {
        FileStorageService fss = ServiceLocator.lookup(FileStorageService.NAME);
        try {
            UUID fileId = uploadTemplate.getFileId();
            File file = uploadService.getFile(fileId);
            fss.putFile(templateDescriptor, file);
            uploadService.deleteFile(fileId);
        } catch (FileStorageException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean commit() {
        boolean result = super.commit();
        if (result) {
            if (deletedContainer.get(template) == null)
                deletedContainer.put(template, new ArrayList());
            List deletedFilesList = (List) deletedContainer.get(template);
            deletedFilesList.addAll(deletedList);
        }
        return result;
    }

    @Override
    public boolean commit(boolean validate) {
        boolean result = super.commit(validate);
        if (result && (deletedList.size() > 0)) {
            if (deletedContainer.get(template) == null)
                deletedContainer.put(template, new ArrayList());
            List deletedFilesList = (List) deletedContainer.get(template);
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
