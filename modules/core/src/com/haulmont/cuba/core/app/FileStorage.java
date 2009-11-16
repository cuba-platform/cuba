/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 30.10.2009 14:14:09
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import static com.google.common.base.Preconditions.checkNotNull;
import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.core.global.View;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.net.URLEncoder;

public class FileStorage implements FileStorageMBean, FileStorageAPI {

    private Log log = LogFactory.getLog(FileStorage.class);

    public FileStorageAPI getAPI() {
        return this;
    }

    public String getStoragePath() {
        String storagePath = ConfigProvider.getConfig(FileStorageConfig.class).getFileStorageDir();
        if (StringUtils.isBlank(storagePath)) {
            String dataDir = ConfigProvider.getConfig(ServerConfig.class).getServerDataDir();
            storagePath = dataDir + "/filestorage/";
        }
        return storagePath;
    }

    public void saveFile(FileDescriptor fileDescr, byte[] data) throws FileStorageException {
        checkNotNull(fileDescr, "No file descriptor");
        checkNotNull(fileDescr.getCreateDate(), "Empty creation date");
        checkNotNull(data, "No file content");

        File dir = getStorageDir(fileDescr.getCreateDate());

        File file = new File(dir, fileDescr.getFileName());
        if (file.exists()) {
            throw new FileStorageException(FileStorageException.Type.FILE_ALREADY_EXISTS, file.getAbsolutePath());
        }

        try {
            FileOutputStream os = new FileOutputStream(file);
            try {
                os.write(data);
            } finally {
                os.close();
            }
            writeLog(fileDescr, file);
        } catch (IOException e) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, file.getAbsolutePath(), e);
        }
    }

    private synchronized void writeLog(FileDescriptor fileDescr, File file) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        StringBuilder sb = new StringBuilder();
        sb.append(df.format(TimeProvider.currentTimestamp())).append(" ");
        sb.append("[").append(SecurityProvider.currentUserSession().getUser()).append("] ");
        sb.append("CREATE").append(" ");
        sb.append("\"").append(file.getAbsolutePath()).append("\" ");
        sb.append("\"").append(fileDescr.getName()).append("\"\n");

        File logFile = new File(getStoragePath(), "storage.log");
        try {
            FileOutputStream fos = new FileOutputStream(logFile, true);
            try {
                IOUtils.write(sb.toString(), fos, "UTF-8");
            } finally {
                fos.close();
            }
        } catch (IOException e) {
            log.error("Unable to write log", e);
        }
    }

    public void removeFile(FileDescriptor fileDescr) throws FileStorageException {
        checkNotNull(fileDescr, "No file descriptor");
        checkNotNull(fileDescr.getCreateDate(), "Empty creation date");

        File dir = getStorageDir(fileDescr.getCreateDate());

        File file = new File(dir, fileDescr.getFileName());
        if (!file.delete())
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, "Unable to delete file " + file.getAbsolutePath());
    }

    public byte[] loadFile(FileDescriptor fileDescr) throws FileStorageException {
        checkNotNull(fileDescr, "No file descriptor");
        checkNotNull(fileDescr.getCreateDate(), "Empty creation date");

        File dir = getStorageDir(fileDescr.getCreateDate());

        File file = new File(dir, fileDescr.getFileName());
        if (!file.exists())
            throw new FileStorageException(FileStorageException.Type.FILE_NOT_FOUND, file.getAbsolutePath());

        try {
            return FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, file.getAbsolutePath(), e);
        }
    }

    private File getStorageDir(Date createDate) {
        String storageDir = ConfigProvider.getConfig(FileStorageConfig.class).getFileStorageDir();
        if (StringUtils.isBlank(storageDir)) {
            String dataDir = ConfigProvider.getConfig(ServerConfig.class).getServerDataDir();
            storageDir = dataDir + "/filestorage/";
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(createDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        File dir = new File(storageDir + year + "/"
                + StringUtils.leftPad(String.valueOf(month), 2, '0') + "/"
                + StringUtils.leftPad(String.valueOf(day), 2, '0'));

        dir.mkdirs();
        return dir;
    }

    public String findInvalidDescriptors() {
        StringBuilder sb = new StringBuilder();
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            Query query = em.createQuery("select fd from core$FileDescriptor fd");
            List<FileDescriptor> fileDescriptors = query.getResultList();
            for (FileDescriptor fileDescriptor : fileDescriptors) {
                File dir = getStorageDir(fileDescriptor.getCreateDate());
                File file = new File(dir, fileDescriptor.getFileName());
                if (!file.exists()) {
                    sb.append(fileDescriptor.getId())
                            .append(", ")
                            .append(fileDescriptor.getName())
                            .append(", ")
                            .append(fileDescriptor.getCreateDate())
                            .append("\n");
                }
            }
            tx.commit();
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        } finally {
            tx.end();
        }
        return sb.toString();
    }

    public String findInvalidFiles() {
        StringBuilder sb = new StringBuilder();

        String storagePath = getStoragePath();
        File storageFolder = new File(storagePath);
        if (!storageFolder.exists())
            return ExceptionUtils.getStackTrace(
                    new FileStorageException(FileStorageException.Type.FILE_NOT_FOUND, storageFolder.getAbsolutePath()));

        Collection<File> systemFiles = FileUtils.listFiles(storageFolder, null, true);
        Collection<File> filesInRootFolder = FileUtils.listFiles(storageFolder, null, false);
        //remove files of root storage folder (e.g. storage.log) from files collection
        systemFiles.removeAll(filesInRootFolder);

        List<FileDescriptor> fileDescriptors = new ArrayList<FileDescriptor>();
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            Query query = em.createQuery("select fd from core$FileDescriptor fd");
            fileDescriptors = query.getResultList();
            tx.commit();
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        } finally {
            tx.end();
        }

        Set<String> descriptorsFileNames = new HashSet<String>();
        for (FileDescriptor fileDescriptor : fileDescriptors) {
            descriptorsFileNames.add(fileDescriptor.getFileName());
        }

        for (File file : systemFiles) {
            if (!descriptorsFileNames.contains(file.getName()))
                //Encode file path if it contains non-ASCII characters
                if (!file.getPath().matches("\\p{ASCII}+")) {
                    try {
                        String encodedFilePath = URLEncoder.encode(file.getPath(), "utf-8");
                        sb.append(encodedFilePath).append("\n");
                    } catch (UnsupportedEncodingException e) {
                        return ExceptionUtils.getStackTrace(e);
                    }
                } else {
                    sb.append(file.getPath()).append("\n");
                }
        }

        return sb.toString();
    }


//    public void updateFileExt(FileDescriptor fileDescr) throws FileStorageException {
//        checkNotNull(fileDescr, "No file descriptor");
//        checkNotNull(fileDescr.getCreateDate(), "Empty creation date");
//
//        final String fileName = fileDescr.getId().toString();
//
//        File dir = getStorageDir(fileDescr.getCreateDate());
//
//        String[] fileNames = dir.list(new FilenameFilter() {
//            public boolean accept(File dir, String name) {
//                int i = name.lastIndexOf('.');
//                if (i > -1)
//                    name = StringUtils.substring(name, 0, i);
//                return fileName.equalsIgnoreCase(name);
//            }
//        });
//        if (fileNames.length == 0)
//            throw new FileStorageException(FileStorageException.Type.FILE_NOT_FOUND, dir.getAbsolutePath() + "/" + fileName);
//        if (fileNames.length > 1)
//            throw new FileStorageException(FileStorageException.Type.MORE_THAN_ONE_FILE, dir.getAbsolutePath() + "/" + fileName);
//        if (fileDescr.getFileName().equalsIgnoreCase(fileNames[0]))
//            return;
//
//        File file = new File(dir, fileNames[0]);
//        File newFile = new File(dir, fileDescr.getFileName());
//        if (!file.renameTo(newFile))
//            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, "Unable to rename file " + file.getAbsolutePath());
//    }
}
