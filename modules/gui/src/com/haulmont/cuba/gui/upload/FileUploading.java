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

import com.haulmont.bali.util.StreamUtils.LazySupplier;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.executors.TaskLifeCycle;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

@Component(FileUploadingAPI.NAME)
public class FileUploading implements FileUploadingAPI, FileUploadingMBean {

    private final Logger log = LoggerFactory.getLogger(FileUploading.class);

    protected Map<UUID, File> tempFiles = new ConcurrentHashMap<>();

    /**
     * Upload buffer size.
     * Default: 64 KB
     */
    protected static final int BUFFER_SIZE = 64 * 1024;

    protected String tempDir;

    @Inject
    protected UuidSource uuidSource;

    @Inject
    protected TimeSource timeSource;

    @Inject
    protected FileLoader fileLoader;

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

            try (FileOutputStream os = new FileOutputStream(file)) {
                os.write(data);
            }
            tempFiles.put(uuid, file);
        } catch (RuntimeException | IOException e) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, file.getAbsolutePath());
        }

        return uuid;
    }

    @Override
    public UUID saveFile(InputStream stream, UploadProgressListener listener) throws FileStorageException {
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
        } catch (InterruptedException e) {
            // should never happen
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fileDescr.getId().toString());
        }

        deleteFile(fileId);
    }

    protected void uploadFileIntoStorage(UUID fileId, FileDescriptor fileDescr,
                                         @Nullable UploadToStorageProgressListener listener)
            throws FileStorageException, InterruptedException {

        checkNotNullArgument(fileDescr);

        File file = getFile(fileId);
        if (file == null) {
            throw new FileStorageException(FileStorageException.Type.FILE_NOT_FOUND, fileDescr.getName());
        }

        long fileSize = file.length();

        LazySupplier<InputStream> inputStreamSupplier = LazySupplier.of(() -> {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new FileLoader.InputStreamSupplierException("Temp file is not found " + file.getAbsolutePath());
            }
        });

        if (listener != null) {
            @SuppressWarnings("UnnecessaryLocalVariable")
            UploadToStorageProgressListener nonnullListener = listener;

            fileLoader.saveStream(fileDescr, inputStreamSupplier, transferredBytes -> {
                try {
                    nonnullListener.progressChanged(fileId, transferredBytes, fileSize);
                } catch (InterruptedException ie) {
                    // if thread is already interrupted we will restore interrupted flag
                    Thread.currentThread().interrupt();
                }
            });
        } else {
            fileLoader.saveStream(fileDescr, inputStreamSupplier);
        }

        if (inputStreamSupplier.supplied()) {
            IOUtils.closeQuietly(inputStreamSupplier.get());
        }
    }

    @Override
    public FileDescriptor putFileIntoStorage(final TaskLifeCycle<Long> taskLifeCycle)
            throws FileStorageException, InterruptedException {

        checkNotNullArgument(taskLifeCycle);

        UUID fileId = (UUID) taskLifeCycle.getParams().get("fileId");
        String fileName = (String) taskLifeCycle.getParams().get("fileName");

        checkNotNull(fileId);
        checkNotNull(fileName);

        UploadToStorageProgressListener progressListener = (fileId1, uploadedBytes, totalBytes) ->
                taskLifeCycle.publish(uploadedBytes);

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