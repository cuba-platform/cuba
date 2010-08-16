/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Valery Novikov
 * Created: 16.08.2010 14:08:48
 *
 * $Id$
 */

package com.haulmont.cuba.web.sys;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class StaticDownloadServlet extends HttpServlet {

    private static Log log = LogFactory.getLog(FileDownloadServlet.class);

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String fileName = request.getPathInfo();
        File file = new File(com.haulmont.cuba.core.sys.AppContext.getProperty("cuba.static") + fileName);
        if (file.exists()) {
            byte[] fileBArray = new byte[(int) file.length()];
            FileInputStream fis = new FileInputStream(file);
            fis.read(fileBArray);
            fis.close();
            ServletOutputStream os = response.getOutputStream();
            os.write(fileBArray, 0, fileBArray.length);
            os.flush();
            os.close();
        } else {
            error(response);
            log.error("Invalid url: " + request.getContextPath());
        }
    }

    private void error(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

}
