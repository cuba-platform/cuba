/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Fontanenko
 * Created: 07.05.2010 15:12:05
 *
 * $Id$
 */

package com.haulmont.cuba.report.formatters.tools;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.app.FileStorageService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import static com.haulmont.cuba.report.formatters.tools.ODTUnoConverter.*;
import com.sun.star.beans.PropertyValue;
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XStorable;
import com.sun.star.io.IOException;
import com.sun.star.io.XInputStream;
import com.sun.star.io.XOutputStream;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.text.XTextDocument;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.*;
import ooo.connector.BootstrapSocketConnector;

import java.io.File;

public class ODTHelper {
    // Get group of ODT objects
    public static ODTContextGroup createXComponentGroup(String openOfficePath) throws BootstrapException, com.sun.star.uno.Exception {
        XComponentContext xRemoteContext = new BootstrapSocketConnector(openOfficePath).connect();
        XMultiComponentFactory xRemoteServiceManager = xRemoteContext.getServiceManager();
        Object desktop = xRemoteServiceManager.createInstanceWithContext("com.sun.star.frame.Desktop", xRemoteContext);
        XDesktop xDesktop = asXDesktop(desktop);
        ODTContextGroup group = new ODTContextGroup(xRemoteContext,xRemoteServiceManager,xDesktop);
        return group;
    }

    public static XComponentLoader createXComponentLoader(String openOfficePath) throws BootstrapException, com.sun.star.uno.Exception {
        XComponentContext xRemoteContext = new BootstrapSocketConnector(openOfficePath).connect();
        XMultiComponentFactory xRemoteServiceManager = xRemoteContext.getServiceManager();
        Object desktop = xRemoteServiceManager.createInstanceWithContext("com.sun.star.frame.Desktop", xRemoteContext);
        XDesktop xDesktop = asXDesktop(desktop);
        return asXComponentLoader(xDesktop);
    }

    public static XInputStream getXInputStream(FileDescriptor fileDescriptor) {
        FileStorageService fss = Locator.lookup(FileStorageService.NAME);
        try {
            byte[] bytes = fss.loadFile(fileDescriptor);
            OOInputStream oois = new OOInputStream(bytes);
            return oois;
        } catch (FileStorageException e) {
            throw new RuntimeException(e);
        }
    }

    public static XComponent loadXComponent(XComponentLoader xComponentLoader, XInputStream inputStream) throws com.sun.star.lang.IllegalArgumentException, IOException {
        PropertyValue[] props = new PropertyValue[2];
        props[0] = new PropertyValue();
        props[1] = new PropertyValue();
        props[0].Name = "InputStream";
        props[0].Value = inputStream;
        props[1].Name = "Hidden";
        props[1].Value = true;
        return xComponentLoader.loadComponentFromURL("private:stream", "_blank", 0, props);
    }

    public static void saveXComponent(XComponent xComponent, XOutputStream xOutputStream, String filterName) throws IOException {
        PropertyValue[] props = new PropertyValue[2];
        props[0] = new PropertyValue();
        props[1] = new PropertyValue();
        props[0].Name = "OutputStream";
        props[0].Value = xOutputStream;
        props[1].Name = "FilterName";
        props[1].Value = filterName;
        XStorable xStorable = asXStorable(xComponent);
        xStorable.storeToURL("private:stream", props);
    }

    public static XComponent loadXComponent(XComponentLoader xComponentLoader, String sURL) throws com.sun.star.lang.IllegalArgumentException, IOException {
        PropertyValue[] loadProps = new PropertyValue[0];
        return xComponentLoader.loadComponentFromURL(sURL, "_blank", 0, loadProps);
    }

    public static void closeXComponent(XComponent xComponent) {
        XCloseable xCloseable = asXCloseable(xComponent);
        try {
            xCloseable.close(false);
        } catch (com.sun.star.util.CloseVetoException e) {
            xComponent.dispose();
        }
    }

    public static void saveDocument(XComponent xComponent) throws Exception {
        XStorable xStorable = asXStorable(xComponent);
        xStorable.store();
    }

    public static void saveAsDocument(XComponent xComponent, String path, PropertyValue[] props) throws java.io.IOException, IOException {
        File newFile = new File(path);
        newFile.createNewFile();
        XStorable xStorable = asXStorable(xComponent);
        xStorable.storeToURL(pathToUrl(path), props);
    }

    public static long replaceInDocument(XTextDocument xTextDocument, String searchString, String replaceString, boolean isRegexp) {
        XReplaceable xReplaceable = (XReplaceable) UnoRuntime.queryInterface(XReplaceable.class, xTextDocument);
        XReplaceDescriptor xRepDesc = xReplaceable.createReplaceDescriptor();
        // set a string to search for
        xRepDesc.setSearchString(searchString);
        // set the string to be inserted
        xRepDesc.setReplaceString(replaceString);
        // create an array of one property value for a CharWeight property
        PropertyValue[] aReplaceArgs = new PropertyValue[0];

        try {
            if (isRegexp) {
                xRepDesc.setPropertyValue("SearchRegularExpression", true);
            }

            // set our sequence with one property value as ReplaceAttribute
            XPropertyReplace xPropRepl = (XPropertyReplace) UnoRuntime.queryInterface(
                    XPropertyReplace.class, xRepDesc);
            xPropRepl.setReplaceAttributes(aReplaceArgs);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        // replace
        return xReplaceable.replaceAll(xRepDesc);
    }

    /*
    *  Utility method. Converts path to url
    */

    public static String pathToUrl(String sURL) throws java.io.IOException {
        java.io.File sourceFile = new java.io.File(sURL);
        StringBuffer sTmp = new StringBuffer("file:///");
        sTmp.append(sourceFile.getCanonicalPath().replace('\\', '/'));
        return sTmp.toString();
    }
}
