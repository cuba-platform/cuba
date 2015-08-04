/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.fileupload.CubaFileUploadServerRpc;
import com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.CubaFileUploadState;
import com.vaadin.annotations.JavaScript;
import com.vaadin.server.*;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.LegacyComponent;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author artamonov
 * @version $Id$
 */
@JavaScript({
        "resources/jqueryfileupload/jquery.ui.widget-1.11.1.min.js",
        "resources/jqueryfileupload/jquery.iframe-transport-9.10.5.min.js",
        "resources/jqueryfileupload/jquery.fileupload-9.10.5.min.js"
})
public class CubaFileUpload extends AbstractComponent implements Component.Focusable, LegacyComponent {

    /**
     * The output of the upload is redirected to this receiver.
     */
    protected Receiver receiver;

    private boolean isUploading;

    private long contentLength = -1;

    private boolean interrupted = false;

    /*
     * Handle to terminal via Upload monitors and controls the upload during it
     * is being streamed.
     */
    protected com.vaadin.server.StreamVariable streamVariable;

    protected Set<String> permittedExtensions;

    public CubaFileUpload() {
        registerRpc(new CubaFileUploadServerRpc() {
            @Override
            public void fileSizeLimitExceeded(String fileName) {
                fireFileSizeLimitExceeded(fileName);
            }

            @Override
            public void queueUploadFinished() {
                // trigger UI update after uploading
                markAsDirty();
            }
        });

        setErrorHandler(new ErrorHandler() {
            @Override
            public void error(com.vaadin.server.ErrorEvent event) {
                Log log = LogFactory.getLog(CubaFileUpload.class);
                //noinspection ThrowableResultOfMethodCallIgnored
                Throwable ex = event.getThrowable();
                if (StringUtils.contains(ExceptionUtils.getRootCauseMessage(ex), "The multipart stream ended unexpectedly")) {
                    log.warn("Unable to upload file, it seems upload canceled or network error occured");
                } else {
                    log.warn("Unexpected error in CubaFileUpload", ex);
                }
            }
        });
    }

    @Override
    protected CubaFileUploadState getState() {
        return (CubaFileUploadState) super.getState();
    }

    @Override
    protected CubaFileUploadState getState(boolean markAsDirty) {
        return (CubaFileUploadState) super.getState(markAsDirty);
    }

    @Override
    public int getTabIndex() {
        return getState(false).tabIndex;
    }

    @Override
    public void setTabIndex(int tabIndex) {
        if (getTabIndex() != tabIndex) {
            getState().tabIndex = tabIndex;
        }
    }

    @Override
    public void focus() {
        // Overridden only to make public
        super.focus();
    }

    /**
     * Returns the icon's alt text.
     *
     * @return String with the alt text
     */
    public String getIconAlternateText() {
        return getState(false).iconAltText;
    }

    public void setIconAlternateText(String iconAltText) {
        if (!ObjectUtils.equals(getIconAlternateText(), iconAltText)) {
            getState().iconAltText = iconAltText;
        }
    }

    /**
     * Return HTML rendering setting
     *
     * @return <code>true</code> if the caption text is to be rendered as HTML,
     *         <code>false</code> otherwise
     */
    public boolean isHtmlContentAllowed() {
        return getState(false).captionAsHtml;
    }

    /**
     * Set whether the caption text is rendered as HTML or not. You might need
     * to re-theme button to allow higher content than the original text style.
     *
     * If set to true, the captions are passed to the browser as html and the
     * developer is responsible for ensuring no harmful html is used. If set to
     * false, the content is passed to the browser as plain text.
     *
     * @param htmlContentAllowed
     *            <code>true</code> if caption is rendered as HTML,
     *            <code>false</code> otherwise
     */
    public void setHtmlContentAllowed(boolean htmlContentAllowed) {
        if (isHtmlContentAllowed() != htmlContentAllowed) {
            getState().captionAsHtml = htmlContentAllowed;
        }
    }

    /**
     * Sets the component's icon and alt text.
     *
     * An alt text is shown when an image could not be loaded, and read by
     * assisitve devices.
     *
     * @param icon
     *            the icon to be shown with the component's caption.
     * @param iconAltText
     *            String to use as alt text
     */
    public void setIcon(Resource icon, String iconAltText) {
        super.setIcon(icon);

        getState().iconAltText = iconAltText;
    }

    public boolean isMultiSelect() {
        return getState(false).multiSelect;
    }

    public void setMultiSelect(boolean multiSelect) {
        if (isMultiSelect() != multiSelect) {
            getState().multiSelect = multiSelect;
        }
    }

    public String getUnableToUploadFileMessage() {
        return getState(false).unableToUploadFileMessage;
    }

    public void setUnableToUploadFileMessage(String message) {
        if (!ObjectUtils.equals(getUnableToUploadFileMessage(), message)) {
            getState().unableToUploadFileMessage = message;
        }
    }

    public String getProgressWindowCaption() {
        return getState(false).progressWindowCaption;
    }

    public void setProgressWindowCaption(String progressWindowCaption) {
        if (!ObjectUtils.equals(getProgressWindowCaption(), progressWindowCaption)) {
            getState().progressWindowCaption = progressWindowCaption;
        }
    }

    public String getCancelButtonCaption() {
        return getState(false).cancelButtonCaption;
    }

    public void setCancelButtonCaption(String cancelButtonCaption) {
        if (!ObjectUtils.equals(getCancelButtonCaption(), cancelButtonCaption)) {
            getState().cancelButtonCaption = cancelButtonCaption;
        }
    }

    public String getAccept() {
        return getState(false).accept;
    }

    /**
     * Note: this is just a hint for browser, user may select files that do not meet this property
     *
     * @param accept mime types, comma separated
     */
    public void setAccept(String accept) {
        if (!StringUtils.equals(accept, getAccept())) {
            getState().accept = accept;
        }
    }

    public Set<String> getPermittedExtensions() {
        if (permittedExtensions == null) {
            return Collections.emptySet();
        }
        return permittedExtensions;
    }

    public void setPermittedExtensions(Set<String> permittedExtensions) {
        this.permittedExtensions = permittedExtensions;
    }

    public void setPermittedExtensions(String... permittedExtensions) {
        if (permittedExtensions != null) {
            this.permittedExtensions = new HashSet<>(Arrays.asList(permittedExtensions));
        }
    }

    public double getFileSizeLimit() {
        return getState(false).fileSizeLimit;
    }

    /**
     * @param fileSizeLimit file size limit in bytes
     */
    public void setFileSizeLimit(int fileSizeLimit) {
        getState().fileSizeLimit = fileSizeLimit;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

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
                    if (getReceiver() == null) {
                        throw new IllegalStateException(
                                "Upload cannot be performed without a receiver set");
                    }
                    OutputStream receiveUpload = getReceiver().receiveUpload(
                            lastStartedEvent.getFileName(),
                            lastStartedEvent.getMimeType());
                    lastStartedEvent = null;
                    return receiveUpload;
                }

                @Override
                public void streamingStarted(StreamingStartEvent event) {
                    startUpload();
                    contentLength = event.getContentLength();
                    lastStartedEvent = event;

                    double fileSizeLimit = getFileSizeLimit();
                    if (fileSizeLimit >0 && event.getContentLength() > fileSizeLimit) {
                        Log log = LogFactory.getLog(CubaFileUpload.class);
                        log.warn("Unable to start upload. File size limit exceeded, but client-side checks ignored.");

                        interruptUpload();
                        return;
                        // here client sends file to us bypassing client-side checks, just stop uploading
                    }

                    fireStarted(event.getFileName(), event.getMimeType());
                }

                @Override
                public void streamingFinished(StreamingEndEvent event) {
                    fireUploadSuccess(event.getFileName(), event.getMimeType(), event.getContentLength());
                    endUpload();
                }

                @Override
                public void streamingFailed(StreamingErrorEvent event) {
                    try {
                        Exception exception = event.getException();
                        if (exception instanceof NoInputStreamException) {
                            fireNoInputStream(event.getFileName(),
                                    event.getMimeType(), 0);
                        } else if (exception instanceof NoOutputStreamException) {
                            fireNoOutputStream(event.getFileName(),
                                    event.getMimeType(), 0);
                        } else {
                            fireUploadInterrupted(event.getFileName(),
                                    event.getMimeType(), 0, exception);
                        }
                    } finally {
                        endUpload();
                    }
                }
            };
        }
        return streamVariable;
    }

    /**
     * Go into upload state. This is to prevent double uploading on same
     * component.
     *
     * Warning: this is an internal method used by the framework and should not
     * be used by user of the Upload component. Using it results in the Upload
     * component going in wrong state and not working. It is currently public
     * because it is used by another class.
     */
    public void startUpload() {
        if (isUploading) {
            throw new IllegalStateException("uploading already started");
        }
        isUploading = true;
    }

    /**
     * Interrupts the upload currently being received. The interruption will be
     * done by the receiving thread so this method will return immediately and
     * the actual interrupt will happen a bit later.
     */
    public void interruptUpload() {
        if (isUploading) {
            interrupted = true;
        }
    }

    /**
     * Go into state where new uploading can begin.
     *
     * Warning: this is an internal method used by the framework and should not
     * be used by user of the Upload component.
     */
    private void endUpload() {
        isUploading = false;
        contentLength = -1;
        interrupted = false;
        markAsDirty();
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        // Post file to this strean variable
        target.addVariable(this, "uploadUrl", getStreamVariable());
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
    }

    protected void fireStarted(String filename, String MIMEType) {
        fireEvent(new StartedEvent(this, filename, MIMEType,
                contentLength));
    }

    protected void fireNoInputStream(String filename, String MIMEType,
                                     long length) {
        fireEvent(new NoInputStreamEvent(this, filename, MIMEType,
                length));
    }

    protected void fireNoOutputStream(String filename, String MIMEType,
                                      long length) {
        fireEvent(new NoOutputStreamEvent(this, filename, MIMEType,
                length));
    }

    protected void fireUploadInterrupted(String filename, String MIMEType,
                                         long length, Exception e) {
        fireEvent(new FailedEvent(this, filename, MIMEType, length, e));
    }

    protected void fireUploadSuccess(String filename, String MIMEType,
                                     long length) {
        fireEvent(new SucceededEvent(this, filename, MIMEType, length));
    }

    protected void fireFileSizeLimitExceeded(String filename) {
        fireEvent(new FileSizeLimitExceededEvent(this, filename));
    }

    /**
     * Interface that must be implemented by the upload receivers to provide the
     * Upload component an output stream to write the uploaded data.
     *
     * @author Vaadin Ltd.
     * @since 3.0
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
        OutputStream receiveUpload(String filename, String mimeType);
    }

    /**
     * Upload.FinishedEvent is sent when the upload receives a file, regardless
     * of whether the reception was successful or failed. If you wish to
     * distinguish between the two cases, use either SucceededEvent or
     * FailedEvent, which are both subclasses of the FinishedEvent.
     *
     * @author Vaadin Ltd.
     * @since 3.0
     */
    public static class FinishedEvent extends Component.Event {

        /**
         * Length of the received file.
         */
        private final long length;

        /**
         * MIME type of the received file.
         */
        private final String type;

        /**
         * Received file name.
         */
        private final String filename;

        /**
         *
         * @param source
         *            the source of the file.
         * @param filename
         *            the received file name.
         * @param MIMEType
         *            the MIME type of the received file.
         * @param length
         *            the length of the received file.
         */
        public FinishedEvent(CubaFileUpload source, String filename, String MIMEType,
                             long length) {
            super(source);
            type = MIMEType;
            this.filename = filename;
            this.length = length;
        }

        /**
         * Uploads where the event occurred.
         *
         * @return the Source of the event.
         */
        public CubaFileUpload getUpload() {
            return (CubaFileUpload) getSource();
        }

        /**
         * Gets the file name.
         *
         * @return the filename.
         */
        public String getFilename() {
            return filename;
        }

        /**
         * Gets the MIME Type of the file.
         *
         * @return the MIME type.
         */
        public String getMIMEType() {
            return type;
        }

        /**
         * Gets the length of the file.
         *
         * @return the length.
         */
        public long getLength() {
            return length;
        }
    }

    /**
     * Upload.FailedEvent event is sent when the upload is received, but the
     * reception is interrupted for some reason.
     *
     * @author Vaadin Ltd.
     * @since 3.0
     */
    public static class FailedEvent extends FinishedEvent {

        private Exception reason = null;

        public FailedEvent(CubaFileUpload source, String filename, String MIMEType,
                           long length, Exception reason) {
            this(source, filename, MIMEType, length);
            this.reason = reason;
        }

        public FailedEvent(CubaFileUpload source, String filename, String MIMEType,
                           long length) {
            super(source, filename, MIMEType, length);
        }

        /**
         * Gets the exception that caused the failure.
         *
         * @return the exception that caused the failure, null if n/a
         */
        public Exception getReason() {
            return reason;
        }
    }

    public static class FileSizeLimitExceededEvent extends Component.Event {

        private String filename;

        /**
         * @param source   the source of the file.
         * @param filename the received file name.
         */
        public FileSizeLimitExceededEvent(CubaFileUpload source, String filename) {
            super(source);

            this.filename = filename;
        }
    }

    /**
     * FailedEvent that indicates that an output stream could not be obtained.
     */
    public static class NoOutputStreamEvent extends FailedEvent {

        public NoOutputStreamEvent(CubaFileUpload source, String filename,
                                   String MIMEType, long length) {
            super(source, filename, MIMEType, length);
        }
    }

    /**
     * FailedEvent that indicates that an input stream could not be obtained.
     */
    public static class NoInputStreamEvent extends FailedEvent {

        public NoInputStreamEvent(CubaFileUpload source, String filename,
                                  String MIMEType, long length) {
            super(source, filename, MIMEType, length);
        }
    }

    /**
     * Upload.SucceededEvent event is sent when the upload is received
     * successfully.
     *
     * @author Vaadin Ltd.
     * @since 3.0
     */
    public static class SucceededEvent extends FinishedEvent {

        public SucceededEvent(CubaFileUpload source, String filename, String MIMEType,
                              long length) {
            super(source, filename, MIMEType, length);
        }
    }

    /**
     * Upload.StartedEvent event is sent when the upload is started to received.
     *
     * @author Vaadin Ltd.
     * @since 5.0
     */
    public static class StartedEvent extends Component.Event {

        private final String filename;
        private final String type;
        /**
         * Length of the received file.
         */
        private final long length;

        public StartedEvent(CubaFileUpload source, String filename, String MIMEType,
                            long contentLength) {
            super(source);
            this.filename = filename;
            type = MIMEType;
            length = contentLength;
        }

        /**
         * Uploads where the event occurred.
         *
         * @return the Source of the event.
         */
        public CubaFileUpload getUpload() {
            return (CubaFileUpload) getSource();
        }

        /**
         * Gets the file name.
         *
         * @return the filename.
         */
        public String getFilename() {
            return filename;
        }

        /**
         * Gets the MIME Type of the file.
         *
         * @return the MIME type.
         */
        public String getMIMEType() {
            return type;
        }

        /**
         * @return the length of the file that is being uploaded
         */
        public long getContentLength() {
            return length;
        }
    }

    /**
     * Receives the events when the upload starts.
     *
     * @author Vaadin Ltd.
     * @since 5.0
     */
    public interface StartedListener extends Serializable {

        /**
         * Upload has started.
         *
         * @param event
         *            the Upload started event.
         */
        void uploadStarted(StartedEvent event);
    }

    /**
     * Receives the events when the uploads are ready.
     *
     * @author Vaadin Ltd.
     * @since 3.0
     */
    public interface FinishedListener extends Serializable {

        /**
         * Upload has finished.
         *
         * @param event
         *            the Upload finished event.
         */
        void uploadFinished(FinishedEvent event);
    }

    /**
     * Receives events when the uploads are finished, but unsuccessful.
     *
     * @author Vaadin Ltd.
     * @since 3.0
     */
    public interface FailedListener extends Serializable {

        /**
         * Upload has finished unsuccessfully.
         *
         * @param event
         *            the Upload failed event.
         */
        void uploadFailed(FailedEvent event);
    }

    /**
     * Receives events when the uploads are successfully finished.
     *
     * @author Vaadin Ltd.
     * @since 3.0
     */
    public interface SucceededListener extends Serializable {

        /**
         * Upload successfull..
         *
         * @param event
         *            the Upload successfull event.
         */
        void uploadSucceeded(SucceededEvent event);
    }

    public interface FileSizeLimitExceededListener extends Serializable {

        void fileSizeLimitExceeded(FileSizeLimitExceededEvent e);
    }

    private static final Method UPLOAD_FINISHED_METHOD;

    private static final Method UPLOAD_FAILED_METHOD;

    private static final Method UPLOAD_SUCCEEDED_METHOD;

    private static final Method UPLOAD_STARTED_METHOD;

    private static final Method FILESIZE_LIMIT_EXCEEDED_METHOD;

    static {
        try {
            UPLOAD_FINISHED_METHOD = FinishedListener.class.getDeclaredMethod("uploadFinished", FinishedEvent.class);
            UPLOAD_FAILED_METHOD = FailedListener.class.getDeclaredMethod("uploadFailed", FailedEvent.class);
            UPLOAD_STARTED_METHOD = StartedListener.class.getDeclaredMethod("uploadStarted", StartedEvent.class);
            UPLOAD_SUCCEEDED_METHOD = SucceededListener.class.getDeclaredMethod("uploadSucceeded", SucceededEvent.class);
            FILESIZE_LIMIT_EXCEEDED_METHOD = FileSizeLimitExceededListener.class.getDeclaredMethod("fileSizeLimitExceeded", FileSizeLimitExceededEvent.class);
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException(
                    "Internal error finding methods in CubaFileUpload");
        }
    }

    /**
     * Adds the upload started event listener.
     *
     * @param listener
     *            the Listener to be added.
     */
    public void addStartedListener(StartedListener listener) {
        addListener(StartedEvent.class, listener, UPLOAD_STARTED_METHOD);
    }

    /**
     * Removes the upload started event listener.
     *
     * @param listener
     *            the Listener to be removed.
     */
    public void removeStartedListener(StartedListener listener) {
        removeListener(StartedEvent.class, listener, UPLOAD_STARTED_METHOD);
    }

    /**
     * Adds the upload received event listener.
     *
     * @param listener
     *            the Listener to be added.
     */
    public void addFinishedListener(FinishedListener listener) {
        addListener(FinishedEvent.class, listener, UPLOAD_FINISHED_METHOD);
    }

    /**
     * Removes the upload received event listener.
     *
     * @param listener
     *            the Listener to be removed.
     */
    public void removeFinishedListener(FinishedListener listener) {
        removeListener(FinishedEvent.class, listener, UPLOAD_FINISHED_METHOD);
    }

    /**
     * Adds the upload interrupted event listener.
     *
     * @param listener
     *            the Listener to be added.
     */
    public void addFailedListener(FailedListener listener) {
        addListener(FailedEvent.class, listener, UPLOAD_FAILED_METHOD);
    }

    /**
     * Removes the upload interrupted event listener.
     *
     * @param listener
     *            the Listener to be removed.
     */
    public void removeFailedListener(FailedListener listener) {
        removeListener(FailedEvent.class, listener, UPLOAD_FAILED_METHOD);
    }

    /**
     * Adds the upload success event listener.
     *
     * @param listener
     *            the Listener to be added.
     */
    public void addSucceededListener(SucceededListener listener) {
        addListener(SucceededEvent.class, listener, UPLOAD_SUCCEEDED_METHOD);
    }

    /**
     * Removes the upload success event listener.
     *
     * @param listener
     *            the Listener to be removed.
     */
    public void removeSucceededListener(SucceededListener listener) {
        removeListener(SucceededEvent.class, listener, UPLOAD_SUCCEEDED_METHOD);
    }

    public void addFileSizeLimitExceededListener(FileSizeLimitExceededListener listener) {
        addListener(FileSizeLimitExceededEvent.class, listener, FILESIZE_LIMIT_EXCEEDED_METHOD);
    }

    public void removeFileSizeLimitExceededListener(FileSizeLimitExceededListener listener) {
        removeListener(FileSizeLimitExceededEvent.class, listener, FILESIZE_LIMIT_EXCEEDED_METHOD);
    }
}