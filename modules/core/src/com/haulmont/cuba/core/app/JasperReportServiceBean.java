/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.05.2009 11:04:57
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.GlobalConfig;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@Service(JasperReportService.NAME)
public class JasperReportServiceBean implements JasperReportService {
    private static final String SRC_EXT = ".jrxml";
    private static final String JASPER_EXT = ".jasper";

    @Inject
    private FreemarkerProcessor freemarkerProcessor;

    private Log log = LogFactory.getLog(JasperReportServiceBean.class);

    public JasperReport getJasperReport(String name) {
        File jasperFile = compileJasperReport(name);
        try {
            return (JasperReport) JRLoader.loadObject(jasperFile);
        } catch (JRException e) {
            throw new RuntimeException("Unable to load compiled report " + name, e);
        }
    }

    private File compileJasperReport(String name) {
        GlobalConfig config = ConfigProvider.getConfig(GlobalConfig.class);
        String srcRootPath = config.getConfDir() + "/";
        String tmpRootPath = config.getTempDir() + "/";

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

    public String processFreemarkerTemplate(String name, Map<String, Object> params) {
        return processFreemarkerTemplate(name, params, null);
    }

    public String processFreemarkerTemplate(String name, Map<String, Object> params, String outputEncoding) {
        log.debug("Processing " + name);
        return freemarkerProcessor.processTemplate(name, params, outputEncoding);
    }

    public JasperPrint executeJasperReport(String name, Map<String, Object> params) {
        return executeJasperReport(name, params, null);
    }

    public JasperPrint executeJasperReport(String name, Map<String, Object> params, JRDataSource dataSource) {
        JasperPrint print;
        Transaction tx = Locator.createTransaction();
        try {
            log.debug("Executing report " + name);

            JasperReport report = getJasperReport(name);
            try {
                if (dataSource != null) {
                    print = JasperFillManager.fillReport(report, params, dataSource);
                } else {
                    DataSource ds = Locator.getDataSource();
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
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            tx.commit();
        } finally {
            tx.end();
        }
        return print;
    }
}
