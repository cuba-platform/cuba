/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.report.formatters.doctags;

import com.haulmont.cuba.report.formatters.oo.OOOConnection;
import com.haulmont.cuba.report.formatters.oo.OfficeComponent;
import com.sun.star.awt.Size;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.drawing.XShape;
import com.sun.star.graphic.XGraphic;
import com.sun.star.graphic.XGraphicProvider;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.lib.uno.adapter.ByteArrayToXInputStreamAdapter;
import com.sun.star.text.HoriOrientation;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextRange;
import com.sun.star.uno.XComponentContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.haulmont.cuba.report.formatters.oo.ODTUnoConverter.*;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class BitmapTagHandler implements TagHandler {

    private final static String REGULAR_EXPRESSION = "\\$\\{bitmap:([0-9]+?)x([0-9]+?)\\}";

    private static final String TEXT_GRAPHIC_OBJECT = "com.sun.star.text.TextGraphicObject";
    private static final String GRAPHIC_PROVIDER_OBJECT = "com.sun.star.graphic.GraphicProvider";

    private static final int IMAGE_FACTOR = 27;

    private Pattern tagPattern;

    public BitmapTagHandler() {
        tagPattern = Pattern.compile(REGULAR_EXPRESSION, Pattern.CASE_INSENSITIVE);
    }

    public Pattern getTagPattern() {
        return tagPattern;
    }

    /**
     * Insert image in Doc document
     *
     * @param officeComponent OpenOffice Objects
     * @param destination     Text
     * @param textRange       Place for insert
     * @param paramValue      Parameter
     * @param paramsMatcher   Matcher for parameters regexp
     * @throws Exception
     */
    public void handleTag(OfficeComponent officeComponent,
                          XText destination, XTextRange textRange,
                          Object paramValue, Matcher paramsMatcher) throws Exception {

        boolean inserted = false;
        if (paramValue != null) {
            byte[] imageContent = (byte[]) paramValue;
            if (imageContent.length != 0) {
                int width = Integer.parseInt(paramsMatcher.group(1));
                int height = Integer.parseInt(paramsMatcher.group(2));
                try {
                    XComponent xComponent = officeComponent.getOfficeComponent();
                    OOOConnection connection = officeComponent.getOfficeConnection();
                    insertImage(xComponent, connection, destination, textRange, imageContent, width, height);
                    inserted = true;
                } catch (Exception ignored){
                }
            }
        }
        if (!inserted)
            destination.getText().insertString(textRange, "", true);
    }

    private void insertImage(XComponent document, OOOConnection connection, XText destination, XTextRange textRange,
                             byte[] imageContent, int width, int height) throws Exception {
        XMultiServiceFactory xFactory = asXMultiServiceFactory(document);
        XComponentContext xComponentContext = connection.getxComponentContext();
        XMultiComponentFactory serviceManager = xComponentContext.getServiceManager();

        Object oImage = xFactory.createInstance(TEXT_GRAPHIC_OBJECT);
        Object oGraphicProvider = serviceManager.createInstanceWithContext(GRAPHIC_PROVIDER_OBJECT, xComponentContext);

        XGraphicProvider xGraphicProvider = asXGraphicProvider(oGraphicProvider);

        XPropertySet imageProperties = buildImageProperties(xGraphicProvider, oImage, imageContent);
        XTextContent xTextContent = asXTextContent(oImage);
        destination.insertTextContent(textRange, xTextContent, true);
        setImageSize(width, height, oImage, imageProperties);
    }

    private void setImageSize(int width, int height, Object oImage, XPropertySet imageProperties)
            throws Exception {
        Size aActualSize = (Size) imageProperties.getPropertyValue("ActualSize");
        aActualSize.Height = height * IMAGE_FACTOR;
        aActualSize.Width = width * IMAGE_FACTOR;
        XShape xShape = asXShape(oImage);
        xShape.setSize(aActualSize);
    }

    private XPropertySet buildImageProperties(XGraphicProvider xGraphicProvider, Object oImage, byte[] imageContent)
            throws Exception {
        XPropertySet imageProperties = asXPropertySet(oImage);

        PropertyValue[] propValues = new PropertyValue[] { new PropertyValue() } ;
        propValues[0].Name = "InputStream";
        propValues[0].Value = new ByteArrayToXInputStreamAdapter(imageContent);

        XGraphic graphic = xGraphicProvider.queryGraphic(propValues);
        if (graphic != null) {
            imageProperties.setPropertyValue("Graphic", graphic);

            imageProperties.setPropertyValue("HoriOrient", HoriOrientation.NONE);
            imageProperties.setPropertyValue("VertOrient", HoriOrientation.NONE);

            imageProperties.setPropertyValue("HoriOrientPosition", 0);
            imageProperties.setPropertyValue("VertOrientPosition", 0);
        }

        return imageProperties;
    }
}