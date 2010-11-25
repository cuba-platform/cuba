/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Yuryi Artamonov
 * Created: 18.11.2010 14:50:38
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.TimeProvider;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

@Service(FileUploadService.NAME)
public class FileUploadServiceBean implements FileUploadService {
    private Map<UUID, File> tempFiles = new HashMap<UUID, File>();

    /**
     * Upload buffer size.
     */
    private static final int BUFFER_SIZE = 64 * 1024; // 64k

    public UUID saveFile(byte[] data) throws FileStorageException {
        checkNotNull(data, "No file content");

        String tempDir = ConfigProvider.getConfig(ServerConfig.class).getServerTempDir();
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
                }
                finally {
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

    public UUID saveFile(InputStream stream, UploadProgressListener listener)
            throws FileStorageException {
        if (stream == null)
            throw new NullPointerException("Null input stream for save file");
        String tempDir = ConfigProvider.getConfig(ServerConfig.class).getServerTempDir();
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

    public File getFile(UUID fileId) {
        if (tempFiles.containsKey(fileId))
            return tempFiles.get(fileId);
        else
            return null;
    }

    public FileDescriptor getFileDescriptor(UUID fileId, String name) {
        File file = getFile(fileId);
        int fileSize = (int)file.length();

        FileDescriptor fDesc = new FileDescriptor();
        String ext = "";
        String fileName = name;
        int extIndex = fileName.lastIndexOf('.');
        if ((extIndex >= 0) && (extIndex < fileName.length()))
            ext = fileName.substring(extIndex + 1);

        fDesc.setSize(fileSize);
        fDesc.setExtension(ext);
        fDesc.setName(fileName);
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
            boolean res = file.delete();
            if (res)
                tempFiles.remove(fileId);
            else
                throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, file.getAbsolutePath());
        }
    }
}
