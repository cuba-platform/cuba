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

package com.haulmont.cuba.gui.upload;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.remoting.ClusterInvocationSupport;
import com.haulmont.cuba.core.sys.remoting.LocalFileExchangeService;
import com.haulmont.cuba.gui.executors.TaskLifeCycle;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.inject.Inject;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

@Component(FileUploadingAPI.NAME)
public class FileUploading implements FileUploadingAPI, FileUploadingMBean {

    protected Map<UUID, File> tempFiles = new ConcurrentHashMap<>();

    /**
     * Upload buffer size.
     * Default: 64 KB
     */
    protected static final int BUFFER_SIZE = 64 * 1024;

    protected Logger log = LoggerFactory.getLogger(getClass());

    protected static final String CORE_FILE_UPLOAD_CONTEXT = "/upload";

    protected String tempDir;

    // Using injection by name here, because an application project can define several instances
    // of ClusterInvocationSupport type to work with different middleware blocks
    @Resource(name = ClusterInvocationSupport.NAME)
    protected ClusterInvocationSupport clusterInvocationSupport;

    @Inject
    protected UuidSource uuidSource;

    @Inject
    protected TimeSource timeSource;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    public void setConfiguration(Configuration configuration) {
        tempDir = configuration.getConfig(GlobalConfig.class).getTempDir();
    }

    @Override
    public UUID saveFile(byte[] data) throws FileStorageException {
        checkNotNullArgument(data, "No file content");

        UUID uuid = uuidSource.createUuid();
        File dir = new File(tempDir);
        File file = new File(dir, uuid.toString());
        try {
            if (file.exists()) {
                throw new FileStorageException(FileStorageException.Type.FILE_ALREADY_EXISTS, file.getAbsolutePath());
            }

            boolean failed = false;
            try (FileOutputStream os = new FileOutputStream(file)) {
                os.write(data);
            } catch (Exception ex) {
                failed = true;
            } finally {
                if (!failed) {
                    tempFiles.put(uuid, file);
                }
            }
        } catch (RuntimeException e) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, file.getAbsolutePath());
        }

        return uuid;
    }

    @Override
    public UUID saveFile(InputStream stream, UploadProgressListener listener)
            throws FileStorageException {

        checkNotNullArgument(stream, "Null input stream for save file");

        UUID uuid = uuidSource.createUuid();
        File dir = new File(tempDir);
        File file = new File(dir, uuid.toString());
        if (file.exists()) {
            throw new FileStorageException(FileStorageException.Type.FILE_ALREADY_EXISTS, file.getAbsolutePath());
        }

        try {
            boolean failed = false;
            try (FileOutputStream fileOutput = new FileOutputStream(file)) {
                byte buffer[] = new byte[BUFFER_SIZE];
                int bytesRead;
                int totalBytes = 0;
                while ((bytesRead = stream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bytesRead);
                    totalBytes += bytesRead;
                    if (listener != null)
                        listener.progressChanged(uuid, totalBytes);
                }
            } catch (Exception ex) {
                failed = true;
                throw ex;
            } finally {
                if (!failed)
                    tempFiles.put(uuid, file);
            }
        } catch (Exception ex) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, file.getAbsolutePath(), ex);
        }
        return uuid;
    }

    @Override
    public UUID createEmptyFile() throws FileStorageException {
        FileInfo fileInfo = createFileInternal();
        return fileInfo.getId();
    }

    @Override
    public FileInfo createFile() throws FileStorageException {
        return createFileInternal();
    }

    protected FileInfo createFileInternal() throws FileStorageException {
        UUID uuid = uuidSource.createUuid();
        File dir = new File(tempDir);
        File file = new File(dir, uuid.toString());

        if (file.exists()) {
            throw new FileStorageException(FileStorageException.Type.FILE_ALREADY_EXISTS, file.getAbsolutePath());
        }

        try {
            if (file.createNewFile())
                tempFiles.put(uuid, file);
            else
                throw new FileStorageException(FileStorageException.Type.FILE_ALREADY_EXISTS, file.getAbsolutePath());
        } catch (IOException ex) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, file.getAbsolutePath());
        }

        return new FileInfo(file, uuid);
    }

    @Override
    public UUID createNewFileId() throws FileStorageException {
        UUID uuid = uuidSource.createUuid();
        File dir = new File(tempDir);
        File file = new File(dir, uuid.toString());
        if (file.exists()) {
            throw new FileStorageException(FileStorageException.Type.FILE_ALREADY_EXISTS, file.getAbsolutePath());
        }
        tempFiles.put(uuid, file);
        return uuid;
    }

    @Override
    public File getFile(UUID fileId) {
        return tempFiles.get(fileId);
    }

    @Override
    public FileDescriptor getFileDescriptor(UUID fileId, String name) {
        File file = getFile(fileId);
        if (file == null) {
            return null;
        }
        Metadata metadata = AppBeans.get(Metadata.NAME);

        FileDescriptor fDesc = metadata.create(FileDescriptor.class);

        fDesc.setSize(file.length());
        fDesc.setExtension(FilenameUtils.getExtension(name));
        fDesc.setName(name);
        fDesc.setCreateDate(timeSource.currentTimestamp());

        return fDesc;
    }

    @Override
    public void deleteFile(UUID fileId) throws FileStorageException {
        File file = tempFiles.remove(fileId);
        if (file != null) {
            if (file.exists()) {
                boolean res = file.delete();
                if (!res)
                    log.warn("Could not delete temp file " + file.getAbsolutePath());
            }
        }
    }

    @Override
    public void deleteFileLink(String fileName) {
        Map<UUID, File> clonedFileMap = new HashMap<>(tempFiles);
        Iterator<Map.Entry<UUID, File>> iterator = clonedFileMap.entrySet().iterator();
        UUID forDelete = null;
        while ((iterator.hasNext()) && (forDelete == null)) {
            Map.Entry<UUID, File> fileEntry = iterator.next();
            if (fileEntry.getValue().getAbsolutePath().equals(fileName)) {
                forDelete = fileEntry.getKey();
            }
        }

        if (forDelete != null) {
            tempFiles.remove(forDelete);
        }
    }

    @Override
    public void putFileIntoStorage(UUID fileId, FileDescriptor fileDescr) throws FileStorageException {
        try {
            uploadFileIntoStorage(fileId, fileDescr, null);
        } catch (InterruptedIOException e) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fileDescr.getId().toString());
        }

        deleteFile(fileId);
    }

    protected void uploadFileIntoStorage(UUID fileId, FileDescriptor fileDescr,
                                         @Nullable UploadToStorageProgressListener listener)
            throws FileStorageException, InterruptedIOException {

        checkNotNullArgument(fileDescr);

        File file = getFile(fileId);
        if (file == null) {
            throw new FileStorageException(FileStorageException.Type.FILE_NOT_FOUND, fileDescr.getName());
        }

        String useLocalInvocation = AppContext.getProperty("cuba.useLocalServiceInvocation");
        if (Boolean.parseBoolean(useLocalInvocation)) {
            uploadLocally(fileDescr, file);
        } else {
            uploadWithServlet(fileId, fileDescr, listener, file);
        }
    }

    private void uploadLocally(FileDescriptor fileDescr, File file) {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            AppBeans.get(LocalFileExchangeService.NAME, LocalFileExchangeService.class)
                    .uploadFile(inputStream, fileDescr);
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while uploading file locally", e);
        }
    }

    private void uploadWithServlet(UUID fileId, FileDescriptor fileDescr, UploadToStorageProgressListener listener, File file) throws FileStorageException, InterruptedIOException {
        for (Iterator<String> iterator = clusterInvocationSupport.getUrlList().iterator(); iterator.hasNext(); ) {
            String url = iterator.next()
                    + CORE_FILE_UPLOAD_CONTEXT
                    + "?s=" + userSessionSource.getUserSession().getId()
                    + "&f=" + fileDescr.toUrlParam();

            HttpPost method = new HttpPost(url);
            FileEntity entity;
            if (listener != null) {
                entity = new FileStorageProgressEntity(file, "application/octet-stream", fileId, listener);
            } else {
                entity = new FileEntity(file, ContentType.APPLICATION_OCTET_STREAM);
            }

            method.setEntity(entity);

            HttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager();
            HttpClient client = HttpClientBuilder.create()
                    .setConnectionManager(connectionManager)
                    .build();
            try {
                HttpResponse response = client.execute(method);
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    break;
                } else {
                    log.debug("Unable to upload file to " + url + "\n" + response.getStatusLine());
                    if (statusCode == HttpStatus.SC_NOT_FOUND && iterator.hasNext()) {
                        log.debug("Trying next URL");
                    } else {
                        throw new FileStorageException(FileStorageException.Type.fromHttpStatus(statusCode), fileDescr.getName());
                    }
                }
            } catch (InterruptedIOException e) {
                log.trace("Uploading has been interrupted");
                throw e;
            } catch (IOException e) {
                log.debug("Unable to upload file to " + url + "\n" + e);
                if (iterator.hasNext()) {
                    log.debug("Trying next URL");
                } else {
                    throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fileDescr.getName(), e);
                }
            } finally {
                connectionManager.shutdown();
            }
        }
    }

    @Override
    public FileDescriptor putFileIntoStorage(final TaskLifeCycle<Long> taskLifeCycle)
            throws FileStorageException, InterruptedIOException {

        checkNotNullArgument(taskLifeCycle);

        UUID fileId = (UUID) taskLifeCycle.getParams().get("fileId");
        String fileName = (String) taskLifeCycle.getParams().get("fileName");

        checkNotNull(fileId);
        checkNotNull(fileName);

        UploadToStorageProgressListener progressListener = new UploadToStorageProgressListener() {
            @Override
            public void progressChanged(UUID fileId, long uploadedBytes, long totalBytes) throws InterruptedException {
                taskLifeCycle.publish(uploadedBytes);
            }
        };

        FileDescriptor fileDescriptor = getFileDescriptor(fileId, fileName);
        uploadFileIntoStorage(fileId, fileDescriptor, progressListener);

        deleteFile(fileId);

        return fileDescriptor;
    }

    @Override
    public void clearTempDirectory() {
        try {
            File dir = new File(tempDir);
            File[] files = dir.listFiles();
            if (files == null)
                throw new IllegalStateException("Not a directory: " + tempDir);
            Date currentDate = timeSource.currentTimestamp();
            for (File file : files) {
                Date fileDate = new Date(file.lastModified());
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(fileDate);
                calendar.add(Calendar.DAY_OF_YEAR, 2);
                if (currentDate.compareTo(calendar.getTime()) > 0) {
                    deleteFileLink(file.getAbsolutePath());
                    if (!file.delete()) {
                        log.warn(String.format("Could not remove temp file %s", file.getName()));
                    }
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    @Override
    public String showTempFiles() {
        StringBuilder builder = new StringBuilder();
        Map<UUID, File> clonedFileMap = new HashMap<>(tempFiles);
        for (Map.Entry<UUID, File> fileEntry : clonedFileMap.entrySet()) {
            builder.append(fileEntry.getKey().toString()).append(" | ");
            Date lastModified = new Date(fileEntry.getValue().lastModified());
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            builder.append(formatter.format(lastModified)).append("\n");
        }
        return builder.toString();
    }

    /**
     * Listener to be notified about the progress of uploading file from the temporary storage
     * into middleware FileStorage.
     */
    interface UploadToStorageProgressListener {
        /**
         * @param fileId        temporary file ID
         * @param uploadedBytes current uploaded bytes count
         * @param totalBytes    total contents size
         */
        void progressChanged(UUID fileId, long uploadedBytes, long totalBytes) throws InterruptedException;
    }
}