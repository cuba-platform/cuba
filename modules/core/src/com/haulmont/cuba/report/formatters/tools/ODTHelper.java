package com.haulmont.cuba.report.formatters.tools;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.app.FileStorageService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.sun.star.beans.PropertyValue;
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XEnumeration;
import com.sun.star.container.XEnumerationAccess;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XStorable;
import com.sun.star.io.IOException;
import com.sun.star.io.XInputStream;
import com.sun.star.io.XOutputStream;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XCloseable;
import ooo.connector.BootstrapSocketConnector;

import java.io.File;
import java.util.Map;

import static com.haulmont.cuba.report.formatters.tools.ODTUnoConverter.*;

/**
 * Created by IntelliJ IDEA.
 * User: fontanenko
 * Date: 23.06.2010
 * Time: 14:15:08
 * To change this template use File | Settings | File Templates.
 */
public class ODTHelper {

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

    public static void replaceInDocumentText(XTextDocument xTextDocument, Map<String, String> replacements, boolean isRegExp) throws NoSuchElementException, WrappedTargetException {
        XText xText = xTextDocument.getText();
        XEnumerationAccess paragraphsAccess = asXEnumerationAccess(xText);
        XEnumeration paragraphs = paragraphsAccess.createEnumeration();
        while (paragraphs.hasMoreElements()) {
            XTextContent xTextContent = asXTextContent(paragraphs.nextElement());
            XServiceInfo xServiceInfo = asXServiceInfo(xTextContent);
            // Accessing only paragraphs, not tables
            if (!xServiceInfo.supportsService("com.sun.star.text.TextTable")) {
                XEnumerationAccess textPortionsAccess = asXEnumerationAccess(xTextContent);
                XEnumeration textPortions = textPortionsAccess.createEnumeration();
                while (textPortions.hasMoreElements()) {
                    XTextRange xTextPortion = asXTextRange(textPortions.nextElement());
                    for (String target : replacements.keySet()) {
                        if (isRegExp) {
                            String sourceString = xTextPortion.getString();
                            String resultString = sourceString.replaceAll(target, replacements.get(target));
                            xTextPortion.setString(resultString);
                        } else {
                            xTextPortion.setString(xTextPortion.getString().replace(target, replacements.get(target)));
                        }
                    }
                }
            }
        }
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
