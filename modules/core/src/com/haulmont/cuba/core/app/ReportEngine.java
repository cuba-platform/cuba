/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.05.2009 18:29:30
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;

import java.io.File;
import java.net.URI;
import java.util.Map;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.haulmont.cuba.core.Locator;

import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * ReportEngine MBean implementation.
 * <p>
 * This MBean is intended for compiling and executing of Jasper Reports
 */
public class ReportEngine implements ReportEngineMBean, ReportEngineAPI
{
    private static final String SRC_EXT = ".jrxml";
    private static final String JASPER_EXT = ".jasper";

    private String srcRootPath;
    private String tmpRootPath;

    private Log log = LogFactory.getLog(ReportEngine.class);

    public void create() {
        String confUrl = System.getProperty("jboss.server.config.url");
        if (confUrl == null)
            throw new IllegalStateException("System property jboss.server.config.url not defined");
        srcRootPath = URI.create(confUrl).getPath() + "/";

        String tmpDir = System.getProperty("jboss.server.temp.dir");
        if (tmpDir == null)
            throw new IllegalStateException("System property jboss.server.temp.dir not defined");    
        tmpRootPath = tmpDir + "/";
    }

    public ReportEngineAPI getAPI() {
        return this;
    }

    public JasperReport getJasperReport(String name) {
        File jasperFile = compileJasperReport(name);
        try {
            return (JasperReport) JRLoader.loadObject(jasperFile);
        } catch (JRException e) {
            throw new RuntimeException("Unable to load compiled report " + name, e);
        }
    }

    public JasperPrint executeJasperReport(String name, Map<String, Object> params, JRDataSource dataSource) {
        JasperReport report = getJasperReport(name);
        JasperPrint print;
        try {
            if (dataSource != null) {
                print = JasperFillManager.fillReport(report, params, dataSource);
            } else {
                PersistenceConfigMBean mbean = Locator.lookupMBean(PersistenceConfigMBean.class, PersistenceConfigMBean.OBJECT_NAME);
                String s = mbean.getDatasourceName();
                DataSource ds = (DataSource) Locator.getJndiContext().lookup(s);
                Connection conn = ds.getConnection();
                try {
                    print = JasperFillManager.fillReport(report, params, conn);
                } finally {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        log.warn("Error closing connection", e);
                    }
                }
            }
        } catch (JRException e) {
            throw new RuntimeException(e);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return print;
    }

    private File compileJasperReport(String name) {
        File srcFile = new File(srcRootPath, name + SRC_EXT);
        if (!srcFile.exists())
            throw new RuntimeException("Report source file " + srcFile + " not found");

        long lm = srcFile.lastModified();

        File jasperFile = new File(tmpRootPath, name + JASPER_EXT);

        if (jasperFile.exists() && jasperFile.lastModified() >= lm) {
            log.debug("Compiled report " + name + " is up to date");
            return jasperFile;
        }

        log.debug("Compile report " + name);

        String destPath = tmpRootPath + name + JASPER_EXT;
        File destFile = new File(destPath);
        destFile.getParentFile().mkdirs();
        try {
            JasperCompileManager.compileReportToFile(srcFile.getAbsolutePath(), destPath);
        } catch (JRException e) {
            throw new RuntimeException("Unable to compile report " + name, e);
        }
        jasperFile.setLastModified(lm);
        return jasperFile;
    }
}
