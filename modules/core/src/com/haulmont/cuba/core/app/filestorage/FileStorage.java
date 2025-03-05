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
package com.haulmont.cuba.core.app.filestorage;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.haulmont.cuba.core.app.FileStorageAPI;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

@Component(FileStorageAPI.NAME)
public class FileStorage implements FileStorageAPI {

    private static final Logger log = LoggerFactory.getLogger(FileStorage.class);

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected TimeSource timeSource;

    @Inject
    protected Configuration configuration;

    protected boolean isImmutableFileStorage;

    protected ExecutorService writeExecutor = Executors.newFixedThreadPool(5,
            new ThreadFactoryBuilder().setNameFormat("FileStorageWriter-%d").build());

    protected volatile File[] storageRoots;

    @PostConstruct
    public void init() {
        this.isImmutableFileStorage = configuration.getConfig(ServerConfig.class).getImmutableFileStorage();
    }

    /**
     * INTERNAL. Don't use in application code.
     */
    public File[] getStorageRoots() {
        if (storageRoots == null) {
            String conf = configuration.getConfig(ServerConfig.class).getFileStorageDir();
            if (StringUtils.isBlank(conf)) {
                String dataDir = configuration.getConfig(GlobalConfig.class).getDataDir();
                File dir = new File(dataDir, "filestorage");
                dir.mkdirs();
                storageRoots = new File[]{dir};
            } else {
                List<File> list = new ArrayList<>();
                for (String str : conf.split(",")) {
                    str = str.trim();
                    if (!StringUtils.isEmpty(str)) {
                        File file = new File(str);
                        if (!list.contains(file))
                            list.add(file);
                    }
                }
                storageRoots = list.toArray(new File[list.size()]);
            }
        }
        return storageRoots;
    }

    @Override
    public long saveStream(final FileDescriptor fileDescr, final InputStream inputStream) throws FileStorageException {
        checkFileDescriptor(fileDescr);

        File[] roots = getStorageRoots();

        // Store to primary storage

        checkStorageDefined(roots, fileDescr);
        checkPrimaryStorageAccessible(roots, fileDescr);

        File dir = getStorageDir(roots[0], fileDescr);
        dir.mkdirs();
        checkDirectoryExists(dir);

        final File file = new File(dir, getFileName(fileDescr));
        checkFileExists(file);

        ServerConfig serverConfig = configuration.getConfig(ServerConfig.class);
        long maxAllowedSize = serverConfig.getFileStorageMaxFileSize().toBytes();
        long size = 0;
        OutputStream os = null;
        try {
            os = FileUtils.openOutputStream(file);
            size = IOUtils.copyLarge(inputStream, os, 0, maxAllowedSize);

            if (size >= maxAllowedSize) {
                if (inputStream.read() != IOUtils.EOF) {
                    os.close();
                    if (file.exists()) {
                        if (!file.delete()) {
                            log.warn("Failed to delete an incorrectly uploaded file '{}'. " +
                                            "File was to large and has been rejected but already loaded part was not deleted.",
                                    file.getAbsolutePath());
                        }
                    }
                    throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION,
                            String.format("File is too large: '%s'. Max file size = %s MB is exceeded but there are unread bytes left.",
                                    file.getAbsolutePath(),
                                    serverConfig.getFileStorageMaxFileSize().toMegabytes()));
                }
            }
            os.flush();
            writeLog(file, false);
        } catch (IOException e) {
            IOUtils.closeQuietly(os);
            FileUtils.deleteQuietly(file);

            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, file.getAbsolutePath(), e);
        } finally {
            IOUtils.closeQuietly(os);
        }

        // Copy file to secondary storages asynchronously

        final SecurityContext securityContext = AppContext.getSecurityContext();
        for (int i = 1; i < roots.length; i++) {
            if (!roots[i].exists()) {
                log.error("Error saving {} into {} : directory doesn't exist", fileDescr, roots[i]);
                continue;
            }

            File copyDir = getStorageDir(roots[i], fileDescr);
            final File fileCopy = new File(copyDir, getFileName(fileDescr));

            writeExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        AppContext.setSecurityContext(securityContext);
                        FileUtils.copyFile(file, fileCopy, true);
                        writeLog(fileCopy, false);
                    } catch (Exception e) {
                        log.error("Error saving {} into {} : {}", fileDescr, fileCopy.getAbsolutePath(), e.getMessage());
                    } finally {
                        AppContext.setSecurityContext(null);
                    }
                }
            });
        }

        return size;
    }

    protected void checkFileExists(File file) throws FileStorageException {
        if (file.exists() && isImmutableFileStorage)
            throw new FileStorageException(FileStorageException.Type.FILE_ALREADY_EXISTS, file.getAbsolutePath());
    }

    protected void checkDirectoryExists(File dir) throws FileStorageException {
        if (!dir.exists())
            throw new FileStorageException(FileStorageException.Type.STORAGE_INACCESSIBLE, dir.getAbsolutePath());
    }

    protected void checkPrimaryStorageAccessible(File[] roots, FileDescriptor fileDescr) throws FileStorageException {
        if (!roots[0].exists()) {
            log.error("Inaccessible primary storage at {}", roots[0]);
            throw new FileStorageException(FileStorageException.Type.STORAGE_INACCESSIBLE, fileDescr.getId().toString());
        }
    }

    protected void checkStorageDefined(File[] roots, FileDescriptor fileDescr) throws FileStorageException {
        if (roots.length == 0) {
            log.error("No storage directories defined");
            throw new FileStorageException(FileStorageException.Type.STORAGE_INACCESSIBLE, fileDescr.getId().toString());
        }
    }

    @Override
    public void saveFile(final FileDescriptor fileDescr, final byte[] data) throws FileStorageException {
        checkNotNullArgument(data, "File content is null");
        saveStream(fileDescr, new ByteArrayInputStream(data));
    }

    protected synchronized void writeLog(File file, boolean remove) {
        File rootDir;
        try {
            rootDir = file.getParentFile().getParentFile().getParentFile().getParentFile();
        } catch (NullPointerException e) {
            log.error("Unable to write log: invalid file storage structure", e);
            return;
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        UserSession userSession = userSessionSource.getUserSession();
        String userLogin = userSession.getUser().getLogin();
        String userId = userSession.getUser().getId().toString();

        StringBuilder sb = new StringBuilder();
        sb.append(df.format(timeSource.currentTimestamp())).append(" ");

        sb.append("[").append(userLogin).append("--").append(userId).append("] ");
        sb.append(remove ? "REMOVE" : "CREATE").append(" ");
        sb.append("\"").append(file.getAbsolutePath()).append("\"\n");

        File logFile = new File(rootDir, "storage.log");
        try {
            try (FileOutputStream fos = new FileOutputStream(logFile, true)) {
                IOUtils.write(sb.toString(), fos, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.error("Unable to write log", e);
        }
    }

    @Override
    public void removeFile(FileDescriptor fileDescr) throws FileStorageException {
        checkFileDescriptor(fileDescr);

        File[] roots = getStorageRoots();
        if (roots.length == 0) {
            log.error("No storage directories defined");
            return;
        }

        for (File root : roots) {
            File dir = getStorageDir(root, fileDescr);
            File file = new File(dir, getFileName(fileDescr));
            if (file.exists()) {
                if (!file.delete()) {
                    throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, "Unable to delete file " + file.getAbsolutePath());
                } else {
                    writeLog(file, true);
                }
            }
        }
    }

    protected void checkFileDescriptor(FileDescriptor fd) {
        if (fd == null || fd.getCreateDate() == null) {
            throw new IllegalArgumentException("A FileDescriptor instance with populated 'createDate' attribute must be provided");
        }
    }

    @Override
    public InputStream openStream(FileDescriptor fileDescr) throws FileStorageException {
        checkFileDescriptor(fileDescr);

        File[] roots = getStorageRoots();
        if (roots.length == 0) {
            log.error("No storage directories available");
            throw new FileStorageException(FileStorageException.Type.FILE_NOT_FOUND, fileDescr.getId().toString());
        }

        InputStream inputStream = null;
        for (File root : roots) {
            File dir = getStorageDir(root, fileDescr);

            File file = new File(dir, getFileName(fileDescr));
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
            throw new FileStorageException(FileStorageException.Type.FILE_NOT_FOUND, fileDescr.getId().toString());
    }

    @Override
    public byte[] loadFile(FileDescriptor fileDescr) throws FileStorageException {
        InputStream inputStream = openStream(fileDescr);
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fileDescr.getId().toString(), e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    @Override
    public boolean fileExists(FileDescriptor fileDescr) {
        checkFileDescriptor(fileDescr);

        File[] roots = getStorageRoots();
        for (File root : roots) {
            File dir = getStorageDir(root, fileDescr);
            File file = new File(dir, getFileName(fileDescr));
            if (file.exists()) {
                return true;
            }
        }
        return false;
    }

    /**
     * INTERNAL. Don't use in application code.
     */
    public File getStorageDir(File rootDir, FileDescriptor fileDescriptor) {
        checkNotNullArgument(rootDir);
        checkNotNullArgument(fileDescriptor);

        Calendar cal = Calendar.getInstance();
        cal.setTime(fileDescriptor.getCreateDate());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return new File(rootDir, year + "/"
                + StringUtils.leftPad(String.valueOf(month), 2, '0') + "/"
                + StringUtils.leftPad(String.valueOf(day), 2, '0'));
    }

    public static String getFileName(FileDescriptor fileDescriptor) {
        return fileDescriptor.getId().toString() + "." + fileDescriptor.getExtension();
    }

    @PreDestroy
    protected void stopWriteExecutor() {
        writeExecutor.shutdown();
    }
}