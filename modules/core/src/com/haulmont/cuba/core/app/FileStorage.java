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

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.google.common.base.Preconditions.checkNotNull;

@ManagedBean(FileStorageAPI.NAME)
public class FileStorage implements FileStorageMBean, FileStorageAPI {

    @Inject
    private UserSessionSource userSessionSource;

    @Inject
    private Persistence persistence;

    private ExecutorService writeExecutor = Executors.newFixedThreadPool(5);

    private Log log = LogFactory.getLog(FileStorage.class);

    public File[] getStorageRoots() {
        String conf = ConfigProvider.getConfig(ServerConfig.class).getFileStorageDir();
        if (StringUtils.isBlank(conf)) {
            String dataDir = ConfigProvider.getConfig(GlobalConfig.class).getDataDir();
            File dir = new File(dataDir, "filestorage");
            dir.mkdirs();
            return new File[] {dir};
        } else {
            List<File> list = new ArrayList<File>();
            for (String str : conf.split(",")) {
                str = str.trim();
                if (!StringUtils.isEmpty(str)) {
                    File file = new File(str);
                    if (!list.contains(file))
                        list.add(file);
                }
            }
            return list.toArray(new File[list.size()]);
        }
    }

    @Override
    public void saveStream(final FileDescriptor fileDescr, final InputStream inputStream) throws FileStorageException {
        checkNotNull(fileDescr, "No file descriptor");
        checkNotNull(fileDescr.getCreateDate(), "Empty creation date");

        File[] roots = getStorageRoots();

        // Store to primary storage

        if (roots.length == 0) {
            log.error("No storage directories defined");
            throw new FileStorageException(FileStorageException.Type.STORAGE_INACCESSIBLE, fileDescr.getFileName());
        }
        if (!roots[0].exists()) {
            log.error("Inaccessible primary storage at " + roots[0]);
            throw new FileStorageException(FileStorageException.Type.STORAGE_INACCESSIBLE, fileDescr.getFileName());
        }

        File dir = getStorageDir(roots[0], fileDescr.getCreateDate());
        dir.mkdirs();
        if (!dir.exists())
            throw new FileStorageException(FileStorageException.Type.STORAGE_INACCESSIBLE, dir.getAbsolutePath());

        final File file = new File(dir, fileDescr.getFileName());
        if (file.exists())
            throw new FileStorageException(FileStorageException.Type.FILE_ALREADY_EXISTS, file.getAbsolutePath());

        OutputStream os = null;
        try {
            os = FileUtils.openOutputStream(file);
            IOUtils.copy(inputStream, os);
            os.flush();
            writeLog(file, false);
        } catch (IOException e) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, file.getAbsolutePath(), e);
        } finally {
            IOUtils.closeQuietly(os);
        }

        // Copy file to secondary storages asynchronously

        final SecurityContext securityContext = AppContext.getSecurityContext();
        for (int i = 1; i < roots.length; i++) {
            if (!roots[i].exists()) {
                log.error("Error saving " + fileDescr + " into " + roots[i] + " : directory doesn't exist");
                continue;
            }

            File copyDir = getStorageDir(roots[i], fileDescr.getCreateDate());
            final File fileCopy = new File(copyDir, fileDescr.getFileName());

            writeExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        AppContext.setSecurityContext(securityContext);
                        FileUtils.copyFile(file, fileCopy, true);
                        writeLog(fileCopy, false);
                    } catch (Exception e) {
                        log.error("Error saving " + fileDescr + " into " + fileCopy.getAbsolutePath() + " : "
                                + e.getMessage());
                    }
                }
            });
        }
    }

    @Override
    public void saveFile(final FileDescriptor fileDescr, final byte[] data) throws FileStorageException {
        checkNotNull(data, "No file content");
        saveStream(fileDescr, new ByteArrayInputStream(data));
    }

    private synchronized void writeLog(File file, boolean remove) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        StringBuilder sb = new StringBuilder();
        sb.append(df.format(TimeProvider.currentTimestamp())).append(" ");
        sb.append("[").append(userSessionSource.getUserSession().getUser()).append("] ");
        sb.append(remove ? "REMOVE" : "CREATE").append(" ");
        sb.append("\"").append(file.getAbsolutePath()).append("\"\n");

        File rootDir;
        try {
            rootDir = file.getParentFile().getParentFile().getParentFile().getParentFile();
        } catch (NullPointerException e) {
            log.error("Unable to write log: invalid file storage structure", e);
            return;
        }
        File logFile = new File(rootDir, "storage.log");
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

    @Override
    public void removeFile(FileDescriptor fileDescr) throws FileStorageException {
        checkNotNull(fileDescr, "No file descriptor");
        checkNotNull(fileDescr.getCreateDate(), "Empty creation date");

        File[] roots = getStorageRoots();
        if (roots.length == 0) {
            log.error("No storage directories defined");
            return;
        }

        for (File root : roots) {
            File dir = getStorageDir(root, fileDescr.getCreateDate());
            File file = new File(dir, fileDescr.getFileName());
            if (file.exists()) {
                if (!file.delete())
                    throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, "Unable to delete file " + file.getAbsolutePath());
                else
                    writeLog(file, true);
            }
        }
    }

    @Override
    public InputStream openFileInputStream(FileDescriptor fileDescr) throws FileStorageException {
        checkNotNull(fileDescr, "No file descriptor");
        checkNotNull(fileDescr.getCreateDate(), "Empty creation date");

        File[] roots = getStorageRoots();
        if (roots.length == 0) {
            log.error("No storage directories available");
            throw new FileStorageException(FileStorageException.Type.FILE_NOT_FOUND, fileDescr.getFileName());
        }

        InputStream inputStream = null;
        for (File root : roots) {
            File dir = getStorageDir(root, fileDescr.getCreateDate());

            File file = new File(dir, fileDescr.getFileName());
            if (!file.exists()) {
                log.error("File " + file + " not found");
                continue;
            }

            try {
                inputStream = FileUtils.openInputStream(file);
                break;
            } catch (IOException e) {
                log.error("Error opening input stream for " + file, e);
            }
        }
        if (inputStream != null)
            return inputStream;
        else
            throw new FileStorageException(FileStorageException.Type.FILE_NOT_FOUND, fileDescr.getFileName());
    }

    @Override
    public byte[] loadFile(FileDescriptor fileDescr) throws FileStorageException {
        InputStream inputStream = openFileInputStream(fileDescr);
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fileDescr.getFileName(), e);
        }
    }

    @Override
    public void putFile(final FileDescriptor fileDescr, final File file) throws FileStorageException {
        checkNotNull(fileDescr, "No file descriptor");
        checkNotNull(fileDescr.getCreateDate(), "Empty creation date");
        checkNotNull(file, "No file");

        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            saveStream(fileDescr, inputStream);
        } catch (FileNotFoundException e) {
            throw new FileStorageException(FileStorageException.Type.FILE_NOT_FOUND, file.getAbsolutePath(), e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    private File getStorageDir(File rootDir, Date createDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(createDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return new File(rootDir, year + "/"
                + StringUtils.leftPad(String.valueOf(month), 2, '0') + "/"
                + StringUtils.leftPad(String.valueOf(day), 2, '0'));
    }

    @Override
    public String findInvalidDescriptors() {
        File[] roots = getStorageRoots();
        if (roots.length == 0)
            return "No storage directories defined";

        StringBuilder sb = new StringBuilder();
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            Query query = em.createQuery("select fd from sys$FileDescriptor fd");
            List<FileDescriptor> fileDescriptors = query.getResultList();
            for (FileDescriptor fileDescriptor : fileDescriptors) {
                File dir = getStorageDir(roots[0], fileDescriptor.getCreateDate());
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

    @Override
    public String findInvalidFiles() {
        File[] roots = getStorageRoots();
        if (roots.length == 0)
            return "No storage directories defined";

        StringBuilder sb = new StringBuilder();

        File storageFolder = roots[0];
        if (!storageFolder.exists())
            return ExceptionUtils.getStackTrace(
                    new FileStorageException(FileStorageException.Type.FILE_NOT_FOUND, storageFolder.getAbsolutePath()));

        Collection<File> systemFiles = FileUtils.listFiles(storageFolder, null, true);
        Collection<File> filesInRootFolder = FileUtils.listFiles(storageFolder, null, false);
        //remove files of root storage folder (e.g. storage.log) from files collection
        systemFiles.removeAll(filesInRootFolder);

        List<FileDescriptor> fileDescriptors = new ArrayList<FileDescriptor>();
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            Query query = em.createQuery("select fd from sys$FileDescriptor fd");
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
}
