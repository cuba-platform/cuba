/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Artamonov Yuryi
 * Created: 04.02.11 15:07
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.TimeProvider;

import javax.annotation.ManagedBean;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkNotNull;

@ManagedBean(FileUploadingApi.NAME)
public class FileUploading extends ManagementBean implements FileUploadingApi, FileUploadingMBean {

    private Map<UUID, File> tempFiles = new ConcurrentHashMap<UUID, File>();

    /**
     * Upload buffer size.
     * Default: 64 KB
     */
    private static final int BUFFER_SIZE = 64 * 1024;

    public UUID saveFile(byte[] data) throws FileStorageException {
        checkNotNull(data, "No file content");

        String tempDir = ConfigProvider.getConfig(GlobalConfig.class).getTempDir();
        UUID uuid = UUID.randomUUID();
        File dir = new File(tempDir);
        File file = new File(dir, uuid.toString());
        try {
            if (file.exists()) {
                throw new FileStorageException(FileStorageException.Type.FILE_ALREADY_EXISTS, file.getAbsolutePath());
            }
            FileOutputStream os = new FileOutputStream(file);

            try {
                boolean failed = false;
                try {
                    os.write(data);
                } catch (Exception ex) {
                    failed = true;
                } finally {
                    os.close();
                    if (!failed)
                        tempFiles.put(uuid, file);
                }
            } catch (IOException e) {
                throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, file.getAbsolutePath(), e);
            }
        } catch (Exception e) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, file.getAbsolutePath());
        }

        return uuid;
    }

    public UUID saveFile(InputStream stream, FileUploadService.UploadProgressListener listener)
            throws FileStorageException {
        if (stream == null)
            throw new NullPointerException("Null input stream for save file");
        String tempDir = ConfigProvider.getConfig(GlobalConfig.class).getTempDir();
        UUID uuid = UUID.randomUUID();
        File dir = new File(tempDir);
        File file = new File(dir, uuid.toString());
        if (file.exists()) {
            throw new FileStorageException(FileStorageException.Type.FILE_ALREADY_EXISTS, file.getAbsolutePath());
        }
        try {
            FileOutputStream fileOutput = new FileOutputStream(file);
            boolean failed = false;
            try {
                byte buffer[] = new byte[BUFFER_SIZE];
                int bytesRead = 0;
                int totalBytes = 0;
                while ((bytesRead = stream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bytesRead);
                    totalBytes += bytesRead;
                    if (listener != null)
                        listener.progressChanged(uuid, totalBytes);
                }
            } catch (IOException ex) {
                failed = true;
                throw ex;
            } finally {
                fileOutput.close();

                if (!failed)
                    tempFiles.put(uuid, file);
            }
        } catch (Exception ex) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, file.getAbsolutePath(), ex);
        }
        return uuid;
    }

    public UUID createEmptyFile() throws FileStorageException {
        UUID uuid = UUID.randomUUID();
        String tempDir = ConfigProvider.getConfig(GlobalConfig.class).getTempDir();
        File dir = new File(tempDir);
        File file = new File(dir, uuid.toString());
        try {
            if (file.exists()) {
                throw new FileStorageException(FileStorageException.Type.FILE_ALREADY_EXISTS, file.getAbsolutePath());
            }
            if (file.createNewFile())
                tempFiles.put(uuid, file);
            else
                throw new FileStorageException(FileStorageException.Type.FILE_ALREADY_EXISTS, file.getAbsolutePath());
        } catch (FileStorageException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new FileStorageException(FileStorageException.Type.FILE_ALREADY_EXISTS, file.getAbsolutePath());
        }
        return uuid;
    }

    public UUID getNewDescriptor() throws FileStorageException {
        UUID uuid = UUID.randomUUID();
        String tempDir = ConfigProvider.getConfig(GlobalConfig.class).getTempDir();
        File dir = new File(tempDir);
        File file = new File(dir, uuid.toString());
        try {
            if (file.exists()) {
                throw new FileStorageException(FileStorageException.Type.FILE_ALREADY_EXISTS, file.getAbsolutePath());
            }
            tempFiles.put(uuid, file);
        } catch (FileStorageException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new FileStorageException(FileStorageException.Type.FILE_ALREADY_EXISTS, file.getAbsolutePath());
        }
        return uuid;
    }

    public File getFile(UUID fileId) {
        if (tempFiles.containsKey(fileId))
            return tempFiles.get(fileId);
        else
            return null;
    }

    public FileDescriptor getFileDescriptor(UUID fileId, String name) {
        File file = getFile(fileId);
        int fileSize = (int) file.length();

        FileDescriptor fDesc = new FileDescriptor();
        String ext = "";
        int extIndex = name.lastIndexOf('.');
        if ((extIndex >= 0) && (extIndex < name.length()))
            ext = name.substring(extIndex + 1);

        fDesc.setSize(fileSize);
        fDesc.setExtension(ext);
        fDesc.setName(name);
        fDesc.setCreateDate(TimeProvider.currentTimestamp());

        return fDesc;
    }

    public InputStream loadFile(UUID fileId) throws FileNotFoundException {
        if (tempFiles.containsKey(fileId)) {
            File f = tempFiles.get(fileId);
            return new FileInputStream(f);
        } else
            return null;
    }

    public void deleteFile(UUID fileId) throws FileStorageException {
        if (tempFiles.containsKey(fileId)) {
            File file = tempFiles.get(fileId);
            if (file.exists()) {
                boolean res = file.delete();
                if (!res)
                    throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, file.getAbsolutePath());
            }
            tempFiles.remove(fileId);
        }
    }

    public void deleteFileLink(String fileName) {
        Iterator<Map.Entry<UUID, File>> iterator = tempFiles.entrySet().iterator();
        UUID forDelete = null;
        while ((iterator.hasNext()) && (forDelete == null)) {
            Map.Entry<UUID, File> fileEntry = iterator.next();
            if (fileEntry.getValue().getAbsolutePath().equals(fileName)) {
                forDelete = fileEntry.getKey();
            }
        }
        if (forDelete != null)
            tempFiles.remove(forDelete);
    }

    public String showTempFiles() {
        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<UUID, File>> iterator = tempFiles.entrySet().iterator();
        while ((iterator.hasNext())) {
            Map.Entry<UUID, File> fileEntry = iterator.next();
            builder.append(fileEntry.getKey().toString()).append(" | ");
            Date lastModified = new Date(fileEntry.getValue().lastModified());
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            builder.append(formatter.format(lastModified)).append("\n");
        }
        return builder.toString();
    }
}