/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.auth.RequestContext;
import com.haulmont.cuba.web.toolkit.ui.client.multiupload.CubaMultiUploadServerRpc;
import com.haulmont.cuba.web.toolkit.ui.client.multiupload.CubaMultiUploadState;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.LegacyComponent;
import org.apache.commons.lang.ObjectUtils;

import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaMultiUpload extends AbstractComponent implements LegacyComponent {

    private List<UploadListener> uploadListeners = new ArrayList<>();

    private BootstrapFailureHandler bootstrapFailureHandler;

    private boolean interrupted = false;

    /**
     * The output of the upload is redirected to this receiver.
     */
    private Receiver receiver;

    private boolean isUploading;

    CubaMultiUploadServerRpc rpc = new CubaMultiUploadServerRpc() {
        @Override
        public void resourceLoadingFailed() {
            if (bootstrapFailureHandler != null)
                bootstrapFailureHandler.loadWebResourcesFailed();
        }

        @Override
        public void flashNotInstalled() {
            if (bootstrapFailureHandler != null)
                bootstrapFailureHandler.flashNotInstalled();
        }

        @Override
        public void queueUploadCompleted() {
            fireQueueComplete();
        }

        @Override
        public void uploadError(String fileName, String message, int code) {
            UploadErrorType uploadErrorType = UploadErrorType.fromId(code);
            fireError(fileName, message, uploadErrorType);
        }
    };

    /*
     * Handle to terminal via Upload monitors and controls the upload during it
     * is being streamed.
     */
    private com.vaadin.server.StreamVariable streamVariable;

    protected com.vaadin.server.StreamVariable getStreamVariable() {
        if (streamVariable == null) {
            streamVariable = new com.vaadin.server.StreamVariable() {
                private StreamingStartEvent lastStartedEvent;

                @Override
                public boolean listenProgress() {
                    return false;
                }

                @Override
                public void onProgress(StreamingProgressEvent event) {
                }

                @Override
                public boolean isInterrupted() {
                    return interrupted;
                }

                @Override
                public OutputStream getOutputStream() {
                    if (receiver == null)
                        return null;

                    OutputStream receiveUpload = receiver.receiveUpload(
                            lastStartedEvent.getFileName(),
                            lastStartedEvent.getMimeType());
                    lastStartedEvent = null;
                    return receiveUpload;
                }

                @Override
                public void streamingStarted(StreamingStartEvent event) {
                    fireStarted(event.getFileName());
                    lastStartedEvent = event;
                    isUploading = true;
                }

                @Override
                public void streamingFinished(StreamingEndEvent event) {
                    fireFinished(event.getFileName());
                    markAsDirty();
                    isUploading = false;
                    lastStartedEvent = null;
                }

                @Override
                public void streamingFailed(StreamingErrorEvent event) {
                    fireError(event.getFileName(), "", UploadErrorType.IO_ERROR);
                    isUploading = false;
                    lastStartedEvent = null;
                }
            };
        }
        return streamVariable;
    }

    public void interruptUpload() {
        if (isUploading) {
            interrupted = true;
        }
    }

    /**
     * Emit upload received event.
     *
     * @param filename file name
     */
    protected void fireStarted(String filename) {
        for (UploadListener uploadListener : uploadListeners)
            uploadListener.fileUploadStart(filename);
    }

    /**
     * Emit upload received event.
     *
     * @param filename file name
     */
    protected void fireFinished(String filename) {
        for (UploadListener uploadListener : uploadListeners)
            uploadListener.fileUploaded(filename);
    }

    /**
     * Emit upload received event.
     *
     * @param filename file name
     */
    protected void fireError(String filename, String message, UploadErrorType uploadErrorType) {
        for (UploadListener uploadListener : uploadListeners)
            uploadListener.errorNotify(filename, message, uploadErrorType);
    }

    /**
     * Emit upload received event.
     */
    protected void fireQueueComplete() {
        for (UploadListener uploadListener : uploadListeners)
            uploadListener.queueUploadComplete();
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        getState().jsessionId = RequestContext.get().getRequest().getSession().getId();

        super.beforeClientResponse(initial);
    }

    public CubaMultiUpload() {
        registerRpc(rpc);
    }

    public Resource getButtonImage() {
        return getResource(CubaMultiUploadState.BUTTON_IMAGE_KEY);
    }

    public void setButtonImage(Resource image) {
        setResource(CubaMultiUploadState.BUTTON_IMAGE_KEY, image);
    }

    public int getButtonWidth() {
        return getState(false).buttonWidth;
    }

    public void setButtonWidth(int buttonWidth) {
        getState().buttonWidth = buttonWidth;
    }

    public void setButtonHeight(int buttonHeight) {
        getState().buttonHeight = buttonHeight;
    }

    public int getButtonHeight() {
        return getState(false).buttonHeight;
    }

    public String getFileTypesMask() {
        return getState(false).fileTypes;
    }

    public void setFileTypesMask(String fileTypesMask) {
        getState().fileTypes = fileTypesMask;
    }

    public String getFileTypesDescription() {
        return getState(false).fileTypesDescription;
    }

    public void setFileTypesDescription(String fileTypesDescription) {
        getState().fileTypesDescription = fileTypesDescription;
    }

    public double getFileSizeLimitMB() {
        return getState(false).fileSizeLimit;
    }

    public void setFileSizeLimitMB(double filesizeLimit) {
        getState().fileSizeLimit = filesizeLimit;
    }

    public double getQueueUploadLimitMB() {
        return getState(false).queueUploadLimit;
    }

    public void setQueueUploadLimitMB(double queueUploadLimit) {
        getState().queueUploadLimit = queueUploadLimit;
    }

    public int getQueueSizeLimit() {
        return getState(false).queueSizeLimit;
    }

    public void setQueueSizeLimit(int queueSizeLimit) {
        getState().queueSizeLimit = queueSizeLimit;
    }

    @Override
    public void setCaption(String caption) {
        getState().buttonCaption = caption;
    }

    @Override
    public String getCaption() {
        return getState(false).buttonCaption;
    }

    @Override
    public CubaMultiUploadState getState() {
        return (CubaMultiUploadState) super.getState();
    }

    @Override
    protected CubaMultiUploadState getState(boolean markAsDirty) {
        return (CubaMultiUploadState) super.getState(markAsDirty);
    }

    public void addUploadListener(UploadListener startListener) {
        uploadListeners.add(startListener);
    }

    public void removeUploadListener(UploadListener startListener) {
        uploadListeners.remove(startListener);
    }

    public BootstrapFailureHandler getBootstrapFailureHandler() {
        return bootstrapFailureHandler;
    }

    public void setBootstrapFailureHandler(BootstrapFailureHandler bootstrapFailureHandler) {
        this.bootstrapFailureHandler = bootstrapFailureHandler;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        target.addVariable(this, "action", getStreamVariable());
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
    }

    /**
     * Interface that must be implemented by the upload receivers to provide the
     * Upload component an output stream to write the uploaded data.
     */
    public interface Receiver extends Serializable {

        /**
         * Invoked when a new upload arrives.
         *
         * @param filename
         *            the desired filename of the upload, usually as specified
         *            by the client.
         * @param mimeType
         *            the MIME type of the uploaded file.
         * @return Stream to which the uploaded file should be written.
         */
        public OutputStream receiveUpload(String filename, String mimeType);

    }

    public interface BootstrapFailureHandler extends Serializable {
        void loadWebResourcesFailed();

        void flashNotInstalled();
    }

    public enum UploadErrorType {
        QUEUE_LIMIT_EXCEEDED(-100),
        FILE_EXCEEDS_SIZE_LIMIT(-110),
        ZERO_BYTE_FILE(-120),
        INVALID_FILETYPE(-130),
        HTTP_ERROR(-200),
        MISSING_UPLOAD_URL(-210),
        IO_ERROR(-220),
        SECURITY_ERROR(-230),
        UPLOAD_LIMIT_EXCEEDED(-240),
        UPLOAD_FAILED(-250),
        SPECIFIED_FILE_ID_NOT_FOUND(-260),
        FILE_VALIDATION_FAILED(-270),
        FILE_CANCELLED(-280),
        UPLOAD_STOPPED(-290);

        private int id;

        private UploadErrorType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static UploadErrorType fromId(Integer id) {
            for (UploadErrorType type : UploadErrorType.values()) {
                if (ObjectUtils.equals(id, type.getId()))
                    return type;
            }
            return UPLOAD_FAILED; // unknown id
        }
    }

    public interface UploadListener extends Serializable {
        void fileUploadStart(String fileName);

        void fileUploaded(String fileName);

        void queueUploadComplete();

        void errorNotify(String fileName, String message, UploadErrorType errorCode);
    }
}