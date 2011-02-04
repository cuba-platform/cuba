/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 27.11.2009 18:50:17
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.app.EntityLogMBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@ManagedBean("cuba_CachingFacade")
public class CachingFacade implements CachingFacadeMBean {

    public void clearGroovyCache() {
        ScriptingProvider.clearCache();
    }

    public void clearMessagesCache() {
        MessageProvider.clearCache();
    }

    public void clearResourceRepositoryCache() {
        Locator.lookupMBean(ResourceRepositoryMBean.class).evictAll();
    }

    public void clearConfigStorageCache() {
        Locator.lookupMBean(ConfigStorageMBean.class).clearCache();
    }

    public void clearEntityLogCache() {
        Locator.lookupMBean(EntityLogMBean.class).invalidateCache();
    }

    public void clearStorageTempDirectory() {
        try {
            FileUploadingApi uploadingApi = Locator.lookup(FileUploadingApi.NAME);
            ServerConfig config = ConfigProvider.getConfig(ServerConfig.class);
            File dir = new File(config.getServerTempDir());
            File[] files = dir.listFiles();
            Date currentDate = TimeProvider.currentTimestamp();
            for (File file : files) {
                Date fileDate = new Date(file.lastModified());
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(fileDate);
                calendar.add(Calendar.DAY_OF_YEAR, 2);
                if (currentDate.compareTo(calendar.getTime()) > 0) {
                    uploadingApi.deleteFileLink(file.getAbsolutePath());
                    if (!file.delete())
                        throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, file.getAbsolutePath());
                }
            }
        } catch (Exception ex) {
            LogFactory.getLog(getClass()).error(ex.getMessage(), ex);
        }
    }
}
